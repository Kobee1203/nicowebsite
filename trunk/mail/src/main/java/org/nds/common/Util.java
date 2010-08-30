package org.nds.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

public final class Util {

	protected final static Log logger = LogFactory.getLog(Util.class);

	public static final String XML_FILE_EXTENSION = ".xml";

	public static Properties load(String propertiesPath, String defaultPath) {
		Properties props = new Properties();

		if (StringUtils.isBlank(propertiesPath) && StringUtils.isBlank(defaultPath)) {
			logger.error("No properties path defined. Return null");
			return null;
		}

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = null;
		if (!StringUtils.isBlank(propertiesPath)) {
			resource = resourceLoader.getResource(propertiesPath);
		}
		if ((resource == null || !resource.exists()) && defaultPath != null) {
			resource = resourceLoader.getResource(defaultPath);
		}

		try {
			InputStream is = resource.getInputStream();

			try {
				if (resource.getFilename().endsWith(XML_FILE_EXTENSION)) {
					props.loadFromXML(is);
				} else {
					props.load(is);
				}
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			logger.error("Error to load the properties. Check if there is a resource exists for the path '" + propertiesPath
			        + (defaultPath != null ? " or for the path '" + defaultPath + "'" : ""), e);
		}

		return props;
	}

	public static Resource getResource(File f) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		String resourcePath = ResourceUtils.FILE_URL_PREFIX + f.getAbsolutePath();
		return resourceLoader.getResource(resourcePath);
	}
}
