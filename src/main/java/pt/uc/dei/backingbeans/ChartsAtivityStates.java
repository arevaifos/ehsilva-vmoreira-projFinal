package pt.uc.dei.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.helpers.*;
import pt.uc.dei.implement.ActivitiesImpl;
import pt.uc.dei.implement.ProjectImpl;
import pt.uc.dei.implement.UserImpl;
import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Project;

/**
 * @author
 *
 */
@Named("charts")
@SessionScoped
public class ChartsAtivityStates implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(ChartsAtivityStates.class);
	
	@Inject
	private ProjectImpl projectImpl;
	
	@Inject
	private UserImpl userImpl;
	
	@Inject
	private ActivitiesImpl activitiesImpl;
	
	private String nameProject="";
	
	private List<Activity> ativityByProject=new ArrayList<>();
	private List<Gantt> ativityByProjectString=new ArrayList<>();
	
	private List<Project> allProjects=new ArrayList<>();
	private List<String> codeProjects=new ArrayList<>();
	
	private Project projectSelect;
	
	private boolean hasProjectToGant=false;

	@PostConstruct
	public void init() {
		refreshProjects();		
	}
	
	
	public List<Activity> fillViewActivitiesByProject() {
		hasProjectToGant = selectProject(nameProject);
		List<Activity> gantActivity = activitiesImpl.fillactivities(projectSelect.getIdProject());
		return gantActivity;
	}
	
	public void createGanttGraphic(){
		ativityByProjectString=new ArrayList<>();
		try {
			selectProject(nameProject);
			ativityByProject=activitiesImpl.fillactivities(projectSelect.getIdProject());
			for(int i=0;i<ativityByProject.size();i++){
				float percentage=(float)((ativityByProject.get(i).getTimeSpend()*100.0/ ativityByProject.get(i).getTimePlan()));
				logger.debug("PERCENTAGE "+percentage);
				Gantt gantt= new Gantt(ativityByProject.get(i).getNameActivity(),Float.toString(percentage));
				ativityByProjectString.add(gantt);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void refreshProjects() {
		
		if(userImpl.isDirectorUser()){
			allProjects = projectImpl.fillProject();
		}else{
			allProjects=projectImpl.getAllProjectByManage(userImpl.getLoggedUser().getIduser());
		}
		if(allProjects!=null){
			for (int i = 0; i < allProjects.size(); i++) {
				codeProjects.add(allProjects.get(i).getCodigoProject());
			}
		}
	}

	private boolean selectProject(String code) {
		for (int i = 0; i < allProjects.size(); i++) {
			if (allProjects.get(i).getCodigoProject().equals(code)) {
				projectSelect = allProjects.get(i);
				logger.debug("PROJECT SELECTED: " + projectSelect.getCodigoProject());
				return true;
			}
		}
		return false;
	}


	public String getNameProject() {
		return nameProject;
	}


	public void setNameProject(String nameProject) {
		this.nameProject = nameProject;
	}


	public List<Activity> getAtivityByProject() {
		return ativityByProject;
	}


	public void setAtivityByProject(List<Activity> ativityByProject) {
		this.ativityByProject = ativityByProject;
	}


	public List<Project> getAllProjects() {
		return allProjects;
	}


	public void setAllProjects(List<Project> allProjects) {
		this.allProjects = allProjects;
	}


	public List<String> getCodeProjects() {
		return codeProjects;
	}


	public void setCodeProjects(List<String> codeProjects) {
		this.codeProjects = codeProjects;
	}


	public Project getProjectSelect() {
		return projectSelect;
	}


	public void setProjectSelect(Project projectSelect) {
		this.projectSelect = projectSelect;
	}


	public boolean isHasProjectToGant() {
		return hasProjectToGant;
	}


	public void setHasProjectToGant(boolean hasProjectToGant) {
		this.hasProjectToGant = hasProjectToGant;
	}


	public List<Gantt> getAtivityByProjectString() {
		return ativityByProjectString;
	}


	public void setAtivityByProjectString(List<Gantt> ativityByProjectString) {
		this.ativityByProjectString = ativityByProjectString;
	}


	
	
}
