package pt.uc.dei.helpers;

import java.util.Date;

public class ReportIndicator {
	
	private Date begindate;

	private Date begindateAllocation;

	private float budget;

	private String codigoProject;

	private float cost;

	private String email;

	private Date enddate;

	private Date enddateAllocation;

	private int idAllocation;

	private int idProject;

	private float percentage;

	private int timeLeft;

	private int timePlan;

	private int timeSpend;

	private int userGestor;
	
	public ReportIndicator() {
		super();
	}
	

	public ReportIndicator( Date begindate, Date begindateAllocation, float budget,
			String codigoProject, float cost, String email, Date enddate, Date enddateAllocation, int idAllocation,
			int idProject, float percentage, int timeLeft, int timePlan, int timeSpend, int userGestor) {
		super();
	
		this.begindate = begindate;
		this.begindateAllocation = begindateAllocation;
		this.budget = budget;
		this.codigoProject = codigoProject;
		this.cost = cost;
		this.email = email;
		this.enddate = enddate;
		this.enddateAllocation = enddateAllocation;
		this.idAllocation = idAllocation;
		this.idProject = idProject;
		this.percentage = percentage;
		this.timeLeft = timeLeft;
		this.timePlan = timePlan;
		this.timeSpend = timeSpend;
		this.userGestor = userGestor;
	}




	public int getUserGestor() {
		return userGestor;
	}

	public void setUserGestor(int userGestor) {
		this.userGestor = userGestor;
	}


	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
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

	public Date getEnddateAllocation() {
		return enddateAllocation;
	}

	public void setEnddateAllocation(Date enddateAllocation) {
		this.enddateAllocation = enddateAllocation;
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

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
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


	@Override
	public String toString() {
		return "ReportIndicator [begindate=" + begindate + ", begindateAllocation=" + begindateAllocation + ", budget="
				+ budget + ", codigoProject=" + codigoProject + ", cost=" + cost + ", email=" + email + ", enddate="
				+ enddate + ", enddateAllocation=" + enddateAllocation + ", idAllocation=" + idAllocation
				+ ", idProject=" + idProject + ", percentage=" + percentage + ", timeLeft=" + timeLeft + ", timePlan="
				+ timePlan + ", timeSpend=" + timeSpend + ", userGestor=" + userGestor + "]";
	}
	
	

}
