package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Roles database table.
 * 
 */
@Entity
@Table(name="Roles")
@NamedQueries({
	@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r"),
	@NamedQuery(name="Role.findByRole",query="SELECT r FROM Role r WHERE r.role=:role"),

	})


public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idRole;

	private String role;

	
	//bi-directional many-to-many association to Role
	@ManyToMany(mappedBy="roles")
	private List<User> users;
	
	public Role() {
	}

	public int getIdRole() {
		return this.idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

}