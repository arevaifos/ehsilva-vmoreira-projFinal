package pt.uc.dei.backingbeans;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.InputStream;



import org.apache.log4j.Logger;
import org.omnifaces.util.Utils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import pt.uc.dei.exceptions.UserDataInvalidException;
import pt.uc.dei.implement.ActivitiesImpl;
import pt.uc.dei.implement.HolidayImpl;
import pt.uc.dei.implement.ProjectImpl;
import pt.uc.dei.implement.UserImpl;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Allocation;
import pt.uc.dei.model.Attachment;
import pt.uc.dei.model.Project;
import pt.uc.dei.model.State_activity;
import pt.uc.dei.model.Type_activity;
import pt.uc.dei.model.User;
import pt.uc.dei.model.UsersAllocations;

/**
 * @author
 *
 */

@Named("activities")
@SessionScoped
public class Activities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Activities.class);

	@Inject
	private Internationalization internationalization;

	@Inject
	private ActivitiesImpl activitiesImpl;

	@Inject
	private ProjectImpl projectImpl;

	@Inject
	private UserImpl userImpl;

	@Inject
	private HolidayImpl holidayImpl;

	private String inputNewName = "";
	private String inputNewActiDsc = "";
	private String newActivitiesTypeString = "";
	private String stateActivityString="";

	private String projectString = "";
	private String employerString = "";

	private String comment="";

	private User employer = new User();

	private Date startDate = new Date();
	private Date endDate = new Date();

	private boolean hasPrecedence = false;
	private boolean canBeChangeToFinished = false;
	private boolean hasAllFieldsToNewActivity = true;
	private boolean hasAllFieldsToNewAllocation=true;
	private boolean hasProjectToGant = false;
	private boolean secondStepToSave=false;
	private boolean firstStepToSaveEdit=false;
	private boolean firstStepToSave=false; 
	private boolean showSaveBotton=false;
	private boolean showNextBotton=false;
	private boolean  showPrecedencesArea=false;

	private List<String> allProjectsString = new ArrayList<>();
	private List<Project> allProjects = new ArrayList<Project>();
	private List<Type_activity> allActivitiesType = new ArrayList<>();
	private List<String> allActivitiesTypeString = new ArrayList<>();

	private List<String> allEmployersString = new ArrayList<>();
	private List<User> allEmployers = new ArrayList<>();

	private List<Activity> allActivitiesByProject = new ArrayList<>();
	private List<Activity> noPrecedentesActivitiesProject = new ArrayList<>();
	private List<Activity> precedentesActivitiesProject = new ArrayList<>();
	private List<Activity> beginActivity = new ArrayList<>();

	private List<UsersAllocations> usersAllocations=new ArrayList<>();

	private List<State_activity> stateActivity=new ArrayList<>();
	private List<String> stateActivityStringArray=new ArrayList<>();

	private List<Attachment> attachmentsByActivity=new ArrayList<>();
	private List <byte[]> byteFileList=new ArrayList<>();
	private List<String> nameFile=new ArrayList<>();
	private List<String> nameMimeFile=new ArrayList<>();

	private Activity newActivity = new Activity();
	private Allocation newAllocation = new Allocation();

	private float cost = 0;
	private float percentageFloat=0;
	private float rangeMaxPercentage=100;

	private int timePlan = 0;
	private int timeSpend=0;
	private int timeLeft=0;
	private int timeEffort=0;
	private int fileLimit=10;


	private Project project = new Project();

	private Type_activity type = new Type_activity();

	private State_activity state= new State_activity();

	private UploadedFile file;

	private List<String> nameDownload=new ArrayList<>();

	private byte[] byteFile;

	private List<StreamedContent>attachsList= new ArrayList<>();

	public Activities() {
		super();
	}

	/**
	 * INICIALIZAÇÃO DE ARRAYLIST COM TODOS OS USERS, OS PROJETOS E OS TIPOS DE
	 * ATIVIDADES
	 */
	@PostConstruct
	public void init() {
		refreshActivities();
		refreshEmployers();
		refreshProjects();
		refreshAllActivitiesType();
		refreshAllStateActivity();
	}

	//*************METODOS PARA CRIAR ACTIVIDADE*************

	/**
	 * MÉTODO PARA QUE CRIA UMA NOVA ATIVIDADE, UMA NOVA ALOCAÇÃO E CHAMA O
	 * MÉTODO QUE SALVA NA BASE DE DADOS.
	 */
	public void saveNewActivity() {

		if(!firstStepToSave){
			if (selectProject(projectString)) {
				int idProject = project.getIdProject();
				allActivitiesByProject = activitiesImpl.fillPrecedencesActivitiesNew(idProject,endDate);
				noPrecedentesActivitiesProject = allActivitiesByProject;
			}

			selectTypeActivity(newActivitiesTypeString);

			firstStepToSave=true;

			String message=validateFieldsToNewActivity();

			if(message!=null && !message.trim().equals("")){

				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						message, "");
				FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
			}
			usersAllocations=activitiesImpl.findAllocationByUsers(startDate, endDate);
			refreshEmployers();

		}
		else if(!secondStepToSave){
			logger.debug("Entrei no Activities saveNewActivities");

			selectEmployer(employerString);

			String message = validateFieldsToNewAllocation();

			if(message!=null && !message.trim().equals("")){
				logger.debug("MESSAGE ALLOCATION "+message);
				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						message, "");
				FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
			}
			if (hasAllFieldsToNewAllocation) {
				secondStepToSave=true;

				newActivity.setProject(project);
				newActivity.setTypeActivity(type);
				newActivity.setEndDateact(endDate);
				newActivity.setBeginDateact(startDate);
				newActivity.setNameActivity(inputNewName);
				newActivity.setDescriptionActivity(inputNewActiDsc);
				newActivity.setTimePlan(timePlan);
				newActivity.setTimeLeft(timePlan);
				newActivity.setTimeSpend(0);
				if (employer != null && employer.getIduser() != 0) {
					newAllocation.setUser(employer);
					newAllocation.setProject(project);
					newAllocation.setPercentage(percentageFloat);
					newAllocation.setCost(cost);
					newAllocation.setBegindateAllocation(startDate);
					newAllocation.setEnddateAllocation(endDate);

					logger.info("Criou actividade com alocação ");
				} else {
					logger.info("Criou actividade sem alocação");

				}
			}
			if(!hasPrecedence){
				try {
					logger.debug("NEWALLOCATION " + newAllocation);

					boolean sucess = activitiesImpl.saveNewActivityIndDB(newActivity, newAllocation);
					if (sucess) {						
						FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
								getBundle("ERROR_ACT_019"), "");
						FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
						if (!hasPrecedence) {

							cleanFieldActivities();
							newActivity=new Activity();	
							newAllocation= new Allocation();
						}
						refreshActivities();
					} else {
						FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
								getBundle("ERROR_ACT_020"), "");
						FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
					}
				} catch (UserDataInvalidException e) {
					e.printStackTrace();
					FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
							getBundle("ERROR_ACT_020"), "");
					FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
				}				
			}else{
				secondStepToSave=true;	
			}
		}else{
			for(int i=0;i<precedentesActivitiesProject.size();i++){
				logger.debug("SIZE PRECEDENTES "+ precedentesActivitiesProject.size() + "" );
				logger.debug("IIII "+ i + " : "+ precedentesActivitiesProject.get(i).getNameActivity());			
			}

			newActivity.setActivities1(precedentesActivitiesProject);
			try {
				logger.debug("NEWALLOCATION " + newAllocation);
				boolean sucess = activitiesImpl.saveNewActivityIndDB(newActivity, newAllocation);
				if (sucess) {
					FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
							getBundle("ERROR_ACT_019"), "");
					FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);

					refreshActivities();
					newActivity=new Activity();	
					newAllocation= new Allocation();
				} else {
					FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
							getBundle("ERROR_ACT_020"), "");
					FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
				}
			} catch (UserDataInvalidException e) {
				e.printStackTrace();
				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						getBundle("ERROR_ACT_020"), "");
				FacesContext.getCurrentInstance().addMessage("messageActivity1", messageFaces);
			}
			newActivity=new Activity();	
			newAllocation= new Allocation();
			cleanFieldActivities();
			refreshEmployers();
			refreshActivities();
		}
	}


	//*************METODOS USADOS EM EDITAR ATIVIDADE *************

	/**
	 * METODO PARA CARREGAR OS DADOS DA ATIVIDADE A EDITAR
	 * 
	 * @param activityToEdit
	 */
	public void editActivityLoad(Activity activityToEdit) {
		refreshAllStateActivity();
		newActivity=activitiesImpl.findActivityToMerge(activityToEdit.getIdActivity());
		inputNewName = activityToEdit.getNameActivity();
		inputNewActiDsc = activityToEdit.getDescriptionActivity();
		newActivitiesTypeString = activityToEdit.getTypeActivity().getTypeActivity();
		projectString = activityToEdit.getProject().getCodigoProject();
		newAllocation=activitiesImpl.gatherAllocation(activityToEdit.getIdActivity());			
		if(newAllocation!=null){
			employer=activitiesImpl.gatherAllocationUser(newAllocation.getUser().getIduser());
			if(employer!=null){
				employerString=employer.getEmail();
				logger.debug("EDITACTIVITYLOAD EMPLOYERSTRING 2"+ employerString);			
				cost = newAllocation.getCost();
				percentageFloat = newAllocation.getPercentage();
			}
		}
		startDate = activityToEdit.getBeginDateact();
		endDate = activityToEdit.getEndDateact();
		timePlan = activityToEdit.getTimePlan();
		timeLeft=activityToEdit.getTimeLeft();
		timeSpend=activityToEdit.getTimeSpend();
		precedentesActivitiesProject=activitiesImpl.fillPrecedencesActivitiesEdit(activityToEdit.getIdActivity());
		stateActivityString=activityToEdit.getStateActivity().getStateActi();
		canBeChangeToFinished=validateFieldsToCloseActivity();
		if(!canBeChangeToFinished){
			for(int i=0;i<stateActivityStringArray.size();i++){
				if(stateActivityStringArray.get(i).equals("Terminada")){
					stateActivityStringArray.remove(i);
				}
			}
		}

		noPrecedentesActivitiesProject=new ArrayList<>();
		if(precedentesActivitiesProject!=null && !precedentesActivitiesProject.isEmpty()){
			hasPrecedence=true;
		}else{
			hasPrecedence=false;
		}
		hasAllFieldsToNewActivity = true;
		newActivity.setIdActivity(activityToEdit.getIdActivity());

		if(precedentesActivitiesProject.size()>0){
			try {
				fillActivitiesByProjectAndEndDate(activityToEdit.getProject().getIdProject(), endDate);
				for(int i=0;i<allActivitiesByProject.size();i++){
					boolean isDifferent=true;
					for(int j=0;j<precedentesActivitiesProject.size();j++){

						if(precedentesActivitiesProject.get(j).getIdActivity()==allActivitiesByProject.get(i).getIdActivity()){
							isDifferent=false;
						}

					}
					if(isDifferent){

						noPrecedentesActivitiesProject.add(allActivitiesByProject.get(i));
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}



	/**
	 * METODO PARA SALVAR ATIVIDADE EDITADA, UMA ALOCAÇÃO E CHAMA O
	 * METODO QUE SALVA NA BASE DE DADOS.
	 * 
	 * @param activityToEdit
	 */
	public void editActivitySave() {
		newAllocation=new Allocation();
		if(!firstStepToSave){
			selectProject(projectString);

			selectTypeActivity(newActivitiesTypeString);

			selectStateActivity();

			firstStepToSave=true;

			String message=validateFieldsToNewActivity();
			if(message!=null && !message.trim().equals("")){

				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						message, "");
				FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
				logger.debug("MESSAGE ACTIVITY "+message);
			}

			usersAllocations=activitiesImpl.findAllocationByUsers(startDate, endDate);
			refreshEmployers();

		}
		else if(!secondStepToSave){
			selectEmployer(employerString);

			String message = validateFieldsToNewAllocation();


			if(message!=null && !message.trim().equals("")){
				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						message, "");
				FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
				logger.debug("MESSAGE ALLOCATION "+message);
			}


			if (hasAllFieldsToNewAllocation && canBeChangeToFinished) {
				secondStepToSave=true;
				newActivity.setProject(project);
				newActivity.setTypeActivity(type);
				newActivity.setEndDateact(endDate);
				newActivity.setBeginDateact(startDate);
				newActivity.setNameActivity(inputNewName);
				newActivity.setDescriptionActivity(inputNewActiDsc);
				newActivity.setTimeLeft(timeLeft);
				newActivity.setStateActivity(state);

				if (employer != null && employer.getIduser() != 0) {
					newAllocation = new Allocation();
					newAllocation.setUser(employer);
					newAllocation.setProject(project);
					newAllocation.setPercentage(percentageFloat);
					newAllocation.setCost(cost);
					newAllocation.setBegindateAllocation(startDate);
					newAllocation.setEnddateAllocation(endDate);

					logger.debug("Criou actividade com alocação ");
				} else {
					logger.debug("Criou actividade sem alocação");
				}
			}
			if(!hasPrecedence){
				try {

					boolean sucess = activitiesImpl.saveEditActivityIndDB(newActivity, newAllocation);
					if (sucess) {
						FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
								getBundle("ERROR_ACT_021"), "");
						FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
						if (!hasPrecedence) {
							cleanFieldActivities();
						}
						refreshActivities();
					} else {
						FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
								getBundle("ERROR_ACT_022"), "");
						FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
					}
				} catch (UserDataInvalidException e) {
					e.printStackTrace();
					FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
							getBundle("ERROR_ACT_022"), "");
					FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
				}				
			}else{
				secondStepToSave=true;	
			}
		}else{
			try {
				logger.debug("NEWALLOCATION " + newAllocation);
				newActivity.setActivities1(precedentesActivitiesProject);
				boolean sucess = activitiesImpl.saveEditActivityIndDB(newActivity, newAllocation);
				if (sucess) {
					FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
							getBundle("ERROR_ACT_021"), "");
					FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);

					refreshActivities();
				} else {
					FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
							getBundle("ERROR_ACT_022"), "");
					FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
				}
			} catch (UserDataInvalidException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						getBundle("ERROR_ACT_022"), "");
				FacesContext.getCurrentInstance().addMessage("mensagensEdit", messageFaces);
			}
			cleanFieldActivities();
		}
	}


	private boolean validateFieldsToCloseActivity() {
		logger.debug("validateFieldsToCloseActivity "+precedentesActivitiesProject.size());
		boolean closed=true;
		int cont=0;
		if(precedentesActivitiesProject.size()>0){
			for(int i=0;i<precedentesActivitiesProject.size();i++){
				boolean allPrecedentesAreClosed=false;
				if(precedentesActivitiesProject.get(i).getStateActivity().getStateActi().equals("Terminada")||
						precedentesActivitiesProject.get(i).getStateActivity().getStateActi().equals("Bloqueada")){
					allPrecedentesAreClosed=true;
				}
				if(allPrecedentesAreClosed){
					cont++;
				}
			}
			if(precedentesActivitiesProject.size()==cont){
				closed=true;
			}else{
				closed=false;
			}
		}
		return closed;
	}


	//************* METODOS PARA VISUALIZAR TAREFA *************

	/**
	 * METODO PARA CARREGAR OS DADOS DA ATIVIDADE A VER
	 * 
	 * @param activityToEdit
	 */
	public void viewActivityLoad(Activity activityToEdit) {
		attachmentsByActivity=new ArrayList<>();
		byteFileList=new ArrayList<>();
		attachsList=new ArrayList<>();

		refreshAllStateActivity();
		logger.debug("id activity " + activityToEdit.getIdActivity());
		this.inputNewName = activityToEdit.getNameActivity();
		inputNewActiDsc = activityToEdit.getDescriptionActivity();
		newActivitiesTypeString = activityToEdit.getTypeActivity().getTypeActivity();
		projectString = activityToEdit.getProject().getCodigoProject();
		newAllocation=activitiesImpl.gatherAllocation(activityToEdit.getIdActivity());
		if(newAllocation!=null){
			employer=activitiesImpl.gatherAllocationUser(newAllocation.getUser().getIduser());
			if(employer!=null){
				employerString=employer.getEmail();
				cost = newAllocation.getCost();
				percentageFloat = newAllocation.getPercentage();
			}
		}
		startDate = activityToEdit.getBeginDateact();
		endDate = activityToEdit.getEndDateact();
		timePlan = activityToEdit.getTimePlan();
		precedentesActivitiesProject=activitiesImpl.fillPrecedencesActivitiesEdit(activityToEdit.getIdActivity());
		if(precedentesActivitiesProject!=null && !precedentesActivitiesProject.isEmpty()){
			hasPrecedence=true;
		}else{
			hasPrecedence=false;
		}
		hasAllFieldsToNewActivity = true;

		attachmentsByActivity=activitiesImpl.fillAllAttachs(activityToEdit.getIdActivity());
		for(int i=0; i<attachmentsByActivity.size();i++){			
			StreamedContent newFile;
			InputStream stream = new ByteArrayInputStream(attachmentsByActivity.get(i).getAttachmentFile());
			newFile = new DefaultStreamedContent(stream, attachmentsByActivity.get(i).getMimefile(),attachmentsByActivity.get(i).getNamefile());
			attachsList.add(newFile);	
		}

	}



	//************* METODOS USADOS EM CRIAR E EDITAR *************

	/**
	 * METODO QUE GERE O EVENTO DE SELEÇÃO DO UTILIZADOR QUE VAI SER ALOCADO E PREENCHE O SPINER COM O VALOR MAXIMO
	 * 
	 */
	public void onUserChange() {
		if(employerString!=null && employerString.trim().length() > 0){
			for(int i=0; i<usersAllocations.size();i++){
				if(usersAllocations.get(i).getEmail().equals(employerString)){
					float percentageFloat=usersAllocations.get(i).getPercentage().floatValue();
					rangeMaxPercentage=100-percentageFloat;  
				}
			}
			logger.debug("onUSERCHANGE "+ rangeMaxPercentage);
		}
	}


	/**
	 * MÉTODO QUE CHAMA O MÉTODO QUE SALVA O ARRAY DE PRECEDENCIAS NA BD E LIMPA
	 * OS CAMPOS
	 * 
	 * @return
	 */
	public String savePrecedences() {
		try {

			activitiesImpl.savePrecedentes(newActivity);
			logger.debug("ADICIONADA A PRECEDENTE: " + newActivity.getNameActivity());
			return "Precedencia(s) adicionada(s)!";
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return "Precedencia(s) não adicionada(s)!";
		}

	}

	/**
	 * ADICIONA PRECEDENCIA AO ARRAY DE PRECEDENCIAS
	 * 
	 * @param activity
	 */
	public void addPrecedences(Activity activity) {
		Activity newActiv=activitiesImpl.findActivityToMerge(activity.getIdActivity());
		
		for(int i=0; i<noPrecedentesActivitiesProject.size();i++){
			if(noPrecedentesActivitiesProject.get(i).getIdActivity()==newActiv.getIdActivity()){
				noPrecedentesActivitiesProject.remove(i);
			}
		}
		
		boolean iAmHere=false;
		for(int i=0; i<precedentesActivitiesProject.size();i++){
			if(precedentesActivitiesProject.get(i).getIdActivity()==newActiv.getIdActivity()){
				iAmHere=true;
			}
		}
		if(!iAmHere){
			precedentesActivitiesProject.add(newActiv);
		}
		
		logger.debug("ADICIONADA A PRECEDENTE: " + activity.getNameActivity());
	}

	/**
	 * ELIMINA PRECEDENCIA DO ARRAY DE PRECEDENCIAS
	 * 
	 * @param activity
	 */
	public void removePrecedences(Activity activity) {
		
		Activity newActiv=activitiesImpl.findActivityToMerge(activity.getIdActivity());
		
		for(int i=0; i<precedentesActivitiesProject.size();i++){
			if(precedentesActivitiesProject.get(i).getIdActivity()==newActiv.getIdActivity()){
				precedentesActivitiesProject.remove(i);
			}
		}
		
		boolean iAmHere=false;
		for(int i=0; i<noPrecedentesActivitiesProject.size();i++){
			if(noPrecedentesActivitiesProject.get(i).getIdActivity()==newActiv.getIdActivity()){
				iAmHere=true;
			}
		}
		if(!iAmHere){
			noPrecedentesActivitiesProject.add(newActiv);
		}
		logger.debug("REMOVIDA A PRECEDENTE: " + activity.getNameActivity());
	}

	/**
	 * MÉTODO QUE LIMPA OS CAMPOS DO FORMULÁRIO DE CRIAR NOVA ATIVIDADE
	 */
	private void cleanFieldActivities() {
		inputNewName = "";
		inputNewActiDsc = "";
		newActivitiesTypeString = "";
		projectString = "";
		employerString = "";
		employer = new User();
		startDate = new Date();
		endDate = new Date();
		hasPrecedence = false;
		hasAllFieldsToNewActivity = true;
		allActivitiesByProject=new ArrayList<>();
		noPrecedentesActivitiesProject=new ArrayList<>();
		precedentesActivitiesProject=new ArrayList<>();
		cost = 0;
		percentageFloat = 0;
		timePlan = 0;
		project = new Project();
		type = new Type_activity();
		firstStepToSave=false;
		secondStepToSave=false;
		showNextBotton=false;
		showPrecedencesArea=false;
		showSaveBotton=false;
	}


	/**
	 * OBTEM ATIVIDADES POR ID DE PROJETO
	 * 
	 * @param idProject
	 */
	public void fillViewActivitiesByProject(int idProject) {
		logger.debug("fillViewActivitiesByProject "+idProject);
		allActivitiesByProject = activitiesImpl.fillactivities(idProject);
	}

	/**
	 * OBTEM ATIVIDADES POR ID DE PROJETO e ENDDATE
	 * 
	 * @param idProject
	 * @param endDate
	 */
	public void fillActivitiesByProjectAndEndDate(int idProject, Date endDate) {
		allActivitiesByProject=new ArrayList<>();
		try {
			allActivitiesByProject = activitiesImpl.fillPrecedencesActivitiesNew(idProject, endDate);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * SELECIONA O PROJETO DE ACORDO COM O CODIGO
	 * 
	 * @param code
	 * @return
	 */
	private boolean selectProject(String code) {
		for (int i = 0; i < allProjects.size(); i++) {
			if (allProjects.get(i).getCodigoProject().equals(code)) {
				project = allProjects.get(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * SELECIONA O TIPO DE ATIVIDADE DE ACORDO COM O VALOR DA STRING
	 * 
	 * @param value
	 * @return
	 */
	private boolean selectTypeActivity(String value) {
		for (int i = 0; i < allActivitiesType.size(); i++) {
			if (allActivitiesType.get(i).getTypeActivity().equals(value)) {
				type = allActivitiesType.get(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * SELECIONA O UTILIZADOR PELO EMAIL
	 * 
	 * @param valueSelected
	 * @return
	 */
	private boolean selectEmployer(String valueSelected) {
		for (int i = 0; i < allEmployers.size(); i++) {
			if (allEmployers.get(i).getEmail().equals(valueSelected)) {
				employer = allEmployers.get(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * VALIDA OS CAMPOS DO FORMULARIO DA NOVA ATIVIDADE
	 * 
	 * @return
	 */
	private String validateFieldsToNewActivity() {

		hasAllFieldsToNewActivity = true;

		if (project == null || project.getIdProject() == 0 || internationalization.getResourceBundle().getString("Proj_selecionar").equals(projectString)) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;	
			return getBundle("ERROR_ACT_007");
		}
		if (type == null || type.getIdtypeActivity() == 0 || internationalization.getResourceBundle().getString("Act_typ2").equals(newActivitiesTypeString)) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_016");
		}
		if (endDate == null) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_009");
		}
		if (startDate == null) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_008");
		}
		if (!holidayImpl.dayIsvalid(startDate)) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_010");
		}
		if (!holidayImpl.dayIsvalid(endDate)) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_011");
		}
		if (endDate.before(startDate)) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_012");
		}
		if (inputNewName == null || inputNewName.equals("")) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_013");
		}
		if (inputNewActiDsc == null || inputNewActiDsc.equals("")) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_014");
		}
		if (timePlan < 0) {
			hasAllFieldsToNewActivity = false;
			firstStepToSave=false;
			return getBundle("ERROR_ACT_015");
		}

		return "";
	}





	/**
	 * VALIDA OS CAMPOS DO FORMULARIO DA NOVA ALOCAÇÃO
	 * 
	 * @return
	 */
	private String validateFieldsToNewAllocation() {
		hasAllFieldsToNewAllocation = true;
		if (employer.getEmail() != null || employer.getIduser() != 0) {
			if (cost<=0f) {
				hasAllFieldsToNewAllocation = false;
				secondStepToSave=false;
				return getBundle("ERROR_ACT_017");
			} 
			if (percentageFloat <= 0f) {
//			if (percentageFloat <= 0f || percentageFloat>rangeMaxPercentage) {
				hasAllFieldsToNewAllocation = false;
				secondStepToSave=false;
				return getBundle("ERROR_ACT_018");
			}
		}
		return "";
	}

	private void refreshAllActivitiesType() {
		allActivitiesTypeString=new ArrayList<>();
		allActivitiesType = activitiesImpl.fillTypeActivitys();
		for (int i = 0; i < allActivitiesType.size(); i++) {
			allActivitiesTypeString.add(allActivitiesType.get(i).getTypeActivity());
		}
	}

	private void refreshProjects() {
		allProjectsString=new ArrayList<>();
		if(userImpl.isDirectorUser()){
			allProjects = projectImpl.fillProject();
		}else{
			allProjects=projectImpl.getAllProjectByManage(userImpl.getLoggedUser().getIduser());
		}
		if(allProjects!=null){
			for (int i = 0; i < allProjects.size(); i++) {
				allProjectsString.add(allProjects.get(i).getCodigoProject());
			}
		}
	}

	private void refreshEmployers() {
		allEmployersString=new ArrayList<>();
		allEmployers=activitiesImpl.fillEmployers();
		usersAllocations=activitiesImpl.findAllocationByUsers(startDate, endDate);
		for (int i = 0; i < usersAllocations.size(); i++) {
			allEmployersString.add(usersAllocations.get(i).getEmail());
		}
	}

	private void refreshActivities() {
		beginActivity=new ArrayList<>();
		beginActivity = activitiesImpl.fillBeginActivity();
	}

	private void refreshAllStateActivity() {
		stateActivityStringArray=new ArrayList<>();
		stateActivity=new ArrayList<>();
		stateActivity=activitiesImpl.fillStatesActivity();
		for(int i=0; i<stateActivity.size();i++){
			stateActivityStringArray.add(stateActivity.get(i).getStateActi());
		}

	}

	private boolean selectStateActivity() {
		for (int i = 0; i < stateActivity.size(); i++) {
			if (stateActivity.get(i).getStateActi().equals(stateActivityString)) {
				state = stateActivity.get(i);
				return true;
			}
		}
		return false;
	}

	// *****************REPORTAR ESFORÇO*****************************************


	/**
	 * METODO PARA CARREGAR DADOS PARA A POPUP DE REPORTAR ESFORÇO
	 * 
	 * @param activity
	 */
	public void loadDataForReport(Activity activity){
		cleanFieldActivities();
		newActivity= activity;
		projectString=newActivity.getProject().getCodigoProject();
		inputNewName=newActivity.getNameActivity();
		timeLeft=newActivity.getTimeLeft();	
	}

	/**
	 * MÉTODO PARA REPORTAR ESFORÇO
	 * 
	 * @return
	 */
	public void reportEffort(){

		int newTime=newActivity.getTimeSpend()+timeEffort;
		if(newActivity.getTimeLeft()>=timeEffort && comment!=null && !comment.equals("")){
			newActivity.setTimeSpend(newTime);
			int timeLeftNew=Math.abs(newActivity.getTimeLeft()-timeEffort);
			newActivity.setTimeLeft(timeLeftNew);
			boolean sucess=activitiesImpl.reportEffortBD(newActivity, comment, byteFileList, nameFile, nameMimeFile);

			if(sucess){
				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						getBundle("ERROR_ACT_023"), "");
				FacesContext.getCurrentInstance().addMessage("mensagens2", messageFaces);
			}else{
				FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
						getBundle("ERROR_ACT_024"), "");
				FacesContext.getCurrentInstance().addMessage("mensagens2", messageFaces);
			}

		}else{
			FacesMessage messageFaces = new FacesMessage(FacesMessage.SEVERITY_INFO,
					getBundle("ERROR_ACT_025"), "");
			FacesContext.getCurrentInstance().addMessage("mensagens2", messageFaces);
		}
		cleanFieldsReport();
	}



	private void cleanFieldsReport() {
		newActivity=new Activity();
		comment="";
		byteFileList=new ArrayList<>();  	
	}

	public void cleanAll(){
		cleanFieldActivities();
		cleanFieldsReport();
	}

	/**
	 * METODO PARA FAZER UPLOAD DOS FICHEIROS
	 * 
	 * @param event
	 */
	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		file=event.getFile();
		if (file != null) {
			try {

				byteFile = Utils.toByteArray(file.getInputstream());
				byteFileList.add(byteFile);
				nameFile.add(file.getFileName());
				nameMimeFile.add(file.getContentType());
			} catch (IOException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}

			logger.debug("byteFile UPLOAD "+byteFile.length);

		}
	}


	// ************************GANTT********************************************

	public List<Activity> fillViewActivitiesByProject() {
		hasProjectToGant = selectProject(projectString);
		List<Activity> gantActivity = activitiesImpl.fillactivities(project.getIdProject());
		return gantActivity;
	}



	public boolean returnFirst(){
		firstStepToSave=false;
		employerString=new String();
		cost=0;
		percentageFloat=0;
		return true;
	}
	
	public boolean returnFirstEdit(){
		firstStepToSave=false;
		return true;
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




	// **************GETTERS E SETTERS**********************

	public String getInputNewName() {
		return inputNewName;
	}

	public void setInputNewName(String inputNewName) {
		this.inputNewName = inputNewName;
	}

	public String getInputNewActiDsc() {
		return inputNewActiDsc;
	}

	public void setInputNewActiDsc(String inputNewActiDsc) {
		this.inputNewActiDsc = inputNewActiDsc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isHasPrecedence() {
		return hasPrecedence;
	}

	public void setHasPrecedence(boolean hasPrecedence) {
		this.hasPrecedence = hasPrecedence;
	}

	public List<String> getAllProjectsString() {
		return allProjectsString;
	}

	public void setAllProjectsString(List<String> allProjectsString) {
		this.allProjectsString = allProjectsString;
	}

	public List<Project> getAllProjects() {
		return allProjects;
	}

	public void setAllProjects(List<Project> allProjects) {
		this.allProjects = allProjects;
	}

	public List<Type_activity> getAllActivitiesType() {
		return allActivitiesType;
	}

	public void setAllActivitiesType(List<Type_activity> allActivitiesType) {
		this.allActivitiesType = allActivitiesType;
	}

	public String getEmployerString() {
		return employerString;
	}

	public void setEmployerString(String employerString) {
		this.employerString = employerString;
	}

	public User getEmployer() {
		return employer;
	}

	public void setEmployer(User employer) {
		this.employer = employer;
	}

	public List<String> getAllEmployersString() {
		return allEmployersString;
	}

	public void setAllEmployersString(List<String> allEmployersString) {
		this.allEmployersString = allEmployersString;
	}

	public List<User> getAllEmployers() {
		return allEmployers;
	}

	public void setAllEmployers(List<User> allEmployers) {
		this.allEmployers = allEmployers;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public List<Activity> getAllActivitiesByProject() {
		return allActivitiesByProject;
	}

	public void setAllActivitiesByProject(List<Activity> allActivitiesByProject) {
		this.allActivitiesByProject = allActivitiesByProject;
	}

	public int getTimePlan() {
		return timePlan;
	}

	public void setTimePlan(int timePlan) {
		this.timePlan = timePlan;
	}

	public List<String> getAllActivitiesTypeString() {
		return allActivitiesTypeString;
	}

	public void setAllActivitiesTypeString(List<String> allActivitiesTypeString) {
		this.allActivitiesTypeString = allActivitiesTypeString;
	}

	public String getNewActivitiesTypeString() {
		return newActivitiesTypeString;
	}

	public void setNewActivitiesTypeString(String newActivitiesTypeString) {
		this.newActivitiesTypeString = newActivitiesTypeString;
	}

	public String getProjectString() {
		return projectString;
	}

	public void setProjectString(String projectString) {
		this.projectString = projectString;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Type_activity getType() {
		return type;
	}

	public void setType(Type_activity type) {
		this.type = type;
	}

	public float getPercentageFloat() {
		return percentageFloat;
	}

	public void setPercentageFloat(float percentageFloat) {
		this.percentageFloat = percentageFloat;
	}

	public boolean isHasAllFieldsToNewActivity() {
		return hasAllFieldsToNewActivity;
	}

	public void setHasAllFieldsToNewActivity(boolean hasAllFieldsToNewActivity) {
		this.hasAllFieldsToNewActivity = hasAllFieldsToNewActivity;
	}

	public List<Activity> getNoPrecedentesActivitiesProject() {
		return noPrecedentesActivitiesProject;
	}

	public void setNoPrecedentesActivitiesProject(List<Activity> noPrecedentesActivitiesProject) {
		this.noPrecedentesActivitiesProject = noPrecedentesActivitiesProject;
	}

	public List<Activity> getPrecedentesActivitiesProject() {
		return precedentesActivitiesProject;
	}

	public void setPrecedentesActivitiesProject(List<Activity> precedentesActivitiesProject) {
		this.precedentesActivitiesProject = precedentesActivitiesProject;
	}

	public List<Activity> getBeginActivity() {
		return beginActivity;
	}

	public void setBeginActivity(List<Activity> beginActivity) {
		this.beginActivity = beginActivity;
	}

	public Activity getNewActivity() {
		return newActivity;
	}

	public void setNewActivity(Activity newActivity) {
		this.newActivity = newActivity;
	}

	public Allocation getNewAllocation() {
		return newAllocation;
	}

	public void setNewAllocation(Allocation newAllocation) {
		this.newAllocation = newAllocation;
	}

	public boolean isHasProjectToGant() {
		return hasProjectToGant;
	}

	public void setHasProjectToGant(boolean hasProjectToGant) {
		this.hasProjectToGant = hasProjectToGant;
	}



	public boolean isFirstStepToSave() {
		return firstStepToSave;
	}

	public void setFirstStepToSave(boolean firstStepToSave) {
		this.firstStepToSave = firstStepToSave;
	}

	public boolean isShowSaveBotton() {
		if((firstStepToSave && !hasPrecedence)||(firstStepToSaveEdit && !hasPrecedence)){
			showSaveBotton=true;
		}else{
			showSaveBotton=false;
		}
		return showSaveBotton;
	}

	public void setShowSaveBotton(boolean showSaveBotton) {
		this.showSaveBotton = showSaveBotton;
	}

	public boolean isShowNextBotton() {
		if((firstStepToSave && hasPrecedence)||(firstStepToSaveEdit && hasPrecedence)){
			showNextBotton=true;
		}else{
			showNextBotton=false;
		}

		return showNextBotton;
	}

	public void setShowNextBotton(boolean showNextBotton) {
		this.showNextBotton = showNextBotton;
	}

	public boolean isSecondStepToSave() {
		return secondStepToSave;
	}

	public void setSecondStepToSave(boolean secondStepToSave) {
		this.secondStepToSave = secondStepToSave;
	}

	public boolean isShowPrecedencesArea() {
		if(secondStepToSave && hasPrecedence){
			showPrecedencesArea=true;
		}else{
			showPrecedencesArea=false;
		}
		return showPrecedencesArea;
	}

	public void setShowPrecedencesArea(boolean showPrecedencesArea) {
		this.showPrecedencesArea = showPrecedencesArea;
	}

	public float getRangeMaxPercentage() {
		return rangeMaxPercentage;
	}

	public void setRangeMaxPercentage(float rangeMaxPercentage) {
		this.rangeMaxPercentage = rangeMaxPercentage;
	}

	public boolean isHasAllFieldsToNewAllocation() {
		return hasAllFieldsToNewAllocation;
	}

	public void setHasAllFieldsToNewAllocation(boolean hasAllFieldsToNewAllocation) {
		this.hasAllFieldsToNewAllocation = hasAllFieldsToNewAllocation;
	}

	public String getStateActivityString() {
		return stateActivityString;
	}

	public void setStateActivityString(String stateActivityString) {
		this.stateActivityString = stateActivityString;
	}

	public List<UsersAllocations> getUsersAllocations() {
		return usersAllocations;
	}

	public void setUsersAllocations(List<UsersAllocations> usersAllocations) {
		this.usersAllocations = usersAllocations;
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

	public List<State_activity> getStateActivity() {
		return stateActivity;
	}

	public void setStateActivity(List<State_activity> stateActivity) {
		this.stateActivity = stateActivity;
	}

	public List<String> getStateActivityStringArray() {
		return stateActivityStringArray;
	}

	public void setStateActivityStringArray(List<String> stateActivityStringArray) {
		this.stateActivityStringArray = stateActivityStringArray;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public State_activity getState() {
		return state;
	}

	public void setState(State_activity state) {
		this.state = state;
	}

	public int getTimeEffort() {
		return timeEffort;
	}

	public void setTimeEffort(int timeEffort) {
		this.timeEffort = timeEffort;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public byte[] getByteFile() {
		return byteFile;
	}

	public void setByteFile(byte[] byteFile) {
		this.byteFile = byteFile;
	}

	public int getFileLimit() {
		return fileLimit;
	}

	public void setFileLimit(int fileLimit) {
		this.fileLimit = fileLimit;
	}

	public List<byte[]> getByteFileList() {
		return byteFileList;
	}

	public void setByteFileList(List<byte[]> byteFileList) {
		this.byteFileList = byteFileList;
	}

	public boolean isCanBeChangeToFinished() {
		return canBeChangeToFinished;
	}

	public void setCanBeChangeToFinished(boolean canBeChangeToFinished) {
		this.canBeChangeToFinished = canBeChangeToFinished;
	}

	public boolean isFirstStepToSaveEdit() {
		if(firstStepToSave&&canBeChangeToFinished){
			firstStepToSaveEdit=true;
		}else{
			firstStepToSaveEdit=false;
		}
		return firstStepToSaveEdit;
	}

	public void setFirstStepToSaveEdit(boolean firstStepToSaveEdit) {
		this.firstStepToSaveEdit = firstStepToSaveEdit;
	}

	public List<String> getNameFile() {
		return nameFile;
	}

	public void setNameFile(List<String> nameFile) {
		this.nameFile = nameFile;
	}

	public List<String> getNameDownload() {
		return nameDownload;
	}

	public void setNameDownload(List<String> nameDownload) {
		this.nameDownload = nameDownload;
	}

	public List<Attachment> getAttachmentsByActivity() {
		return attachmentsByActivity;
	}

	public void setAttachmentsByActivity(List<Attachment> attachmentsByActivity) {
		this.attachmentsByActivity = attachmentsByActivity;
	}

	public List<String> getNameMimeFile() {
		return nameMimeFile;
	}

	public void setNameMimeFile(List<String> nameMimeFile) {
		this.nameMimeFile = nameMimeFile;
	}

	public List<StreamedContent> getAttachsList() {
		return attachsList;
	}

	public void setAttachsList(List<StreamedContent> attachsList) {
		this.attachsList = attachsList;
	}
}
