package org.nds.dbdroid;

import java.io.File;
import java.io.InputStream;
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.XMLReader;

public abstract class DataBaseManager {

	private static final Log log = LogFactory.getLog(DataBaseManager.class);

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	private enum PropertyKey {
		GENERATE_DB("dbdroid.generate"), 
		SHOW_SQL("dbdroid.show_sql");

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
	}

	private Properties properties;
	private Map<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> daos = new HashMap<Class<? extends AndroidDAO<?>>, AndroidDAO<?>>();

	public DataBaseManager(InputStream config) throws DBDroidException {
		loadConfig(config);
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

	private void processProperties() {
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();

			switch (PropertyKey.getValueOf(key)) {
			case GENERATE_DB:
				log.info("-- generate DB --");
				break;
			case SHOW_SQL:
				log.info("-- show SQL --");
				break;
			default:
				log.error("Unknown property key: " + key + " (value: " + value + ")");
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

	public abstract void open();

	public abstract void close();

	public abstract void delete(Object entity);

	public abstract <E> List<E> findAll(Class<E> entityClass);

	public abstract <E> E findById(String id, Class<E> entityClazz);

	public abstract <E> E saveOrUpdate(E entity);

}
