package org.nds.dbdroid.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.dao.AndroidDAO;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigXMLHandler extends DefaultHandler {
	
	private static final Log log = LogFactory.getLog(ConfigXMLHandler.class);

	private static final String DAO_ELEMENT = "dao";
	private static final String DAO_CLASS_ATTR = "class";
	private static final String DAO_PACKAGE_ATTR = "package";
	private static final String PROPERTIES_ELEMENT = "properties";
	private static final String PROPERTY_ELEMENT = "property";
	private static final String PROPERTY_NAME_ATTR = "name";
	private static final String PROPERTY_VALUE_ATTR = "value";

	private String text;
	private Element current = null;

	private Properties properties;
	private Map<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> daos = new HashMap<Class<? extends AndroidDAO<?>>, AndroidDAO<?>>();

	private DataBaseManager dbManager;

	public ConfigXMLHandler(DataBaseManager dbManager) {
		this.dbManager = dbManager;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		current = new Element(uri, localName, qName, attributes);
		text = new String();
		
		if (localName.equals(PROPERTIES_ELEMENT)) {
			properties = new Properties();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (current != null && text != null) {
			current.setValue(text.trim());
		}

		if (localName.equals(DAO_ELEMENT)) {
			String clazz = current.getAttributes().getValue(DAO_CLASS_ATTR);
			if ((clazz == null || clazz.trim().equals("")) && current.getValue() != null) {
				clazz = current.getValue();
			}
			if (clazz != null && !clazz.trim().equals("")) { // Class
				// Retrieve DAO
				try {
					retrieveDAO(Class.forName(clazz));
				} catch (ClassNotFoundException e) {
					throw new SAXException("Class '" + clazz + "' not found!", e);
				}
			} else { // Classes in a package
				String packageName = current.getAttributes().getValue(DAO_PACKAGE_ATTR);
				if (packageName == null || packageName.trim().equals("")) {
					throw new SAXException("'class' or 'package' attribute not defined or empty in the 'dao' element");
				} else {
					// Scan package and retrieve all DAOs
					try {
						retrieveDAOClasses(packageName);
					} catch (ClassNotFoundException e) {
						throw new SAXException("Scanning package " + packageName + ": " + e.getMessage(), e);
					} catch (IOException e) {
						throw new SAXException("Scanning package " + packageName + ": " + e.getMessage(), e);
					}
				}
			}
		} else if (localName.equals(PROPERTY_ELEMENT)) {
			if (properties == null) {
				throw new SAXException("'property' element found, but there is not 'properties' element.");
			}
			String name = current.getAttributes().getValue(PROPERTY_NAME_ATTR);
			if (name == null || name.trim().equals("")) {
				throw new SAXException("'name' attribute not defined or empty in the 'property' element");
			}
			String value = current.getAttributes().getValue(PROPERTY_VALUE_ATTR);
			if (value == null && current.getValue() != null) {
				value = current.getValue();
			}
			properties.put(name, value);
		}

		current = null;
		text = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (current != null && text != null) {
			String value = new String(ch, start, length);
			text += value;
		}
	}

	/**
	 * Create DAO object by reflection and add it to the DAOs map
	 * 
	 * @param clazz
	 *            : class name
	 * @throws SAXException
	 */
	@SuppressWarnings("unchecked")
	private void retrieveDAO(Class<?> clazz) throws SAXException {
		try {
			Class<? extends AndroidDAO<?>> daoClass = (Class<? extends AndroidDAO<?>>) clazz;
			Constructor<? extends AndroidDAO<?>> constr = daoClass.getConstructor(new Class[] { DataBaseManager.class });
			AndroidDAO<?> dao = constr.newInstance(new Object[] { dbManager });
			if (!daos.containsKey(daoClass)) {
				daos.put(daoClass, dao);
			} else {
				log.warn("Retrieve several times the same DAO '" + daoClass + "'. Verify the XML dbdroid configuration");
			}
		} catch (SecurityException e) {
			throw new SAXException("SecurityException for Class '" + clazz + "'", e);
		} catch (NoSuchMethodException e) {
			throw new SAXException("NoSuchMethodException for Class '" + clazz + "'", e);
		} catch (IllegalArgumentException e) {
			throw new SAXException("IllegalArgumentException for Class '" + clazz + "'", e);
		} catch (InstantiationException e) {
			throw new SAXException("InstantiationException for Class '" + clazz + "'", e);
		} catch (IllegalAccessException e) {
			throw new SAXException("IllegalAccessException for Class '" + clazz + "'", e);
		} catch (InvocationTargetException e) {
			throw new SAXException("InvocationTargetException for Class '" + clazz + "'", e);
		}
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages, and retrieve DAO classes.
	 * 
	 * @param packageName
	 *            The base package
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void retrieveDAOClasses(String packageName) throws ClassNotFoundException, IOException, SAXException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		for (File directory : dirs) {
			findDAOClasses(directory, packageName);
		}
	}

	/**
	 * Recursive method used to find all DAO classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @throws ClassNotFoundException
	 * @throws SAXException
	 */
	private void findDAOClasses(File directory, String packageName) throws ClassNotFoundException, SAXException {
		if (!directory.exists()) {
			throw new SAXException("Directory '+" + directory + "' not found.");
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				findDAOClasses(file, packageName + "." + file.getName());
			} else if (file.getName().endsWith(".class")) {
				Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
				if (AndroidDAO.class.equals(clazz.getSuperclass())) {
					retrieveDAO(clazz);
				}
			}
		}
	}

	public Map<Class<? extends AndroidDAO<?>>, AndroidDAO<?>> getDaos() {
		return daos;
	}
	
	public Properties getProperties() {
		return properties;
	}
}
