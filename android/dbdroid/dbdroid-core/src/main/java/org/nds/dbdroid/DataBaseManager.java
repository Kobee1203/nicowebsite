package org.nds.dbdroid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.dbdroid.config.ConfigXMLErrorHandler;
import org.nds.dbdroid.config.ConfigXMLHandler;
import org.nds.dbdroid.dao.AndroidDAO;
import org.nds.dbdroid.exception.DBDroidException;
import org.nds.dbdroid.helper.EntityHelper;
import org.nds.dbdroid.helper.Field;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;

public abstract class DataBaseManager {

    private static final Log log = LogFactory.getLog(DataBaseManager.class);

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

    private static final String CLASSPATH_PREFIX = "classpath:";

    private static final String CREATE_VALUE = "create";
    private static final String UPDATE_VALUE = "update";
    private static final String RESET_VALUE = "reset";

    private enum PropertyKey {
        GENERATE_DB("dbdroid.generate"),
        SHOW_QUERY("dbdroid.show_query"),
        SCRIPT_ENCODING("dbdroid.script_encoding"),
        CREATING_SCRIPT("dbdroid.creating_script"),
        UPDATING_SCRIPT("dbdroid.updating_script"),
        RESETING_SCRIPT("dbdroid.reseting_script");

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

    public DataBaseManager(InputStream config) throws DBDroidException {
        if (config != null) {
            loadConfig(config);
        } else {
            log.warn("XML dbdroid configuration not found." + (config == null ? "Config inputStream object is NULL." : ""));
        }
    }

    private void loadConfig(InputStream config) throws DBDroidException {
        try {
            /** Handling XML */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setValidating(true);
            SAXParser sp = spf.newSAXParser();
            try {
                sp.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
                sp.setProperty(JAXP_SCHEMA_SOURCE, new File(getClass().getResource("/xsd/dbdroid.xsd").toURI()));
            } catch (SAXNotRecognizedException x) {
                // Happens if the parser does not support JAXP 1.2
                log.debug("parser does not support JAXP 1.2");
            }
            XMLReader xr = sp.getXMLReader();

            /** Create handler to handle XML Tags ( extends DefaultHandler ) */
            ConfigXMLHandler configXMLHandler = new ConfigXMLHandler(this);
            xr.setErrorHandler(new ConfigXMLErrorHandler());
            xr.setContentHandler(configXMLHandler);
            xr.parse(new InputSource(config));

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
                        if (CREATE_VALUE.equalsIgnoreCase(value)) {
                            onCreate();
                        } else if (UPDATE_VALUE.equalsIgnoreCase(value)) {
                            onUpdate();
                        } else if (RESET_VALUE.equalsIgnoreCase(value)) {
                            onReset();
                        }
                        break;
                    case CREATING_SCRIPT:
                        log.debug("-- creating script --");
                        break;
                    case UPDATING_SCRIPT:
                        log.debug("-- updating script --");
                        break;
                    case RESETING_SCRIPT:
                        log.debug("-- reseting script --");
                        break;
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

    private void onCreate() throws DBDroidException {
        try {
            String scriptPath = properties.getProperty(PropertyKey.CREATING_SCRIPT.toString());
            String encoding = properties.getProperty(PropertyKey.SCRIPT_ENCODING.toString());
            String script = getScriptContent(scriptPath, encoding);

            createDataBase(script);

            for (Map.Entry<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> e : daos.entrySet()) {
                AndroidDAO<?> dao = e.getValue();
                Class<?> entityClass = dao.getEntityClass();
                log.debug("entityClass: " + entityClass);
                String tableName = EntityHelper.getTableName(entityClass);
                log.debug("Table name: " + EntityHelper.getTableName(entityClass));
                List<Field> fields = EntityHelper.getFields(entityClass);
                log.debug("fields: " + fields);
                createTable(tableName, fields);
            }
        } catch (Exception e) {
            throw new DBDroidException(e.getMessage(), e);
        }
    }

    private void onUpdate() throws DBDroidException {
        try {
            String scriptPath = properties.getProperty(PropertyKey.UPDATING_SCRIPT.toString());
            String encoding = properties.getProperty(PropertyKey.SCRIPT_ENCODING.toString());
            String script = getScriptContent(scriptPath, encoding);

            updateDataBase(script);

            for (Map.Entry<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> e : daos.entrySet()) {
                AndroidDAO<?> dao = e.getValue();
                Class<?> entityClass = dao.getEntityClass();
                log.debug("entityClass: " + entityClass);
                String tableName = EntityHelper.getTableName(entityClass);
                log.debug("Table name: " + EntityHelper.getTableName(entityClass));
                List<Field> fields = EntityHelper.getFields(entityClass);
                log.debug("fields: " + fields);
                updateTable(tableName, fields);
            }
        } catch (Exception e) {
            throw new DBDroidException(e.getMessage(), e);
        }
    }

    private void onReset() throws DBDroidException {
        try {
            String scriptPath = properties.getProperty(PropertyKey.RESETING_SCRIPT.toString());
            String encoding = properties.getProperty(PropertyKey.SCRIPT_ENCODING.toString());
            String script = getScriptContent(scriptPath, encoding);

            resetDataBase(script);

            for (Map.Entry<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> e : daos.entrySet()) {
                AndroidDAO<?> dao = e.getValue();
                Class<?> entityClass = dao.getEntityClass();
                log.debug("entityClass: " + entityClass);
                String tableName = EntityHelper.getTableName(entityClass);
                log.debug("Table name: " + EntityHelper.getTableName(entityClass));
                List<Field> fields = EntityHelper.getFields(entityClass);
                log.debug("fields: " + fields);
                resetTable(tableName, fields);
            }
        } catch (Exception e) {
            throw new DBDroidException(e.getMessage(), e);
        }
    }

    private String getScriptContent(String value, String encoding) throws IOException {
        if (value != null) {
            InputStream is;
            if (value.startsWith(CLASSPATH_PREFIX)) {
                is = getClass().getResourceAsStream(value.substring(value.indexOf(CLASSPATH_PREFIX) + (CLASSPATH_PREFIX.length() + 1)));
            } else {
                is = new FileInputStream(new File(value));
            }

            StringBuilder sb = new StringBuilder();
            String line;

            BufferedReader reader = null;
            InputStreamReader isReader = null;
            try {
                isReader = new InputStreamReader(is, encoding);
                reader = new BufferedReader(isReader);
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("--") && !line.startsWith("//") && !line.startsWith("#")) {
                        sb.append(line).append("\n");
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
            return sb.toString();

        }
        return null;
    }

    protected void createDataBase(String script) {
    }

    protected void updateDataBase(String script) {
    }

    protected void resetDataBase(String script) {
    }

    public abstract void open();

    public abstract void close();

    protected abstract void createTable(String tableName, List<Field> fields);

    protected abstract void updateTable(String tableName, List<Field> fields);

    protected abstract void resetTable(String tableName, List<Field> fields);

    public abstract void delete(Object entity);

    public abstract <E> List<E> findAll(Class<E> entityClass);

    public abstract <E> E findById(String id, Class<E> entityClazz);

    public abstract <E> E saveOrUpdate(E entity);

}
