package org.nds.spring.orm.jpa.persistenceunit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.persistence.EntityManagerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClassLoaderProxy extends ClassLoader {

	Log logger = LogFactory.getLog(ClassLoaderProxy.class);

	private static final String PERSISTENCE_XML = "META-INF/persistence.xml";

	private static final String MASTER_URL_SUFFIX = "it-tidalwave-bluemarine-persistence.jar!/" + PERSISTENCE_XML;

	private static final DocumentBuilderFactory DOC_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

	private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

	private static final XPath XPATH = XPATH_FACTORY.newXPath();

	private static final XPathExpression XPATH_ENTITY_PU_NODE;
	private static final XPathExpression XPATH_ENTITY_CLASS_TEXT;

	enum Filter {
		MASTER {
			@Override
			public boolean filter(URL url) {
				return url.toExternalForm().endsWith(MASTER_URL_SUFFIX);
			}
		},

		OTHERS {
			@Override
			public boolean filter(URL url) {
				return !url.toExternalForm().endsWith(MASTER_URL_SUFFIX);
			}
		};

		public abstract boolean filter(URL url);
	}

	static {
		try {
			XPATH_ENTITY_PU_NODE = XPATH.compile("//persistence/persistence-unit");
			XPATH_ENTITY_CLASS_TEXT = XPATH.compile("//persistence/persistence-unit/class/text()");
		} catch (XPathExpressionException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private URL persistenceXMLurl;

	public ClassLoaderProxy(final ClassLoader parent) {
		super(parent);
	}

	/**
	 * Scan the classpath for the JPA configuration files
	 * 
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	private Collection<URL> findPersistenceXMLs(final Filter filter) throws IOException {
		final Collection<URL> result = new ArrayList<URL>();

		for (final Enumeration<URL> e = super.getResources(PERSISTENCE_XML); e.hasMoreElements();) {
			final URL url = e.nextElement();

			if (filter.filter(url)) {
				result.add(url);
			}
		}

		return result;
	}

	/**
	 * extracts all the "classes" declarations from the secondary files and inserts them in the master file
	 **/
	private String scanPersistenceXML() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException,
	        TransformerConfigurationException, TransformerException {
		logger.info("scanPersistenceXML()");
		final DocumentBuilder builder = DOC_BUILDER_FACTORY.newDocumentBuilder();
		DOC_BUILDER_FACTORY.setNamespaceAware(true);

		final URL masterURL = findPersistenceXMLs(Filter.MASTER).iterator().next();
		logger.info(String.format(">>>> master persistence.xml: %s", masterURL));
		final Document masterDocument = builder.parse(masterURL.toExternalForm());
		final Node puNode = (Node) XPATH_ENTITY_PU_NODE.evaluate(masterDocument, XPathConstants.NODE);

		for (final URL url : findPersistenceXMLs(Filter.OTHERS)) {
			logger.info(String.format(">>>> other persistence.xml: %s", url));
			final Document document = builder.parse(url.toExternalForm());
			final NodeList nodes = (NodeList) XPATH_ENTITY_CLASS_TEXT.evaluate(document, XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				final String entityClassName = nodes.item(i).getNodeValue();
				logger.info(String.format(">>>>>>>> entity class: %s", entityClassName));

				if (i == 0) {
					puNode.appendChild(masterDocument.createTextNode("\n"));
					puNode.appendChild(masterDocument.createComment(" from " + url.toExternalForm().replaceAll(".*/cluster/modules/", "") + " "));
					puNode.appendChild(masterDocument.createTextNode("\n"));
				}

				final Node child = masterDocument.createElement("class");
				child.appendChild(masterDocument.createTextNode(entityClassName));
				puNode.appendChild(child);
				puNode.appendChild(masterDocument.createTextNode("\n"));
			}
		}

		return toString(masterDocument);
	}

	/**
	 * Called by JPA to retrieve the persistence.xml file.
	 * This method must return the URL of the requested resource
	 * and the idea is that we will not return the master file URL, rather the URL of our synthetised file
	 */
	@Override
	public Enumeration<URL> getResources(final String name) throws IOException {
		if (PERSISTENCE_XML.equals(name)) {
			if (persistenceXMLurl == null) {
				try {
					final String persistenceXml = scanPersistenceXML();
					logger.info("persistence.xml " + persistenceXml);

					// The base directory must be empty since Hibernate will scan it searching for classes.
					final File file = new File(System.getProperty("java.io.tmpdir") + "/blueMarinePU/" + PERSISTENCE_XML);
					file.getParentFile().mkdirs();
					final PrintWriter pw = new PrintWriter(new FileWriter(file));
					pw.print(persistenceXml);
					pw.close();
					persistenceXMLurl = new URL("file://" + file.getAbsolutePath());
					logger.info("URL: " + persistenceXMLurl);
				} catch (ParserConfigurationException e) {
					throw new IOException(e.toString());
				} catch (SAXException e) {
					throw new IOException(e.toString());
				} catch (XPathExpressionException e) {
					throw new IOException(e.toString());
				} catch (TransformerConfigurationException e) {
					throw new IOException(e.toString());
				} catch (TransformerException e) {
					throw new IOException(e.toString());
				}
			}

			return new Enumeration<URL>() {
				URL url = persistenceXMLurl;

				public boolean hasMoreElements() {
					return url != null;
				}

				public URL nextElement() {
					final URL url2 = url;
					url = null;
					return url2;
				}
			};
		}

		return super.getResources(name);
	}

	/*
	public synchronized EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			final Properties properties = configuration.getProperties();
			final Thread currentThread = Thread.currentThread();
			final ClassLoader saveClassLoader = currentThread.getContextClassLoader();
			currentThread.setContextClassLoader(new ClassLoaderProxy(saveClassLoader));
			entityManagerFactory = javax.persistence.Persistence.createEntityManagerFactory("BLUEMARINE_PU", configuration.getProperties());
			currentThread.setContextClassLoader(saveClassLoader);
		}

		return entityManagerFactory;
	}
	*/
}
