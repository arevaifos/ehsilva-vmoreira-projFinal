package pt.uc.dei.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the Activity database table.
 * 
 */
@Entity
@NamedQueries({@NamedQuery(name="Activity.findAll", query="SELECT a FROM Activity a"),
	@NamedQuery(name="Activity.findListByIdProject", query="SELECT a FROM Activity a WHERE a.project.idProject=:idProject"),
	@NamedQuery(name = "Activity.findListType", query = "SELECT a FROM Activity a WHERE a.typeActivity.idtypeActivity=:idActivity"),
	@NamedQuery(name="Activity.findListUser", query="SELECT a FROM Activity a JOIN a.allocations l where l.user.email=:email"),
	@NamedQuery(name="Activity.findValidPrecedentesNew", query="SELECT a FROM Activity a where (a.endDateact <=:endDate) AND (a.project.idProject=:idProject)"),
	@NamedQuery(name="Activity.findValidPrecedentes", query="SELECT a FROM Activity a WHERE a.endDateact <= (SELECT b.endDateact FROM Activity b WHERE b.idActivity = :idActivity) AND a.idActivity <> :idActivity and a.project.idProject=:idProject"),
	@NamedQuery(name="Activity.findByIdActivityPrecedentes", query="SELECT distinct pr from Activity a join a.activities1 pr WHERE a.idActivity=:idActivity "),
	})

public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idActivity;

	@Temporal(TemporalType.TIMESTAMP)
	private Date beginDateact;

	private String descriptionActivity;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDateact;

	private String nameActivity;

	private int timeLeft;

	private int timePlan;

	private int timeSpend;

	//bi-directional many-to-many association to Activity
	@ManyToMany
	@JoinTable(
		name="Activity_precedence"
		, joinColumns={
			@JoinColumn(name="idActivity_precedence")
			}
		, inverseJoinColumns={
			@JoinColumn(name="idActivity_main")
			}
		)
	private List<Activity> activities1;

	//bi-directional many-to-many association to Activity
	@ManyToMany(mappedBy="activities1")
	private List<Activity> activities2;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="activity")
	private List<Allocation> allocations;

	//bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name="projects_idProject")
	private Project project;

	//bi-directional many-to-one association to State_activity
	@ManyToOne
	@JoinColumn(name="state_activity_idstateActi")
	private State_activity stateActivity;

	//bi-directional many-to-one association to Type_activity
	@ManyToOne
	@JoinColumn(name="type_activity_idtypeActivity")
	private Type_activity typeActivity;

	//bi-directional many-to-one association to Activity_historic
	@OneToMany(mappedBy="activity")
	private List<Activity_historic> activityHistorics;

	public Activity() {
	}

	public int getIdActivity() {
		return this.idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public Date getBeginDateact() {
		return this.beginDateact;
	}

	public void setBeginDateact(Date beginDateact) {
		this.beginDateact = beginDateact;
	}

	public String getDescriptionActivity() {
		return this.descriptionActivity;
	}

	public void setDescriptionActivity(String descriptionActivity) {
		this.descriptionActivity = descriptionActivity;
	}

	public Date getEndDateact() {
		return this.endDateact;
	}

	public void setEndDateact(Date endDateact) {
		this.endDateact = endDateact;
	}

	public String getNameActivity() {
		return this.nameActivity;
	}

	public void setNameActivity(String nameActivity) {
		this.nameActivity = nameActivity;
	}

	public int getTimeLeft() {
		return this.timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public int getTimePlan() {
		return this.timePlan;
	}

	public void setTimePlan(int timePlan) {
		this.timePlan = timePlan;
	}

	public int getTimeSpend() {
		return this.timeSpend;
	}

	public void setTimeSpend(int timeSpend) {
		this.timeSpend = timeSpend;
	}

	public List<Activity> getActivities1() {
		return this.activities1;
	}

	public void setActivities1(List<Activity> activities1) {
		this.activities1 = activities1;
	}

	public List<Activity> getActivities2() {
		return this.activities2;
	}

	public void setActivities2(List<Activity> activities2) {
		this.activities2 = activities2;
	}


	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public State_activity getStateActivity() {
		return this.stateActivity;
	}

	public void setStateActivity(State_activity stateActivity) {
		this.stateActivity = stateActivity;
	}

	public Type_activity getTypeActivity() {
		return this.typeActivity;
	}

	public void setTypeActivity(Type_activity typeActivity) {
		this.typeActivity = typeActivity;
	}

	public List<Activity_historic> getActivityHistorics() {
		return this.activityHistorics;
	}

	public void setActivityHistorics(List<Activity_historic> activityHistorics) {
		this.activityHistorics = activityHistorics;
	}

	public Activity_historic addActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().add(activityHistoric);
		activityHistoric.setActivity(this);

		return activityHistoric;
	}

	public Activity_historic removeActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().remove(activityHistoric);
		activityHistoric.setActivity(null);

		return activityHistoric;
	}

	/**
	 * @return the allocations
	 */
	public List<Allocation> getAllocations() {
		return allocations;
	}

	/**
	 * @param allocations the allocations to set
	 */
	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Activity [idActivity=" + idActivity + ", beginDateact=" + beginDateact + ", descriptionActivity="
				+ descriptionActivity + ", endDateact=" + endDateact + ", nameActivity=" + nameActivity + ", timeLeft="
				+ timeLeft + ", timePlan=" + timePlan + ", timeSpend=" + timeSpend + ", project=" + project
				+ ", stateActivity=" + stateActivity + ", typeActivity=" + typeActivity + "]";
	}

}