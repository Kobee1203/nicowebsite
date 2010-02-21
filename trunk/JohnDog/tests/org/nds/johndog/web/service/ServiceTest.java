package org.nds.johndog.web.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.nds.johndog.model.Domain;
import org.nds.johndog.model.Item;
import org.nds.johndog.model.Property;
import org.nds.johndog.model.PropertyType;
import org.nds.johndog.model.Rights;
import org.nds.johndog.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ServiceTest {
	protected final static Log log = LogFactory.getLog(ServiceTest.class);

	private static ApplicationContext ctx = null;

	private static UserManager userManager;
	private static HomeService homeService;

	enum PROPERTY_TYPE {
		STRING, BOOLEAN, DATE, INTEGER, LOB
	}

	enum PROPERTY {
		TITLE("Title", PROPERTY_TYPE.STRING),
		IMAGE("Image", PROPERTY_TYPE.LOB),
		VIDEO("Video", PROPERTY_TYPE.LOB)
		;

		String name;
		PROPERTY_TYPE propertyType;

		private PROPERTY(String name, PROPERTY_TYPE propertyType) {
			this.name = name;
			this.propertyType = propertyType;
		}

		public String getName() {
			return name;
		}

		public PROPERTY_TYPE getPropertyType() {
			return propertyType;
		}
	}

	enum DOMAIN {
		CULTURE, ENVIRONMENT, CINEMA, MUSIC, POLITIC
	}

	@Ignore
	private static void makeUser() {
		//User user = new User("admin", "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
		User user = new User("admin", "admin");
		user.setFirstName("Nicolas");
		user.setLastName("Dos Santos");
		Set<Rights> rights = new HashSet<Rights>();
		rights.add(new Rights(user, "USER"));
		rights.add(new Rights(user, "ADMIN"));
		user.setRights(rights);
		
		user = userManager.saveUser(user);

/*
		Rights auth1 = new Rights(user, "ROLE_USER");
		Rights auth2 = new Rights(user, "ROLE_ADMIN");
		userManager.saveRights(auth1);
		userManager.saveRights(auth2);
		
		homeService.saveUser(user);
*/
	}
	@Ignore
	private static void deleteUser() {
		userManager.deleteUser("admin");
	}

	@Ignore
	private static void makePropertyType() {
		for(PROPERTY_TYPE propType : PROPERTY_TYPE.values()) {
			PropertyType propertyType = new PropertyType(propType.toString());
			homeService.savePropertyType(propertyType);
		}
	}
	@Ignore
	private static void deletePropertyType() {
		for(PROPERTY_TYPE propType : PROPERTY_TYPE.values()) {
			homeService.deletePropertyType(propType.toString());
		}
	}

	@Ignore
	private static void makeProperty() {
		for(PROPERTY prop : PROPERTY.values()) {
			List<PropertyType> propertiesType = homeService.getPropertyType(prop.getPropertyType().toString());
			Property property = new Property(prop.getName(), propertiesType.get(0));
			homeService.saveProperty(property);
		}
	}
	@Ignore
	private static void deleteProperty() {
		for(PROPERTY prop : PROPERTY.values()) {
			homeService.deleteProperty(prop.getName());
		}
	}

	@Ignore
	private static void makeDomain() {
		for(DOMAIN d : DOMAIN.values()) {
			Domain domain = new Domain(d.toString());
			homeService.saveDomain(domain);
		}
	}
	@Ignore
	private static void deleteDomain() {
		for(DOMAIN d : DOMAIN.values()) {
			homeService.deleteDomain(d.toString());
		}
	}

	@Ignore
	private static Item makeItem() {
		List<Domain> domain = homeService.getDomain(DOMAIN.MUSIC.toString());
		List<User> user = homeService.getUser("admin");
		Item item = new Item("test", user.get(0), domain.get(0));

		return item;
	}



	@BeforeClass
	public static void setUp() throws Exception {

		String root = "webapp/WEB-INF/";
        String[] paths = {root+"hibernate_mysql.xml", root+"dao.xml", root+"service.xml"};
        ctx = new FileSystemXmlApplicationContext(paths);
        if (ctx==null) log.debug("CONTEXT IS NULL");

        userManager = (UserManager) ctx.getBean("userManager");
        homeService = (HomeService) ctx.getBean("homeService");

        makeUser();
		makePropertyType();
		makeProperty();
		makeDomain();
	}

	@After
	public void tearDown() throws Exception {
		//deleteUser();
		log.debug("User deleted.");
		deleteDomain();
		log.debug("Domain deleted.");
		deleteProperty();
		log.debug("Property deleted.");
		deletePropertyType();
		log.debug("Property type deleted.");

		userManager = null;
		homeService = null;
	}

	@Test
	public void createItem() {
		Item item = makeItem();
		item = homeService.saveItem(item);
		log.debug("item created. id = "+item.getId());
		Item item2 = homeService.getItem(item.getId());
		log.debug("item retrieved with id "+item2.getId()+": "+item2.getName()+" ("+item2.getCreationDate()+")");

		homeService.deleteItem(item2.getId());
		log.debug("item deleted.");
	}
}
