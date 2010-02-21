package org.nds.johndog.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ForeignKey;

@Entity
public class Rights implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private User user;
	private String label;
	
	/* Default constructor */
	public Rights() {}
	
	public Rights(User user, String label) {
		this.user = user;
		this.label = label;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "user_id", nullable = false)
	@ForeignKey(name = "fk_rights_user_id")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(nullable = false, length = 45)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "Rights: [ id: "+id+", label: "+label+"]";
	}
}
