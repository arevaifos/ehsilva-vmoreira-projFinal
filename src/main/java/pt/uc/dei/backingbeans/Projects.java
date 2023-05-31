package pt.uc.dei.backingbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import pt.uc.dei.implement.ProjectImpl;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Project;
import pt.uc.dei.model.User;

@Named("projects")
@SessionScoped
public class Projects implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(Projects.class);

	@Inject
	private Internationalization internationalization;

	@Inject
	private ProjectImpl projectImpl;

	private List<Project> allProjects = new ArrayList<Project>();

	private List<Activity> projectActivities = new ArrayList<Activity>();
	private List<User> projectUsers = new ArrayList<User>();
	private List<User> allUsers = new ArrayList<User>();

	private List<String> allStateProject = new ArrayList<String>();
	private String stateDoProjeto;
	private String saveStateDoProjeto;
	private String stateDoProjeto22;

	private List<String> allTypeProject = new ArrayList<String>();
	private String tipoDeProjeto;

	private Date beginDate;
	private Date endDate;

	private Date beginDate22;
	private Date endDate22;

	private boolean choosed = false;

	private int saveProjectid;

	private String codigoProject;
	private String codigoProject22;
	private float budget;
	private String businesssector;
	private String client;
	private String client22;
	private String description;
	private String title;
	private String title22;
	private String managerName;
	private String newManagerName;
	private String managerEmail;
	private String newManagerEmail;

	private boolean flagEdit;
	private int flagSeFecha;

	private String dialogMsg;
	private String dialogMsg1;
	private String dialogMsg2;
	private String dialogMsg3;

	private boolean[] booleans = { false, false, false };

	private boolean flagProjetosUtilizador;
	private boolean flagActivities;
	private boolean seActivitiesPorFechar;

	public Projects() {
	}

	@PostConstruct
	public void init() {
		flagProjetosUtilizador = true;
		inicia();
	}

	public void init2() {
		flagProjetosUtilizador = false;
		inicia();
	}

	public void inicia() {
		limpaDados();
		flagEdit = false;
		allTypeProject.clear();
		allTypeProject.add("Custo Fixo");
		allTypeProject.add("Tempo e materiais");

		// set das datas para fazer uma pesquisa de projetos para todas as datas
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		beginDate22 = cal.getTime();
		endDate22 = cal.getTime();

		pesquisaProjects(false);

	}

	private void limpaDados() {
		this.saveProjectid = 0;
		this.codigoProject = null;
		this.tipoDeProjeto = null;
		this.beginDate = new Date();
		this.endDate = new Date();
		this.choosed = false;
		this.budget = 0;
		this.businesssector = null;
		this.client = null;
		this.description = null;
		this.title = null;
		this.managerEmail = null;
		this.managerName = null;
		this.newManagerName = null;
		this.projectActivities.clear();
		this.projectUsers.clear();
		this.flagSeFecha = 0;
		this.allStateProject = projectImpl.getAllStateProject();

	}

	public String iniciaViewWindow(Project project) {
		if (project == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("ERROR_PROJ_001"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
			logger.debug("ERROR_PROJ_001" + ": " + project + ". ");
		}

		iniciaXhtml(project);

		return "projectView.xhtml";
	}

	public String iniciaEditWindow(Project project) {
		flagEdit = true;
		if (project == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("ERROR_PROJ_001"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
			logger.debug("ERROR_PROJ_001" + ": " + project + ". ");
		}

		iniciaXhtml(project);

		return "projectEdit.xhtml";

	}

	private void iniciaXhtml(Project project) {
		this.tipoDeProjeto = "";
		if (project.getTypology() == 1) {
			tipoDeProjeto = "Custo Fixo";
		} else if (project.getTypology() == 2) {
			tipoDeProjeto = "Tempo e materiais";
		}

		this.stateDoProjeto = project.getStateProject().getStateproj();
		if (this.stateDoProjeto == null) {
			this.stateDoProjeto = "Outros";
		}
		this.saveStateDoProjeto = this.stateDoProjeto;

		this.saveProjectid = project.getIdProject();
		this.codigoProject = project.getCodigoProject();
		this.beginDate = project.getBegindate();
		this.endDate = project.getEnddate();

		this.budget = project.getBudget();
		this.businesssector = project.getBusinesssector();
		this.client = project.getClient();
		this.description = project.getDescription();
		this.title = project.getTitle();
		this.choosed = true;
		this.managerName = project.getUser().getName();
		this.managerEmail = project.getUser().getEmail();
		this.newManagerName = project.getUser().getName();
		this.newManagerEmail = project.getUser().getEmail();

		this.projectActivities.clear();
		this.projectActivities = projectImpl.getProjectActivities(project.getIdProject());

		this.projectUsers.clear();
		this.projectUsers = projectImpl.getProjectUsers(project.getIdProject());

		// if (this.projectUsers == null) {
		// return;
		// }

	}

	public void saveNewProject() {
		int typology = 0;
		if (tipoDeProjeto.equals("Custo Fixo")) {
			typology = 1;
		} else if (tipoDeProjeto.equals("Tempo e materiais")) {
			typology = 2;
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("ERROR_PROJ_003"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
			return;
		}

		Project project = new Project(beginDate, budget, businesssector, client, codigoProject, description, endDate,
				title, typology);

		String msg = projectImpl.saveNewProject(project, stateDoProjeto, managerEmail);

		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_02"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
			limpaDados();
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
		}

	}

	public void saveEditProject() {

		int typology = 0;
		if (tipoDeProjeto.equals("Custo Fixo")) {
			typology = 1;
		} else if (tipoDeProjeto.equals("Tempo e materiais")) {
			typology = 2;
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("USER_PROJ_003"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
			return;
		}
		Project newProject = new Project(beginDate, budget, businesssector, client, codigoProject, description, endDate,
				title, typology);

		if (!saveStateDoProjeto.equals("Fechado") && stateDoProjeto.equals("Fechado") && flagSeFecha == 0) {
			ifCanClose(newProject);
			dialogMsg = getBundle("INFO_PROJ_004");
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('varFechaProj').show();");
			return;
		}
		saveStateDoProjeto = stateDoProjeto;

		String msg = projectImpl.saveEditProject(saveProjectid, newProject, stateDoProjeto, newManagerEmail);
		System.out.println(msg);

		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_03"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
		}
		if (flagSeFecha > 0) {
			flagSeFecha = 0;
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('mensagens').show();");
		}

	}

	/*
	 * Recebe o new Project
	 * 
	 * @return boolean[]
	 * 
	 * boolean[0] = true => projeto com data fim > que data de fecho
	 * 
	 * boolean[1] = true => tem atividades não fechadas
	 * 
	 * boolean[2] = true => tem atividades com data fim > que data fecho
	 * 
	 */
	private void ifCanClose(Project newProject) {
		flagActivities = false;
		seActivitiesPorFechar = false;
		booleans[0] = false;
		booleans[1] = false;
		booleans[2] = false;
		dialogMsg1 = "";
		dialogMsg2 = "";
		dialogMsg3 = "";

		if (newProject.getEnddate().after(new Date())) {
			booleans[0] = true;
		}

		if (projectActivities != null) {
			for (int i = 0; i < projectActivities.size(); i++) {
				if (booleans[0]) {
					if (projectActivities.get(i).getEndDateact().after(new Date())) {
						booleans[1] = true;
					}
				} else if (projectActivities.get(i).getEndDateact().after(newProject.getEnddate())) {
					booleans[1] = true;
				}
				if (!projectActivities.get(i).getStateActivity().getStateActi().equals("Terminada")) {
					booleans[2] = true;
					seActivitiesPorFechar = true;
				}
			}
		}

		if (booleans[0] || booleans[1] || booleans[2]) {
			if (booleans[0]) {
				dialogMsg1 = getBundle("INFO_PROJ_003");
			}
			if (booleans[1]) {
				if (dialogMsg1.equals("")) {
					dialogMsg1 = getBundle("INFO_PROJ_001");
				} else {
					dialogMsg2 = getBundle("INFO_PROJ_001");
				}
			}
			if (booleans[2]) {
				if (dialogMsg1.equals("")) {
					dialogMsg1 = getBundle("INFO_PROJ_002");
				} else if (dialogMsg2.equals("")) {
					dialogMsg2 = getBundle("INFO_PROJ_002");
				} else {
					dialogMsg3 = getBundle("INFO_PROJ_002");
				}
			}
		}
	}

	public void cancelEditManager() {
		this.newManagerName = this.managerName;
		this.newManagerEmail = this.managerEmail;
	}

	public void saveEditManager() {
		this.managerName = this.newManagerName;
		this.managerEmail = this.newManagerEmail;
	}

	public void pesquisaProjects(boolean flagSeGrowl) {
		allProjects = projectImpl.pesquisaProjects(codigoProject22, title22, client22, stateDoProjeto22, beginDate22,
				endDate22, flagProjetosUtilizador);
		if (allProjects == null && flagSeGrowl) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("WARN_004"), "");
			FacesContext.getCurrentInstance().addMessage("FacesMessage.SEVERITY_INFO", message);
		}
	}

	/**
	 * set flagSeFecha = 1 -> sim coloca projeto como fechado
	 */
	public void setFlagSeFechaSim() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('varFechaProj').hide();");
		flagSeFecha = 1;
		System.out.println("flagActivities " + flagActivities);
		if (flagActivities) {
			projectImpl.finishActivities(projectActivities);
		}
		System.out.println("flagActivities " + flagActivities);
		saveEditProject();
	}

	/**
	 * não fecha
	 */
	public void setFlagSeFechaNao() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('varFechaProj').hide();");
		this.flagSeFecha = 0;
		this.stateDoProjeto = saveStateDoProjeto;
	}

	/**
	 * get message from internationalization
	 * 
	 * @param key
	 *            String
	 * @return
	 */
	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}

	public void chooseManager(String managerEmail, String managerName) {
		if (!flagEdit) {
			this.managerEmail = managerEmail;
			this.managerName = managerName;
		} else {
			this.newManagerEmail = managerEmail;
			this.newManagerName = managerName;
		}

		choosed = true;
	}

	// no edit tem que ser com a data original
	public void canceleChooseManager() {
		if (!flagEdit) {
			this.managerEmail = "";
			this.managerName = "";
		} else {
			this.newManagerEmail = managerEmail;
			this.newManagerName = managerName;
		}
		choosed = false;
	}

	public List<Project> getAllProjects() {
		return allProjects;
	}

	public void setAllProjects(List<Project> allProjects) {
		this.allProjects = allProjects;
	}

	public List<Activity> getProjectActivities() {
		return projectActivities;
	}

	public void setProjectActivities(List<Activity> projectActivities) {
		this.projectActivities = projectActivities;
	}

	public List<User> getProjectUsers() {
		return projectUsers;
	}

	public void setProjectUsers(List<User> projectUsers) {
		this.projectUsers = projectUsers;
	}

	/**
	 * @return the allStateProject
	 */
	public List<String> getAllStateProject() {
		return allStateProject;
	}

	/**
	 * @param allStateProject
	 *            the allStateProject to set
	 */
	public void setAllStateProject(List<String> allStateProject) {
		this.allStateProject = allStateProject;
	}

	/**
	 * @return the allTypeProject
	 * 
	 */
	public String getAllTypeProject(int i) {
		i--;
		if (i == 0 || i == 1) {
			return allTypeProject.get(i);
		}
		return "?";
	}

	/**
	 * @return the allTypeProject
	 */
	public List<String> getAllTypeProject() {
		return allTypeProject;
	}

	/**
	 * @param allTypeProject
	 *            the allTypeProject to set
	 */
	public void setAllTypeProject(List<String> allTypeProject) {
		this.allTypeProject = allTypeProject;
	}

	/**
	 * @return the stateDoProjeto
	 */
	public String getStateDoProjeto() {
		return stateDoProjeto;
	}

	/**
	 * @param stateDoProjeto
	 *            the stateDoProjeto to set
	 */
	public void setStateDoProjeto(String stateDoProjeto) {
		this.stateDoProjeto = stateDoProjeto;
	}

	/**
	 * @return the tipoDeProjeto
	 */
	public String getTipoDeProjeto() {
		return tipoDeProjeto;
	}

	/**
	 * @param tipoDeProjeto
	 *            the tipoDeProjeto to set
	 */
	public void setTipoDeProjeto(String tipoDeProjeto) {
		this.tipoDeProjeto = tipoDeProjeto;
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate
	 *            the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the choosed
	 */
	public boolean isChoosed() {
		return choosed;
	}

	/**
	 * @param choosed
	 *            the choosed to set
	 */
	public void setChoosed(boolean choosed) {
		this.choosed = choosed;
	}

	/**
	 * @return the codigoProject
	 */
	public String getCodigoProject() {
		return codigoProject;
	}

	/**
	 * @param codigoProject
	 *            the codigoProject to set
	 */
	public void setCodigoProject(String codigoProject) {
		this.codigoProject = codigoProject;
	}

	/**
	 * @return the budget
	 */
	public float getBudget() {
		return budget;
	}

	/**
	 * @param budget
	 *            the budget to set
	 */
	public void setBudget(float budget) {
		this.budget = budget;
	}

	/**
	 * @return the businesssector
	 */
	public String getBusinesssector() {
		return businesssector;
	}

	/**
	 * @param businesssector
	 *            the businesssector to set
	 */
	public void setBusinesssector(String businesssector) {
		this.businesssector = businesssector;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}

	/**
	 * @param managerName
	 *            the managerName to set
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/**
	 * @return the newManagerName
	 */
	public String getNewManagerName() {
		return newManagerName;
	}

	/**
	 * @param newManagerName
	 *            the newManagerName to set
	 */
	public void setNewManagerName(String newManagerName) {
		this.newManagerName = newManagerName;
	}

	/**
	 * @return the managerEmail
	 */
	public String getManagerEmail() {
		return managerEmail;
	}

	/**
	 * @param managerEmail
	 *            the managerEmail to set
	 */
	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	/**
	 * @return the newManagerEmail
	 */
	public String getNewManagerEmail() {
		return newManagerEmail;
	}

	/**
	 * @param newManagerEmail
	 *            the newManagerEmail to set
	 */
	public void setNewManagerEmail(String newManagerEmail) {
		this.newManagerEmail = newManagerEmail;
	}

	/**
	 * @return the flagEdit
	 */
	public boolean isFlagEdit() {
		return flagEdit;
	}

	/**
	 * @param flagEdit
	 *            the flagEdit to set
	 */
	public void setFlagEdit(boolean flagEdit) {
		this.flagEdit = flagEdit;
	}

	public Date getBeginDate22() {
		return beginDate22;
	}

	public void setBeginDate22(Date beginDate22) {
		this.beginDate22 = beginDate22;
	}

	public Date getEndDate22() {
		return endDate22;
	}

	public void setEndDate22(Date endDate22) {
		this.endDate22 = endDate22;
	}

	/**
	 * @return the codigoProject22
	 */
	public String getCodigoProject22() {
		return codigoProject22;
	}

	/**
	 * @param codigoProject22
	 *            the codigoProject22 to set
	 */
	public void setCodigoProject22(String codigoProject22) {
		this.codigoProject22 = codigoProject22;
	}

	/**
	 * @return the client22
	 */
	public String getClient22() {
		return client22;
	}

	/**
	 * @param client22
	 *            the client22 to set
	 */
	public void setClient22(String client22) {
		this.client22 = client22;
	}

	/**
	 * @return the title22
	 */
	public String getTitle22() {
		return title22;
	}

	/**
	 * @param title22
	 *            the title22 to set
	 */
	public void setTitle22(String title22) {
		this.title22 = title22;
	}

	public String getStateDoProjeto22() {
		return stateDoProjeto22;
	}

	public void setStateDoProjeto22(String stateDoProjeto22) {
		this.stateDoProjeto22 = stateDoProjeto22;
	}

	/**
	 * @return the dialogMsg
	 */
	public String getDialogMsg() {
		return dialogMsg;
	}

	/**
	 * @param dialogMsg
	 *            the dialogMsg to set
	 */
	public void setDialogMsg(String dialogMsg) {
		this.dialogMsg = dialogMsg;
	}

	/**
	 * @return the dialogMsg1
	 */
	public String getDialogMsg1() {
		return dialogMsg1;
	}

	/**
	 * @param dialogMsg1
	 *            the dialogMsg1 to set
	 */
	public void setDialogMsg1(String dialogMsg1) {
		this.dialogMsg1 = dialogMsg1;
	}

	/**
	 * @return the dialogMsg2
	 */
	public String getDialogMsg2() {
		return dialogMsg2;
	}

	/**
	 * @param dialogMsg2
	 *            the dialogMsg2 to set
	 */
	public void setDialogMsg2(String dialogMsg2) {
		this.dialogMsg2 = dialogMsg2;
	}

	/**
	 * @return the dialogMsg3
	 */
	public String getDialogMsg3() {
		return dialogMsg3;
	}

	/**
	 * @param dialogMsg3
	 *            the dialogMsg3 to set
	 */
	public void setDialogMsg3(String dialogMsg3) {
		this.dialogMsg3 = dialogMsg3;
	}

	/**
	 * @return the flagActivities
	 */
	public boolean isFlagActivities() {
		return flagActivities;
	}

	/**
	 * @param flagActivities
	 *            the flagActivities to set
	 */
	public void setFlagActivities(boolean flagActivities) {
		this.flagActivities = flagActivities;
	}

	/**
	 * @return the booleans
	 */
	public boolean getBooleans(int i) {
		return booleans[i];
	}

	/**
	 * @return the seActivitiesPorFechar
	 */
	public boolean isseActivitiesPorFechar() {
		return seActivitiesPorFechar;
	}

	/**
	 * @param seActivitiesPorFechar
	 *            the seActivitiesPorFechar to set
	 */
	public void setseActivitiesPorFechar(boolean seActivitiesPorFechar) {
		this.seActivitiesPorFechar = seActivitiesPorFechar;
	}

	/**
	 * @return the allUsers
	 */
	public List<User> getAllUsers() {
		System.out.println("estou no get");
		return projectImpl.getUtilizadores();
	}

	/**
	 * @param allUsers
	 *            the allUsers to set
	 */
	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

}