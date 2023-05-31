package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Projects database table.
 * 
 */
@Entity
@Table(name="Projects")

@NamedQueries({ 
	@NamedQuery(name = "Project.findAll", 	 query= "SELECT p FROM Project p"),
	@NamedQuery(name = "Project.findByCode", query= "SELECT p FROM Project p WHERE p.codigoProject=:codigoProject"),
	@NamedQuery(name = "Project.findById",   query= "SELECT p FROM Project p WHERE p.idProject=:idProject"),
	@NamedQuery(name = "Project.findProjectOpen", query = "SELECT p FROM Project p JOIN p.stateProject s WHERE s.stateproj<>'Fechado'"),
	@NamedQuery(name = "Project.findProjectByManager", query = "SELECT p FROM Project p JOIN p.stateProject s WHERE s.stateproj<>'Fechado' and p.user.iduser=:idManager"),
})

//@NamedQuery(name = "Project.findProjectOpen",   query= "SELECT p FROM Project p WHERE p.stateProject.idStateProj<>:idStateProject"
//SELECT * FROM projectmanagement.projects WHERE userGestor=3 and state_project_idStateProj=6;

public class Project implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idProject;

	@Temporal(TemporalType.TIMESTAMP)
	private Date begindate;

	private float budget;

	private String businesssector;

	private String client;

	private String codigoProject;

	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;

	private String title;

	private int typology;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="project")
	private List<Activity> activities;

	//bi-directional many-to-one association to Allocation
	@OneToMany(mappedBy="project")
	private List<Allocation> allocations;

	//bi-directional many-to-one association to State_project
	@ManyToOne
	@JoinColumn(name="state_project_idStateProj")
	private State_project stateProject;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="userGestor")
	private User user;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Project [idProject=" + idProject + ", begindate=" + begindate + ", budget=" + budget
				+ ", businesssector=" + businesssector + ", client=" + client + ", codigoProject=" + codigoProject
				+ ", description=" + description + ", enddate=" + enddate + ", title=" + title
				+ ", typology="
				+ typology + 
				", stateProject=" + stateProject.getStateproj() + 
				", user=" + user.getEmail() + "]";
	}


	public Project() {
	}

	
	public Project(Date begindate, float budget, String businesssector, String client,
			String codigoProject, String description, Date enddate, String title, int typology) {
		this.begindate = begindate;
		this.budget = budget;
		this.businesssector = businesssector;
		this.client = client;
		this.codigoProject = codigoProject;
		this.description = description;
		this.enddate = enddate;
		this.title = title;
		this.typology = typology;
		this.user = null;
		this.activities = null;
		this.allocations = null;
		this.stateProject = null;
		}


	public int getIdProject() {
		return this.idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public Date getBegindate() {
		return this.begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public float getBudget() {
		return this.budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public String getBusinesssector() {
		return this.businesssector;
	}

	public void setBusinesssector(String businesssector) {
		this.businesssector = businesssector;
	}

	public String getClient() {
		return this.client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCodigoProject() {
		return this.codigoProject;
	}

	public void setCodigoProject(String codigoProject) {
		this.codigoProject = codigoProject;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getTypology() {
		return this.typology;
	}

	public void setTypology(int typology) {
		this.typology = typology;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Activity addActivity(Activity activity) {
		getActivities().add(activity);
		activity.setProject(this);

		return activity;
	}

	public Activity removeActivity(Activity activity) {
		getActivities().remove(activity);
		activity.setProject(null);

		return activity;
	}

	public List<Allocation> getAllocations() {
		return this.allocations;
	}

	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	public Allocation addAllocation(Allocation allocation) {
		getAllocations().add(allocation);
		allocation.setProject(this);

		return allocation;
	}

	public Allocation removeAllocation(Allocation allocation) {
		getAllocations().remove(allocation);
		allocation.setProject(null);

		return allocation;
	}

	public State_project getStateProject() {
		return this.stateProject;
	}

	public void setStateProject(State_project stateProject) {
		this.stateProject = stateProject;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}




}