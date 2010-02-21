package org.nds.johndog.web.service;

import java.util.List;

import org.nds.johndog.model.Item;
import org.nds.johndog.model.ItemProperty;
import org.nds.johndog.model.Domain;
import org.nds.johndog.model.Property;
import org.nds.johndog.model.PropertyType;
import org.nds.johndog.model.User;

public interface HomeService {

	User getUser(Long id);
	List<User> getUser(String login);
	User saveUser(User user);
	void deleteUser(Long id);
	void deleteUser(String login);

	Item getItem(Long id);
	Item saveItem(Item item);
	void deleteItem(Long id);

	ItemProperty getItemProperty(Long id);
	ItemProperty saveItemProperty(ItemProperty itemProperty);
	void deleteItemProperty(Long id);

	Domain getDomain(Long id);
	List<Domain> getDomain(String domainName);
	Domain saveDomain(Domain domain);
	void deleteDomain(Long id);
	void deleteDomain(String name);

	Property getProperty(Long id);
	List<Property> getProperty(String name);
	Property saveProperty(Property property);
	void deleteProperty(Long id);
	void deleteProperty(String name);

	PropertyType getPropertyType(Long id);
	List<PropertyType> getPropertyType(String propertyType);
	PropertyType savePropertyType(PropertyType propertyType);
	void deletePropertyType(Long id);
	void deletePropertyType(String propertyType);

}
