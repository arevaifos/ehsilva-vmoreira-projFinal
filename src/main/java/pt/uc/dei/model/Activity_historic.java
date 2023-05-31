package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Activity_historic database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Activity_historic.findAll", query="SELECT a FROM Activity_historic a"),
@NamedQuery(name="Activity_historic.findByIdActivity", query="SELECT a FROM Activity_historic a where a.activity.idActivity=:idActivity")
})
public class Activity_historic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idhistoricActivity;

	@Temporal(TemporalType.TIMESTAMP)
	private Date begindateactnew;

	private String descriptionnew;

	private String details;

	@Temporal(TemporalType.TIMESTAMP)
	private Date enddateactnew;

	@Temporal(TemporalType.TIMESTAMP)
	private Date historicdate;

	private String namenew;

	private int timeleftnew;

	private int timeplannew;

	private int timespendnew;

	//bi-directional many-to-one association to Activity
	@ManyToOne
	@JoinColumn(name="activity_idactivity")
	private Activity activity;

	//bi-directional many-to-one association to Allocation
	@ManyToOne
	@JoinColumn(name="allocation_idallocation")
	private Allocation allocation;

	//bi-directional many-to-one association to State_activity
	@ManyToOne
	@JoinColumn(name="state_activity_idstateacti")
	private State_activity stateActivity;

	//bi-directional many-to-one association to Type_activity
	@ManyToOne
	@JoinColumn(name="type_activity_idtypeactivity")
	private Type_activity typeActivity;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public Activity_historic() {
	}

	public int getIdhistoricActivity() {
		return this.idhistoricActivity;
	}

	public void setIdhistoricActivity(int idhistoricActivity) {
		this.idhistoricActivity = idhistoricActivity;
	}

	public Date getBegindateactnew() {
		return this.begindateactnew;
	}

	public void setBegindateactnew(Date begindateactnew) {
		this.begindateactnew = begindateactnew;
	}

	public String getDescriptionnew() {
		return this.descriptionnew;
	}

	public void setDescriptionnew(String descriptionnew) {
		this.descriptionnew = descriptionnew;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getEnddateactnew() {
		return this.enddateactnew;
	}

	public void setEnddateactnew(Date enddateactnew) {
		this.enddateactnew = enddateactnew;
	}

	public Date getHistoricdate() {
		return this.historicdate;
	}

	public void setHistoricdate(Date historicdate) {
		this.historicdate = historicdate;
	}

	public String getNamenew() {
		return this.namenew;
	}

	public void setNamenew(String namenew) {
		this.namenew = namenew;
	}

	public int getTimeleftnew() {
		return this.timeleftnew;
	}

	public void setTimeleftnew(int timeleftnew) {
		this.timeleftnew = timeleftnew;
	}

	public int getTimeplannew() {
		return this.timeplannew;
	}

	public void setTimeplannew(int timeplannew) {
		this.timeplannew = timeplannew;
	}

	public int getTimespendnew() {
		return this.timespendnew;
	}

	public void setTimespendnew(int timespendnew) {
		this.timespendnew = timespendnew;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Allocation getAllocation() {
		return this.allocation;
	}

	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
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

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Activity_historic [idhistoricActivity=" + idhistoricActivity + ", begindateactnew=" + begindateactnew
				+ ", descriptionnew=" + descriptionnew + ", details=" + details + ", enddateactnew=" + enddateactnew
				+ ", historicdate=" + historicdate + ", namenew=" + namenew + ", timeleftnew=" + timeleftnew
				+ ", timeplannew=" + timeplannew + ", timespendnew=" + timespendnew + ", activity=" + activity
				+ ", allocation=" + allocation + ", stateActivity=" + stateActivity + ", typeActivity=" + typeActivity
				+ ", user=" + user + "]";
	}

	
	
}