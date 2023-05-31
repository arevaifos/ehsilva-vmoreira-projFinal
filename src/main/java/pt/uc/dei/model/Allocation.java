package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;

import pt.uc.dei.model.Activity_historic;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the Allocation database table.
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Allocation.findAll", query = "SELECT a FROM Allocation a"),
		@NamedQuery(name = "Allocation.findProjectUsers", query = "SELECT distinct u FROM User u JOIN u.allocations a JOIN a.project p WHERE p.idProject=:idProject"),
		@NamedQuery(name = "Allocation.findByActivityId", query = "SELECT a FROM Allocation a WHERE a.activity.idActivity=:idActivity") })

public class Allocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idAllocation;

	@Temporal(TemporalType.TIMESTAMP)
	private Date begindateAllocation;

	private float cost;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enddateAllocation;

	private float percentage;
	// bi-directional many-to-one association to Activity_historic
	@OneToMany(mappedBy = "allocation")
	private List<Activity_historic> activityHistorics;

	// bi-directional many-to-one association to Project
	@ManyToOne
	@JoinColumn(name = "projects_idProject")
	private Project project;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_idUser")
	private User user;

	// bi-directional many-to-one association to Activity
	@ManyToOne
	@JoinColumn(name = "activity_idactivity")
	private Activity activity;

	public Allocation() {
	}

	public int getIdAllocation() {
		return this.idAllocation;
	}

	public void setIdAllocation(int idAllocation) {
		this.idAllocation = idAllocation;
	}

	public Date getBegindateAllocation() {
		return this.begindateAllocation;
	}

	public void setBegindateAllocation(Date begindateAllocation) {
		this.begindateAllocation = begindateAllocation;
	}

	public float getCost() {
		return this.cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public Date getEnddateAllocation() {
		return this.enddateAllocation;
	}

	public void setEnddateAllocation(Date enddateAllocation) {
		this.enddateAllocation = enddateAllocation;
	}

	public float getPercentage() {
		return this.percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public List<Activity_historic> getActivityHistorics() {
		return this.activityHistorics;
	}

	public void setActivityHistorics(List<Activity_historic> activityHistorics) {
		this.activityHistorics = activityHistorics;
	}

	public Activity_historic addActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().add(activityHistoric);
		activityHistoric.setAllocation(this);

		return activityHistoric;
	}

	public Activity_historic removeActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().remove(activityHistoric);
		activityHistoric.setAllocation(null);

		return activityHistoric;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Allocation [idAllocation=" + idAllocation + ", begindateAllocation=" + begindateAllocation + ", cost="
				+ cost + ", enddateAllocation=" + enddateAllocation + ", percentage=" + percentage + ", project="
				+ project + ", user=" + user + ", activity=" + activity + "]";
	}

}