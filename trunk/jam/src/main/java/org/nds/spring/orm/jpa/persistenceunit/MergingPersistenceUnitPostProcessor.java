package org.nds.spring.orm.jpa.persistenceunit;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.spi.PersistenceUnitInfo;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * This merges all JPA entities from multiple jars. To use it, all entities must
 * be listed in their respective persistence.xml files using the <class> tag.
 * To use it, I found that this only works if do all of the following:
 * - All your entities need to be mentioned in your persistence.xml files. Normally you don't have to do this is if you set up entity scanning in your
 * Spring config
 * - All your persistence units use the same name
 * - You have to use a particular entity manager factory, namely LocalContainerEntityManagerFactoryBean
 * - You have to have define your persistence provider in persistence.xml, in our case org.hibernate.ejb.HibernatePersistence
 * - Each persistence.xml file needs a unique name, because they are enumerated in the application context.
 * 
 * @see http://forum.springsource.org/showthread.php?t=61763
 */
public class MergingPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {
	Map<String, PersistenceUnitInfo> puis = new HashMap<String, PersistenceUnitInfo>();

	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo mpui) {
		PersistenceUnitInfo pui = puis.get(mpui.getPersistenceUnitName());
		if (pui != null) {
			mpui.getJarFileUrls().addAll(pui.getJarFileUrls());
			mpui.getManagedClassNames().addAll(pui.getManagedClassNames());
			mpui.getMappingFileNames().addAll(pui.getMappingFileNames());
			mpui.getProperties().putAll(pui.getProperties());
		}

		puis.put(mpui.getPersistenceUnitName(), mpui);
	}
}
