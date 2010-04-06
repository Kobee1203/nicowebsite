package org.nds.jam.web.jpa.bean;

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
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * User.
 * User information
 * 
 * @author Nicolas Dos Santos
 */

@Entity
@Table(name = "USERS")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private String username;
	private String password;
	private Boolean enabled;

	private Set<Rights> rights;

	/* Default constructor */
	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(unique = true, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	@OrderBy("role")
	public Set<Rights> getRights() {
		return rights;
	}

	public void setRights(Set<Rights> rights) {
		this.rights = rights;
	}

	@Override
	public boolean equals(Object obj) {
		// Vérification de l'égalité des références
		if (this == obj) {
			return true;
		}

		// Vérification du type du paramètre
		if (obj instanceof User) {
			// Vérification des valeurs des attributs
			User other = (User) obj;

			return new EqualsBuilder().append(this.id, other.id).append(this.username, other.username).isEquals();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.id).append(this.username).toHashCode();
	}
}
