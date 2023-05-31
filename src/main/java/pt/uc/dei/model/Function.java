package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Function database table.
 * 
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name="Function.findAll", query="SELECT f FROM Function f"),
	@NamedQuery(name = "Function.findByDesignation", query= "SELECT f FROM Function f WHERE f.designation=:designation"),
})


public class Function implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idPost;

	private String designation;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="function")
	private List<User> users;

	public Function() {
	}

	public int getIdPost() {
		return this.idPost;
	}

	public void setIdPost(int idPost) {
		this.idPost = idPost;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setFunction(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setFunction(null);

		return user;
	}

}