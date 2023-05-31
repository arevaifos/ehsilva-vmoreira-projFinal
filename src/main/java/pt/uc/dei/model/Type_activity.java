package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;

import pt.uc.dei.model.Activity_historic;

import java.util.List;


/**
 * The persistent class for the Type_activity database table.
 * 
 */
@Entity
@NamedQuery(name="Type_activity.findAll", query="SELECT t FROM Type_activity t")
public class Type_activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idtypeActivity;

	private String typeActivity;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="typeActivity")
	private List<Activity> activities;

	//bi-directional many-to-one association to Activity_historic
	@OneToMany(mappedBy="typeActivity")
	private List<Activity_historic> activityHistorics;

	public Type_activity() {
	}

	public int getIdtypeActivity() {
		return this.idtypeActivity;
	}

	public void setIdtypeActivity(int idtypeActivity) {
		this.idtypeActivity = idtypeActivity;
	}

	public String getTypeActivity() {
		return this.typeActivity;
	}

	public void setTypeActivity(String typeActivity) {
		this.typeActivity = typeActivity;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public Activity addActivity(Activity activity) {
		getActivities().add(activity);
		activity.setTypeActivity(this);

		return activity;
	}

	public Activity removeActivity(Activity activity) {
		getActivities().remove(activity);
		activity.setTypeActivity(null);

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
		activityHistoric.setTypeActivity(this);

		return activityHistoric;
	}

	public Activity_historic removeActivityHistoric(Activity_historic activityHistoric) {
		getActivityHistorics().remove(activityHistoric);
		activityHistoric.setTypeActivity(null);

		return activityHistoric;
	}

	
	@Override
	public String toString() {
		return "Type_activity [idtypeActivity=" + idtypeActivity + ", typeActivity=" + typeActivity +"]";
	}
	
	

}