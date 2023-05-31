package pt.uc.dei.helpers;

import java.util.Date;

public class ProjectWithCPISPI {

	private String projectCode;
	private Date beginDate;
	private Date endDate;
	private float budget;
	private float cpi;
	private float spi;
	private int timePlan;
	private int timeSpend;
	private int timeLeft;
	private int userGestor;
	private float ev;
	private float cv;
	private float ac;
	private float sv;

	public ProjectWithCPISPI() {
		super();

	}


	public ProjectWithCPISPI(String projectCode, Date beginDate, Date endDate, float budget, float cpi, float spi,
			int timePlan, int timeSpend, int timeLeft, int userGestor, float ev, float cv, float ac, float sv) {
		super();
		this.projectCode = projectCode;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.budget = budget;
		this.cpi = cpi;
		this.spi = spi;
		this.timePlan = timePlan;
		this.timeSpend = timeSpend;
		this.timeLeft = timeLeft;
		this.userGestor = userGestor;
		this.ev=ev;
		this.cv=cv;
		this.ac=ac;
		this.sv=sv;

	}



	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public float getBudget() {
		return budget;
	}
	public void setBudget(float budget) {
		this.budget = budget;
	}
	public float getCpi() {
		return cpi;
	}
	public void setCpi(float cpi) {
		this.cpi = cpi;
	}
	public float getSpi() {
		return spi;
	}
	public void setSpi(float spi) {
		this.spi = spi;
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
	public int getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public int getUserGestor() {
		return userGestor;
	}

	public void setUserGestor(int userGestor) {
		this.userGestor = userGestor;
	}

	

	public float getEv() {
		return ev;
	}


	public void setEv(float ev) {
		this.ev = ev;
	}


	public float getCv() {
		return cv;
	}


	public void setCv(float cv) {
		this.cv = cv;
	}


	public float getAc() {
		return ac;
	}


	public void setAc(float ac) {
		this.ac = ac;
	}


	public float getSv() {
		return sv;
	}


	public void setSv(float sv) {
		this.sv = sv;
	}


	@Override
	public String toString() {
		return "ProjectWithCPISPI [projectCode=" + projectCode + ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", budget=" + budget + ", cpi=" + cpi + ", spi=" + spi + ", timePlan=" + timePlan + ", timeSpend="
				+ timeSpend + ", timeLeft=" + timeLeft + ", userGestor=" + userGestor + ", ev=" + ev + ", cv=" + cv
				+ ", ac=" + ac + ", sv=" + sv + "]";
	}




	
	
	
}
