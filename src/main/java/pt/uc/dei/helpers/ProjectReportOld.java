package pt.uc.dei.helpers;

import java.util.Date;

public class ProjectReportOld {
	
	private Date begindate;

	private float budget;

	private String client;

	private String codigoProject;

	private Date enddate;

	private int idProject;

	private int state_project_idStateProj;

	private String title;

	private int typology;

	private int userGestor;
	
	

	public ProjectReportOld() {
		super();
	}

	public ProjectReportOld(Date begindate, float budget, String client, String codigoProject, Date enddate,
			int idProject, int state_project_idStateProj, String title, int typology, int userGestor) {
		super();
		this.begindate = begindate;
		this.budget = budget;
		this.client = client;
		this.codigoProject = codigoProject;
		this.enddate = enddate;
		this.idProject = idProject;
		this.state_project_idStateProj = state_project_idStateProj;
		this.title = title;
		this.typology = typology;
		this.userGestor = userGestor;
	}

	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
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

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public int getIdProject() {
		return idProject;
	}

	public void setIdProject(int idProject) {
		this.idProject = idProject;
	}

	public int getState_project_idStateProj() {
		return state_project_idStateProj;
	}

	public void setState_project_idStateProj(int state_project_idStateProj) {
		this.state_project_idStateProj = state_project_idStateProj;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		return "ProjectReport [begindate=" + begindate + ", budget=" + budget + ", client=" + client
				+ ", codigoProject=" + codigoProject + ", enddate=" + enddate + ", idProject=" + idProject
				+ ", state_project_idStateProj=" + state_project_idStateProj + ", title=" + title + ", typology="
				+ typology + ", userGestor=" + userGestor + "]";
	}

	
	
}
