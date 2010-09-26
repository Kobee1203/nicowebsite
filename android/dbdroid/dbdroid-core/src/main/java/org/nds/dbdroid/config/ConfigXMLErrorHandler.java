package org.nds.dbdroid.config;

import org.nds.dbdroid.DataBaseManager;
import org.nds.dbdroid.log.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ConfigXMLErrorHandler implements ErrorHandler {
	
	private static final Logger log = Logger.getLogger(DataBaseManager.class);
	
	public void warning(SAXParseException e) throws SAXException {
		log.warn(e.getMessage(), e);
	}
	
	public void fatalError(SAXParseException e) throws SAXException {
		throw new SAXException(e.getMessage(), e);
	}
	
	public void error(SAXParseException e) throws SAXException {
		throw new SAXException(e.getMessage(), e);
	}
}
