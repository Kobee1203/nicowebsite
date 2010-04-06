package org.nds.spring.orm.jpa.persistenceunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * This merges all JPA entities from multiple jars. To use it, all entities must
 * be listed in their respective persistence.xml files using the <class> tag.
 * To use it, I found that this only works if do all of the following:
 * - All your entities need to be mentioned in your persistence.xml files. Normally you don't have to do this is if you set up entity scanning in your
 * Spring config
 * - All your persistence units use the same name, and furthermore it has to be the default name, namely "default"
 * - You have to use a particular entity manager factory, namely LocalContainerEntityManagerFactoryBean
 * - You have to have define your persistence provider in persistence.xml, in our case org.hibernate.ejb.HibernatePersistence
 * - Each persistence.xml file needs a unique name, because they are enumerated in the application context.
 * 
 * @see http://forum.springsource.org/showthread.php?t=61763
 */
public class MergingPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {
	Map<String, List<String>> puiClasses = new HashMap<String, List<String>>();

	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		List<String> classes = puiClasses.get(pui.getPersistenceUnitName());
		if (classes == null) {
			classes = new ArrayList<String>();
			puiClasses.put(pui.getPersistenceUnitName(), classes);
		}
		pui.getManagedClassNames().addAll(classes);

		classes.addAll(pui.getManagedClassNames());
	}
}
