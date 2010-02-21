package org.nds.johndog.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

/**
 * Item property. It is one of item properties with a value.
 * This property must be in the Property table
 * @author Nicolas Dos Santos
 *
 */

@Entity
public class ItemProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private byte[] value;

	private Item item;
	private Property property;

	/* Default constructor */
	public ItemProperty() {}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@Lob
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "property_id", nullable = false)
	@ForeignKey(name = "fk_item_property_property_id")
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "item_id", nullable = false)
	@ForeignKey(name = "fk_item_property_item_id")
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
}
