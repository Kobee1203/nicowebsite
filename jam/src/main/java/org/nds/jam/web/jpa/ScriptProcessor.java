package org.nds.jam.web.jpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.util.ConfigHelper;
import org.springframework.beans.factory.InitializingBean;

public class ScriptProcessor implements InitializingBean {

	private static final Log log = LogFactory.getLog(ScriptProcessor.class);

	private SessionFactory sessionFactory;
	private List<String> scripts;

	public void afterPropertiesSet() {
		for (final String script : scripts) {
			Session session = sessionFactory.openSession();
			session.doWork(new Work() {
				public void execute(Connection connection) throws SQLException {
					if (!connection.getAutoCommit()) {
						connection.commit();
						connection.setAutoCommit(false);
					}
					Statement statement = connection.createStatement();
					InputStream stream = null;
					try {
						stream = ConfigHelper.getResourceAsStream(script);
						String script = loadScript(stream);
						String[] queries = script.split(";");
						for (String query : queries) {
							if (!query.trim().isEmpty()) {
								log.debug(query);
								statement.executeUpdate(query);
							}
						}
					} finally {
						try {
							stream.close();
						} catch (Exception exc) {
						}
					}
					connection.commit();
				}
			});
			session.close();
		}
	}

	protected String loadScript(InputStream stream) {
		StringBuilder sb = new StringBuilder();

		BufferedReader reader = null;
		Reader in = null;
		try {
			in = new InputStreamReader(stream);
			reader = new BufferedReader(in);

			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("--") && !line.startsWith("//") && !line.startsWith("#")) {
					sb.append(line);
				}
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception exc) {
			}
			try {
				reader.close();
			} catch (Exception exc) {
			}
		}

		return sb.toString();
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @param scripts
	 *            the scripts to set
	 */
	public void setScripts(List<String> scripts) {
		this.scripts = scripts;
	}
}
