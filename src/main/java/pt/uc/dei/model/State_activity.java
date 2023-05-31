package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;

import pt.uc.dei.model.Activity_historic;

import java.util.List;


/**
 * The persistent class for the State_activity database table.
 * 
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name="State_activity.findAll", query="SELECT s FROM State_activity s"),
	@NamedQuery(name = "State_activity.findByState", query= "SELECT s FROM State_activity s WHERE s.stateActi=:stateActi"),
})

public class State_activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idstateActi;

	private String stateActi;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="stateActivity")
	private List<Activity> activities;

	//bi-directional many-to-one association to Activity_historic
	@OneToMany(mappedBy="stateActivity")
	private List<Activity_historic> activityHistorics;

	public State_activity() {
	}

	public int getIdstateActi() {
		return this.idstateActi;
	}

	public void setIdstateActi(int idstateActi) {
		this.idstateActi = idstateActi;
	}

	public String getStateActi() {
		return this.stateActi;
	}

	public void setStateActi(String stateActi) {
		this.stateActi = stateActi;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Activity addActivity(Activity activity) {
		getActivities().add(activity);
		activity.setStateActivity(this);

		return activity;
	}

	public Activity removeActivity(Activity activity) {
		getActivities().remove(activity);
		activity.setStateActivity(null);

		return activity;
	}


	public List<Activity_historic> getActivityHistorics() {
		return this.activityHistorics;
	}

	public void setActivityHistorics(List<Activity_historic> activityHistorics) {
		this.activityHistorics = activityHistorics;
	}

	public Activity_historic addActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().add(activityHistoric);
		activityHistoric.setStateActivity(this);

		return activityHistoric;
	}

	public Activity_historic removeActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().remove(activityHistoric);
		activityHistoric.setStateActivity(null);

		return activityHistoric;
	}

	
	
}