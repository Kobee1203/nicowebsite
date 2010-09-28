package org.nds.dbdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.nds.dbdroid.config.ConfigXMLErrorHandler;
import org.nds.dbdroid.config.ConfigXMLHandler;
import org.nds.dbdroid.dao.AndroidDAO;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.helper.EntityHelper;
import org.nds.dbdroid.helper.Field;
import org.nds.dbdroid.log.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;

public abstract class DataBaseManager {

    private static final Logger log = Logger.getLogger(DataBaseManager.class);

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final String CLASSPATH_PREFIX = "classpath:";

    private static final String CREATE_VALUE = "create";
    private static final String UPDATE_VALUE = "update";
    private static final String RESET_VALUE = "reset";

    private enum PropertyKey {
        GENERATE_DB("dbdroid.generate"), SHOW_QUERY("dbdroid.show_query"), SCRIPT("dbdroid.script"), SCRIPT_ENCODING("dbdroid.script_encoding");

        private String key;

        private PropertyKey(String key) {
            this.key = key;
        }

        public static PropertyKey getValueOf(String key) {
            for (PropertyKey propertyKey : values()) {
                if (key.equals(propertyKey.key)) {
                    return propertyKey;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return this.key;
        }
    }

    private Properties properties;
    private Map<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> daos = new HashMap<Class<? extends AndroidDAO<?>>, AndroidDAO<?>>();

    private final Map<String, Class<?>> entityFromTableName = new HashMap<String, Class<?>>();
    private final Map<Class<?>, AndroidDAO<?>> daoFromEntity = new HashMap<Class<?>, AndroidDAO<?>>();

    public DataBaseManager(InputStream config) throws DBDroidException {
        if (config != null) {
            loadConfig(config, false);
        } else {
            log.warn("XML dbdroid configuration not found." + (config == null ? "Config inputStream object is NULL." : ""));
        }
    }

    public DataBaseManager(InputStream config, boolean validate) throws DBDroidException {
        if (config != null) {
            loadConfig(config, validate);
        } else {
            log.warn("XML dbdroid configuration not found." + (config == null ? "Config inputStream object is NULL." : ""));
        }
    }

    private void loadConfig(InputStream config, boolean validate) throws DBDroidException {
        try {
            /** Handling XML */
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(validate);
            SAXParser parser = factory.newSAXParser();
            if (validate) {
                try {
                    parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                    parser.setProperty(JAXP_SCHEMA_SOURCE, new File(getClass().getResource("/xsd/dbdroid.xsd").toURI()));
                } catch (SAXNotRecognizedException x) {
                    // Happens if the parser does not support JAXP 1.2
                    log.debug("parser does not support JAXP 1.2");
                }
            }
            XMLReader reader = parser.getXMLReader();

            /** Create handler to handle XML Tags ( extends DefaultHandler ) */
            ConfigXMLHandler configXMLHandler = new ConfigXMLHandler(this);
            reader.setErrorHandler(new ConfigXMLErrorHandler());
            reader.setContentHandler(configXMLHandler);
            reader.parse(new InputSource(config));

            daos = configXMLHandler.getDaos();
            properties = configXMLHandler.getProperties();
        } catch (Exception e) {
            throw new DBDroidException("XML Pasing Exception = " + e, e);
        }

        processProperties();
    }

    private void processProperties() throws DBDroidException {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            PropertyKey propertyKey = PropertyKey.getValueOf(key);
            if (propertyKey != null) {
                switch (propertyKey) {
                case GENERATE_DB:
                    log.debug("-- generate DB --");
                    generateDataBase(value);
                    break;
                case SCRIPT:
                    log.debug("-- script --");
                    String encoding = properties.getProperty(PropertyKey.SCRIPT_ENCODING.toString());
                    runScript(value, encoding);
                    break;
                case SCRIPT_ENCODING:
                    log.debug("-- script encoding: " + value + " --");
                case SHOW_QUERY:
                    log.debug("-- show query --");
                    break;
                default:
                    log.info("Property key: " + key + " (value: " + value + ")");
                }
            } else {
                log.warn("Unknown property key: " + key);
            }
        }
    }

    public <T extends AndroidDAO<?>> T getDAO(Class<T> daoClass) {
        T dao = (T) daos.get(daoClass);
        if (dao == null) {
            throw new NullPointerException("DAO class '" + daoClass + "' not found. Verify the XML dbdroid configuration.");
        }
        return dao;
    }

    protected AndroidDAO<?> getDAOFromEntity(Class<?> entity) {
        return daoFromEntity.get(entity);
    }

    protected Class<?> getEntityFromTableName(String tableName) {
        return entityFromTableName.get(tableName);
    }

    private void generateDataBase(String type) throws DBDroidException {
        try {
            for (Map.Entry<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> e : daos.entrySet()) {
                AndroidDAO<?> dao = e.getValue();
                Class<?> entityClass = dao.getEntityClass();
                log.debug("entityClass: " + entityClass);
                String tableName = EntityHelper.getTableName(entityClass);
                log.debug("Table name: " + tableName);
                List<Field> fields = EntityHelper.getFields(entityClass);
                log.debug("fields: " + fields);

                daoFromEntity.put(entityClass, dao);
                entityFromTableName.put(tableName, entityClass);

                if (CREATE_VALUE.equalsIgnoreCase(type)) {
                    onCreateTable(tableName, fields);
                } else if (UPDATE_VALUE.equalsIgnoreCase(type)) {
                    onUpdateTable(tableName, fields);
                } else if (RESET_VALUE.equalsIgnoreCase(type)) {
                    onResetTable(tableName, fields);
                }
            }
        } catch (Exception e) {
            throw new DBDroidException(e.getMessage(), e);
        }
    }

    private void runScript(String scriptPath, String encoding) throws DBDroidException {
        try {
            List<String> queries = getQueries(scriptPath, encoding);
            for (String query : queries) {
                rawQuery(query.trim());
            }
        } catch (Exception e) {
            throw new DBDroidException(e.getMessage(), e);
        }
    }

    private List<String> getQueries(String value, String encoding) throws IOException {
        List<String> queries = new ArrayList<String>();

        if (value != null) {
            InputStream is;
            if (value.startsWith(CLASSPATH_PREFIX)) {
                is = getClass().getResourceAsStream(value.substring(value.indexOf(CLASSPATH_PREFIX) + (CLASSPATH_PREFIX.length())));
            } else {
                is = new FileInputStream(new File(value));
            }

            if (is == null) {
                throw new IOException("Script file not found with path: " + value);
            }

            BufferedReader reader = null;
            InputStreamReader isReader = null;
            try {
                isReader = encoding != null ? new InputStreamReader(is, encoding) : new InputStreamReader(is);
                reader = new BufferedReader(isReader);

                String line;
                String query = "";
                while ((line = reader.readLine()) != null) {
                    // Skip empty lines
                    if (line.trim().equals("")) {
                        continue;
                    }

                    // Skip comments
                    if (line.startsWith("--") || line.startsWith("//") || line.startsWith("#")) {
                        continue;
                    }

                    query += " " + line;

                    if (query.endsWith("/")) { // complete command
                        query = query.replace('/', ' '); // Remove the '/' since
                                                         // jdbc complains
                        queries.add(replaceArguments(query, (String[]) null));
                        query = "";
                    } else if (query.contains(";")) { // One or several complete
                                                      // query(ies)
                        String[] q = query.split(";");
                        // Loop on different queries
                        for (int i = 0; i < (q.length - 1); i++) {
                            queries.add(replaceArguments(q[i], (String[]) null));
                        }
                        // Check if the line ends with
                        if (query.endsWith(";")) {
                            queries.add(replaceArguments(q[q.length - 1], (String[]) null));
                            query = "";
                        } else {
                            query = q[q.length - 1];
                        }
                    }
                }
            } finally {
                try {
                    is.close();
                } catch (Exception exc) {
                }
                try {
                    isReader.close();
                } catch (Exception exc) {
                }
                try {
                    reader.close();
                } catch (Exception exc) {
                }
            }
        }
        return queries;
    }

    private static String replaceArguments(String sqlQuery, String... args) {
        String query = sqlQuery;
        if (args != null) {
            for (int a = 0; a < args.length; a++) {
                query = query.replace("&" + (a + 1), args[a]);
            }
        }

        return query;
    }

    public abstract void open();

    public abstract void close();

    protected abstract void onCreateTable(String tableName, List<Field> fields);

    protected abstract void onUpdateTable(String tableName, List<Field> fields);

    protected abstract void onResetTable(String tableName, List<Field> fields);

    public abstract void delete(Object entity);

    public abstract <E> List<E> findAll(Class<E> entityClass);

    public abstract <E> E findById(String id, Class<E> entityClazz);

    public abstract <E> E saveOrUpdate(E entity);

    public abstract void rawQuery(String query);

}
