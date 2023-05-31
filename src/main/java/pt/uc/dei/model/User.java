package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Arrays;
import java.util.List;

/**
 * The persistent class for the User database table.
 * 
 */
@Entity

@NamedQueries({ @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.email=:emailLogin and u.password=:passwordLogin"),
		@NamedQuery(name = "User.findSalt", query = "SELECT u.salt FROM User u WHERE u.email=:emailLogin"),
		@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email=:emailLogin"),
		@NamedQuery(name = "User.findListRoles", query = "SELECT r FROM User u join u.roles r WHERE u.iduser=:userid"),
		@NamedQuery(name = "User.findListRole", query = "SELECT u FROM User u join u.roles r WHERE r.idRole=:idRole"),
		@NamedQuery(name = "User.findActivRole", query = "SELECT u FROM User u join u.roles r "
				+ "WHERE r.idRole=:idRole and u.isactive=true"),
		@NamedQuery(name="User.findByIdUser", query="SELECT u FROM User u  WHERE u.iduser=:idUser ")
})

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int iduser;

	private String email;

	private String idgoogle;

	private boolean isactive;

	private String name;

	private String password;

	@Lob
	private byte[] photo;

	private String salt;

	private String token;

	// bi-directional many-to-one association to Activity_historic
	@OneToMany(mappedBy = "user")
	private List<Activity_historic> activityHistorics;

	// bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy = "user")
	private List<Allocation> allocations;

	// bi-directional many-to-one association to Project
	@OneToMany(mappedBy = "user")
	private List<Project> projects;

	// bi-directional many-to-many association to User
	@ManyToMany
	@JoinTable(name = "User_roles", joinColumns = { @JoinColumn(name = "user_idUser") }, inverseJoinColumns = {
			@JoinColumn(name = "roles_idRole") })

	private List<Role> roles;

	// bi-directional many-to-one association to Function
	@ManyToOne
	private Function function;

	public User() {
	}

	public int getIduser() {
		return this.iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdgoogle() {
		return this.idgoogle;
	}

	public void setIdgoogle(String idgoogle) {
		this.idgoogle = idgoogle;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Activity_historic> getActivityHistorics() {
		return this.activityHistorics;
	}

	public void setActivityHistorics(List<Activity_historic> activityHistorics) {
		this.activityHistorics = activityHistorics;
	}

	public Activity_historic addActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().add(activityHistoric);
		activityHistoric.setUser(this);

		return activityHistoric;
	}

	public Activity_historic removeActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().remove(activityHistoric);
		activityHistoric.setUser(null);

		return activityHistoric;
	}

	public List<Allocation> getAllocations() {
		return this.allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public Allocation addAllocation(Allocation allocation) {
		getAllocations().add(allocation);
		allocation.setUser(this);

		return allocation;
	}

	public Allocation removeAllocation(Allocation allocation) {
		getAllocations().remove(allocation);
		allocation.setUser(null);

		return allocation;
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setUser(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setUser(null);

		return project;
	}


	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Function getFunction() {
		return this.function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [iduser=" + iduser + ", email=" + email + ", idgoogle=" + idgoogle + ", isactive=" + isactive
				+ ", name=" + name + ", function=" + function + "]";
	}

	
	
}