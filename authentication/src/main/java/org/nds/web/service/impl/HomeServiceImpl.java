package org.nds.web.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nds.hibernate.dao.DomainDAO;
import org.nds.hibernate.dao.ItemDAO;
import org.nds.hibernate.dao.ItemPropertyDAO;
import org.nds.hibernate.dao.PropertyDAO;
import org.nds.hibernate.dao.PropertyTypeDAO;
import org.nds.hibernate.dao.UserDAO;
import org.nds.model.Domain;
import org.nds.model.Item;
import org.nds.model.ItemProperty;
import org.nds.model.Property;
import org.nds.model.PropertyType;
import org.nds.model.User;
import org.nds.web.service.HomeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;

public class HomeServiceImpl implements HomeService, InitializingBean {

	protected final Log log = LogFactory.getLog(getClass());

	DomainDAO domainDAO;
	ItemDAO itemDAO;
	ItemPropertyDAO itemPropertyDAO;
	PropertyDAO propertyDAO;
	PropertyTypeDAO propertyTypeDAO;
	UserDAO userDAO;

	@Override
	public void afterPropertiesSet() throws Exception {
		// check beans
		if (domainDAO == null) throw new ApplicationContextException("Must set domainDAO bean property on " + getClass());
		if (itemDAO == null) throw new ApplicationContextException("Must set itemDAO bean property on " + getClass());
		if (itemPropertyDAO == null) throw new ApplicationContextException("Must set itemPropertyDAO bean property on " + getClass());
		if (propertyDAO == null) throw new ApplicationContextException("Must set propertyDAO bean property on " + getClass());
		if (propertyTypeDAO == null) throw new ApplicationContextException("Must set propertyTypeDAO bean property on " + getClass());
		if (userDAO == null) throw new ApplicationContextException("Must set userDAO bean property on " + getClass());

		// init data
	}

	/** Getters / setters **/

	public void setDomainDAO(DomainDAO domainDAO) {
		this.domainDAO = domainDAO;
	}

	public void setItemDAO(ItemDAO itemDAO) {
		this.itemDAO = itemDAO;
	}

	public void setItemPropertyDAO(ItemPropertyDAO itemPropertyDAO) {
		this.itemPropertyDAO = itemPropertyDAO;
	}

	public void setPropertyDAO(PropertyDAO propertyDAO) {
		this.propertyDAO = propertyDAO;
	}

	public void setPropertyTypeDAO(PropertyTypeDAO propertyTypeDAO) {
		this.propertyTypeDAO = propertyTypeDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/** End Getters / setters **/


	public User getUser(Long id) {
		return userDAO.findById(id, false);
	}

	public List<User> getUser(String login) {
		return userDAO.findByExample(new User(login, null));
	}

	public Item getItem(Long id) {
		return itemDAO.findById(id, false);
	}

	public ItemProperty getItemProperty(Long id) {
		return itemPropertyDAO.findById(id, false);
	}

	public Domain getDomain(Long id) {
		return domainDAO.findById(id, false);
	}

	public List<Domain> getDomain(String domainName) {
		return domainDAO.findByExample(new Domain(domainName));
	}

	public Property getProperty(Long id) {
		return propertyDAO.findById(id, false);
	}

	public List<Property> getProperty(String name) {
		return propertyDAO.findByExample(new Property(name, null));
	}

	public PropertyType getPropertyType(Long id) {
		return propertyTypeDAO.findById(id, false);
	}

	public List<PropertyType> getPropertyType(String propertyType) {
		return propertyTypeDAO.findByExample(new PropertyType(propertyType));
	}

	public Item saveItem(Item item) {
		return itemDAO.makePersistent(item);
	}

	public ItemProperty saveItemProperty(ItemProperty itemProperty) {
		return itemPropertyDAO.makePersistent(itemProperty);
	}

	public Domain saveDomain(Domain domain) {
		return domainDAO.makePersistent(domain);
	}

	public Property saveProperty(Property property) {
		return propertyDAO.makePersistent(property);
	}

	public PropertyType savePropertyType(PropertyType propertyType) {
		return propertyTypeDAO.makePersistent(propertyType);
	}

	public User saveUser(User user) {
		return userDAO.makePersistent(user);
	}

	public void deleteItem(Long id) {
		itemDAO.makeTransient(getItem(id));
	}

	public void deleteItemProperty(Long id) {
		itemPropertyDAO.makeTransient(getItemProperty(id));
	}

	public void deleteDomain(Long id) {
		domainDAO.makeTransient(getDomain(id));
	}

	public void deleteDomain(String name) {
		domainDAO.makeTransient(getDomain(name).get(0));
	}

	public void deleteProperty(Long id) {
		propertyDAO.makeTransient(getProperty(id));
	}

	public void deleteProperty(String name) {
		propertyDAO.makeTransient(getProperty(name).get(0));
	}

	public void deletePropertyType(Long id) {
		propertyTypeDAO.makeTransient(getPropertyType(id));
	}

	public void deletePropertyType(String propertyType) {
		propertyTypeDAO.makeTransient(getPropertyType(propertyType).get(0));
	}

	public void deleteUser(Long id) {
		userDAO.makeTransient(getUser(id));
	}

	public void deleteUser(String login) {
		userDAO.makeTransient(getUser(login).get(0));
	}
}
