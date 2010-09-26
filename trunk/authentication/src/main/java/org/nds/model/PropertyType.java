package org.nds.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

/**
 * Property type (STRING, INTEGER, BOOLEAN, LOB, ...)
 * @author Nicolas Dos Santos
 *
 */

@Entity
public class PropertyType implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;

	private Set<Property> properties;

	/* Default constructor */
	public PropertyType() {}

	public PropertyType(String name) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("name")
	public Set<Property> getProperties() {
		return properties;
	}
	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}
}
