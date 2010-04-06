package org.nds.jam.web.jpa.bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "RIGHTS")
public class Rights implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private User user;
	private String role;

	/* Default constructor */
	public Rights() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(nullable = false, length = 45)
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object obj) {
		// Vérification de l'égalité des références
		if (this == obj) {
			return true;
		}

		// Vérification du type du paramètre
		if (obj instanceof Rights) {
			// Vérification des valeurs des attributs
			Rights other = (Rights) obj;

			return new EqualsBuilder().append(this.role, other.role).isEquals();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.role).toHashCode();
	}

	@Override
	public String toString() {
		return "Rights: [ role: " + role + "]";
	}

}
