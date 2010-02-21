package org.nds.johndog.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Proxy;

/**
 * Main element.
 * It has got a name and an item type (CD, Book, ...)
 * @author Nicolas Dos Santos
 *
 */

@Entity
//@Table(name="item")
@Proxy(lazy=false)
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String name;
	private Date creationDate = new Date();
	private Date modificationDate;

	private User user;
	private Domain domain;

	Set<ItemProperty> itemProperties;

	/* Default constructor */
	public Item() {}

	public Item(String name, User user, Domain type) {
		this.name = name;
		this.user = user;
		this.domain = type;
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

	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "user_id", nullable = false)
	@ForeignKey(name = "fk_item_user_id")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "domain_id", nullable = false)
	@ForeignKey(name = "fk_item_domain_id")
	public Domain getDomain() {
		return domain;
	}
	public void setDomain(Domain domain) {
		this.domain = domain;
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
