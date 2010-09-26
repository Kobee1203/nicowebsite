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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ForeignKey;

/**
 * Property authorized for an item (title, author, image, ...)
 * @author Nicolas Dos Santos
 *
 */

@Entity
public class Property implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private PropertyType type;

	private Set<ItemProperty> itemProperties;

	/* Default constructor */
	public Property() {}

	public Property(String name, PropertyType type) {
		this.name = name;
		this.type = type;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "property_type_id", nullable = false)
	@ForeignKey(name = "fk_property_property_type_id")
	public PropertyType getType() {
		return type;
	}
	public void setType(PropertyType type) {
		this.type = type;
	}

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("id")
	public Set<ItemProperty> getItemProperties() {
		return itemProperties;
	}
	public void setItemProperties(Set<ItemProperty> itemProperties) {
		this.itemProperties = itemProperties;
	}

}
