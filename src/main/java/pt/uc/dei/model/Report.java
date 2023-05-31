package pt.uc.dei.model;

import java.util.Date;

public class Report {
	
	private Date begindate;

	private Date beginDateact;

	private Date begindateAllocation;

	private float budget;

	private String client;

	private String codigoProject;

	private float cost;

	private String email;

	private Date enddate;

	private Date endDateact;

	private Date enddateAllocation;

	private int idActivity;

	private int idAllocation;

	private int idProject;

	private int iduser;

	private String name;

	private String nameActivity;

	private float percentage;

	private int state_activity_idstateActi;

	private int state_project_idStateProj;

	private int timeLeft;

	private int timePlan;

	private int timeSpend;

	private String title;

	private int type_activity_idtypeActivity;

	private int typology;

	private int userGestor;

	public Report() {
	}
	
	

	public Report(Date begindate, Date beginDateact, Date begindateAllocation, float budget, String client,
			String codigoProject, float cost, String email, Date enddate, Date endDateact, Date enddateAllocation,
			int idActivity, int idAllocation, int idProject, int iduser, String name, String nameActivity,
			float percentage, int state_activity_idstateActi, int state_project_idStateProj, int timeLeft, int timePlan,
			int timeSpend, String title, int type_activity_idtypeActivity, int typology, int userGestor) {
		super();
		this.begindate = begindate;
		this.beginDateact = beginDateact;
		this.begindateAllocation = begindateAllocation;
		this.budget = budget;
		this.client = client;
		this.codigoProject = codigoProject;
		this.cost = cost;
		this.email = email;
		this.enddate = enddate;
		this.endDateact = endDateact;
		this.enddateAllocation = enddateAllocation;
		this.idActivity = idActivity;
		this.idAllocation = idAllocation;
		this.idProject = idProject;
		this.iduser = iduser;
		this.name = name;
		this.nameActivity = nameActivity;
		this.percentage = percentage;
		this.state_activity_idstateActi = state_activity_idstateActi;
		this.state_project_idStateProj = state_project_idStateProj;
		this.timeLeft = timeLeft;
		this.timePlan = timePlan;
		this.timeSpend = timeSpend;
		this.title = title;
		this.type_activity_idtypeActivity = type_activity_idtypeActivity;
		this.typology = typology;
		this.userGestor = userGestor;
	}



	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getBeginDateact() {
		return beginDateact;
	}

	public void setBeginDateact(Date beginDateact) {
		this.beginDateact = beginDateact;
	}

	public Date getBegindateAllocation() {
		return begindateAllocation;
	}

	public void setBegindateAllocation(Date begindateAllocation) {
		this.begindateAllocation = begindateAllocation;
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCodigoProject() {
		return codigoProject;
	}

	public void setCodigoProject(String codigoProject) {
		this.codigoProject = codigoProject;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Date getEndDateact() {
		return endDateact;
	}

	public void setEndDateact(Date endDateact) {
		this.endDateact = endDateact;
	}

	public Date getEnddateAllocation() {
		return enddateAllocation;
	}

	public void setEnddateAllocation(Date enddateAllocation) {
		this.enddateAllocation = enddateAllocation;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public int getIdAllocation() {
		return idAllocation;
	}

	public void setIdAllocation(int idAllocation) {
		this.idAllocation = idAllocation;
	}

	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public int getIduser() {
		return iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameActivity() {
		return nameActivity;
	}

	public void setNameActivity(String nameActivity) {
		this.nameActivity = nameActivity;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public int getState_activity_idstateActi() {
		return state_activity_idstateActi;
	}

	public void setState_activity_idstateActi(int state_activity_idstateActi) {
		this.state_activity_idstateActi = state_activity_idstateActi;
	}

	public int getState_project_idStateProj() {
		return state_project_idStateProj;
	}

	public void setState_project_idStateProj(int state_project_idStateProj) {
		this.state_project_idStateProj = state_project_idStateProj;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public int getTimePlan() {
		return timePlan;
	}

	public void setTimePlan(int timePlan) {
		this.timePlan = timePlan;
	}

	public int getTimeSpend() {
		return timeSpend;
	}

	public void setTimeSpend(int timeSpend) {
		this.timeSpend = timeSpend;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType_activity_idtypeActivity() {
		return type_activity_idtypeActivity;
	}

	public void setType_activity_idtypeActivity(int type_activity_idtypeActivity) {
		this.type_activity_idtypeActivity = type_activity_idtypeActivity;
	}

	public int getTypology() {
		return typology;
	}

	public void setTypology(int typology) {
		this.typology = typology;
	}

	public int getUserGestor() {
		return userGestor;
	}

	public void setUserGestor(int userGestor) {
		this.userGestor = userGestor;
	}



	@Override
	public String toString() {
		return "Report [begindate=" + begindate + ", beginDateact=" + beginDateact + ", begindateAllocation="
				+ begindateAllocation + ", budget=" + budget + ", client=" + client + ", codigoProject=" + codigoProject
				+ ", cost=" + cost + ", email=" + email + ", enddate=" + enddate + ", endDateact=" + endDateact
				+ ", enddateAllocation=" + enddateAllocation + ", idActivity=" + idActivity + ", idAllocation="
				+ idAllocation + ", idProject=" + idProject + ", iduser=" + iduser + ", name=" + name
				+ ", nameActivity=" + nameActivity + ", percentage=" + percentage + ", state_activity_idstateActi="
				+ state_activity_idstateActi + ", state_project_idStateProj=" + state_project_idStateProj
				+ ", timeLeft=" + timeLeft + ", timePlan=" + timePlan + ", timeSpend=" + timeSpend + ", title=" + title
				+ ", type_activity_idtypeActivity=" + type_activity_idtypeActivity + ", typology=" + typology
				+ ", userGestor=" + userGestor + "]";
	}
	
	

	
}
