package pt.uc.dei.implement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.dao.ActivityDao;
import pt.uc.dei.dao.AllocationDao;
import pt.uc.dei.dao.ProjectDao;
import pt.uc.dei.dao.StateProjectDao;
import pt.uc.dei.dao.State_activityDao;
import pt.uc.dei.dao.UserDao;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Project;
import pt.uc.dei.model.Role;
import pt.uc.dei.model.State_activity;
import pt.uc.dei.model.State_project;
import pt.uc.dei.model.User;

@Named("projectImpl")
public class ProjectImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ProjectImpl.class);

	@Inject
	private Internationalization internationalization;

	@Inject
	private State_activityDao state_activityDao;

	@Inject
	private ProjectDao projectDao;

	@Inject
	private ActivityDao activityDao;

	@Inject
	private UserDao userDao;

	@Inject
	private State_project state_project;

	@Inject
	private StateProjectDao stateProjectDao;

	@Inject
	private UserImpl userImpl;

	@Inject
	private AllocationDao allocationDao;

	@Inject
	private HolidayImpl holidayImpl;

	public ProjectImpl() {

	}

	public String saveNewProject(Project project, String statDoProjeto, String managerEmail) {

		String messa = validaUserLogado(project, false);
		if (messa != null) {
			return messa;
		}

		messa = validateVar(project);
		if (messa != null) {
			return messa;
		}

		if (projectDao.findByCode(project.getCodigoProject()) != null) {
			return getBundle("USER_PROJ_004");
		}

		User user = null;
		if (managerEmail != null) {
			user = userDao.findByEmail(managerEmail);
		}
		if (user == null) {
			return getBundle("USER_PROJ_005");
		}

		State_project stateProj = stateProjectDao.findByState("Planeado");
		if (stateProj == null) {
			logger.error(getBundle("USER_PROJ_006"));
			return getBundle("USER_PROJ_007");

		}

		project.setStateProject(stateProj);
		project.setUser(user);

		try {
			projectDao.persist(project);
			projectDao.flush();
			logger.info(getBundle("INFO_02") + " " + userImpl.getLoggedUser().getEmail() + " " + " " + project);
		} catch (Exception e) {
			logger.error(getBundle("ERROR_PROJ_002"));
			e.getStackTrace();
			logger.error("  ");
			return getBundle("ERROR_PROJ_002");
		}

		return null;
	}

	private String validaUserLogado(Project project, boolean flagEdit) {

		if (userImpl.getLoggedUser() != null) {
			if (userImpl.getLoggedUser().getIsactive()) {

				List<Role> roles = userDao.findListRoles(userImpl.getLoggedUser().getIduser());
				if (roles == null || roles.size() == 0) {
					logger.error(getBundle("ERROR_PROJ_008") + " " + userImpl.getLoggedUser().getEmail() + " "
							+ project.getCodigoProject() + " " + project.getTitle() + " ");
					return getBundle("ERROR_PROJ_008");
				}
				boolean flag = false;
				for (int i = 0; i < roles.size() && !flag; i++) {
					if (roles.get(i).getRole().equals("Diretor")) {
						flag = true;
					} else if (roles.get(i).getRole().equals("Manager") && flagEdit) {
						flag = true;
					}
				}
				if (!flag) {
					logger.error(getBundle("ERROR_PROJ_008") + " " + userImpl.getLoggedUser().getEmail() + " "
							+ project.getCodigoProject() + " " + project.getTitle() + " ");
					return getBundle("ERROR_PROJ_008");
				}
			}
		}

		return null;
	}

	public String saveEditProject(int saveProjId, Project newProject, String statDoProjeto, String newManagerEmail) {
		String messa = validaUserLogado(newProject, true);
		if (messa != null) {
			return messa;
		}

		messa = validateVar(newProject);
		if (messa != null) {
			return messa;
		}

		Project projOld = projectDao.findById(saveProjId);

		if (!newProject.getCodigoProject().equals(projOld.getCodigoProject())) {
			if (projectDao.findByCode(newProject.getCodigoProject()) != null) {
				return getBundle("ERROR_PROJ_004");

			}
		}
		User newUser = userDao.findByEmail(newManagerEmail);
		if (newUser == null) {
			return getBundle("ERROR_PROJ_005");
		}

		State_project stateProj = null;
		if (!statDoProjeto.equals(projOld.getStateProject().getStateproj())) {
			stateProj = stateProjectDao.findByState(statDoProjeto);
			if (stateProj == null) {
				return getBundle("ERROR_PROJ_007");
			}
			projOld.setStateProject(stateProj);
		}

		projOld.setUser(newUser);
		projOld.setBegindate(newProject.getBegindate());
		projOld.setEnddate(newProject.getEnddate());
		projOld.setCodigoProject(newProject.getCodigoProject());
		projOld.setBudget(newProject.getBudget());
		projOld.setClient(newProject.getClient());
		projOld.setBusinesssector(newProject.getBusinesssector());
		projOld.setDescription(newProject.getDescription());
		projOld.setTitle(newProject.getTitle());
		projOld.setTypology(newProject.getTypology());
		System.out.println("7777");
		try {
			projectDao.merge(projOld);
			projectDao.flush();
			logger.info(getBundle("INFO_03") + " " + userImpl.getLoggedUser().getEmail() + " " + projOld);
		} catch (Exception e) {
			logger.error("  ");
			logger.error(getBundle("ERROR_PROJ_002"));
			e.printStackTrace();
			logger.error("  ");
			return getBundle("ERROR_PROJ_002");
		}

		return null;
	}

	private String validateVar(Project project) {
		String patternString = "^[a-zA-Z0-9]+$";

		if (project.getCodigoProject() == null || project.getCodigoProject().trim() == "") {
			return getBundle("WARN_001");
		}
		// if (!project.getCodigoProject().matches(patternString)) {
		// return
		// internationalization.getResourceBundle().getString("WARN_002");
		// }

		if (project.getTitle() == null || project.getTitle().trim() == "") {
			return getBundle("WARN_001");
		}
		// if (!project.getTitle().matches(patternString)) {
		// return
		// internationalization.getResourceBundle().getString("WARN_002");
		// }

		if (project.getClient() == null || project.getClient().trim() == "") {
			return getBundle("WARN_001");
		}
		// if (!project.getClient().matches(patternString)) {
		// return
		// internationalization.getResourceBundle().getString("WARN_002");
		// }

		if (project.getBusinesssector() == null ) {
			project.setClient("");
		}
		// if (!project.getBusinesssector().matches(patternString)) {
		// return
		// internationalization.getResourceBundle().getString("WARN_002");
		// }

		if (project.getDescription() == null) {
			project.setClient("");
		}
		// if (!project.getDescription().matches(patternString)) {
		// return
		// internationalization.getResourceBundle().getString("WARN_002");
		// }

		if (project.getBudget() < 0) {
			return getBundle("WARN_001");
		}
		if (project.getBegindate() == null) {
			return getBundle("WARN_001");
		}
		if (project.getEnddate() == null) {
			return getBundle("WARN_001");
		}

		// verificar se data é feriado
		if (!holidayImpl.thatDayIsHoliday(project.getBegindate())) {
			return getBundle("ERROR_HOLi_003") + " " + project.getBegindate().toString();
		}
		// verificar se data é feriado
		if (!holidayImpl.thatDayIsHoliday(project.getEnddate())) {
			return getBundle("ERROR_HOLi_003") + " " + project.getEnddate().toString();
		}

		// verificar se data invalida
		if (!holidayImpl.dayIsvalid(project.getBegindate())) {
			return getBundle("ERROR_HOLi_004") + " " + project.getBegindate().toString();
		}
		// verificar se data invalida
		if (!holidayImpl.dayIsvalid(project.getEnddate())) {
			return getBundle("ERROR_HOLi_004") + " " + project.getBegindate().toString();
		}

		if (project.getEnddate().before(project.getBegindate())) {
			return getBundle("ERROR_PROJ_009") + " " + project.getBegindate().toString();
		}
		
		
		return null;
	}

	public List<Project> getAllProjectsInBD() {
		List<Project> allProjects = projectDao.findAll();
		return allProjects;
	}

	public List<Project> fillProject() {
		List<Project> allProjects = projectDao.findProjectOpen();
		for (int i = 0; i < allProjects.size(); i++) {
			User projectUserc = userDao.find(allProjects.get(i).getUser().getIduser());
			allProjects.get(i).setUser(projectUserc);
		}
		return allProjects;
	}

	public List<State_project> findAllSateProject() {
		List<State_project> allStates = stateProjectDao.findAll();
		return allStates;
	}

	public List<Activity> getProjectActivities(int idProject) {
		try {
			return activityDao.findByIdProject(idProject);
		} catch (Exception e) {
			logger.debug("get project activities" + " ");
			logger.error(e);
		}
		return null;
	}

	public List<User> getProjectUsers(int idProject) {
		try {
			return allocationDao.findProjectUsers(idProject);
		} catch (Exception e) {
			logger.debug("get project users" + " ");
			logger.error(e);
		}
		return null;
	}

	public List<String> getAllStateProject() {
		List<State_project> allStateProject;
		try {
			allStateProject = stateProjectDao.findAll();
		} catch (Exception e) {
			logger.debug("get project status");
			logger.error(e);
			return null;
		}
		if (allStateProject == null) {
			return null;
		}

		List<String> listString = new ArrayList<String>();
		for (int i = 0; i < allStateProject.size(); i++) {
			listString.add(allStateProject.get(i).getStateproj());
		}
		return listString;

	}

	/**
	 * get message fron internationalization
	 * 
	 * @param key
	 *            String
	 * @return
	 */
	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}

	public List<Project> getAllProjectByManage(int idManager) {
		List<Project> projectManage;
		try {
			projectManage = projectDao.findProjectByManager(idManager);
			return projectManage;
		} catch (Exception e) {
			logger.debug("get project managers" + " ");
			logger.error(e);
			return null;
		}
	}

	public List<Project> pesquisaProjects(String byCode, String byTitulo, String byCliente, String stateSelected,
			Date beginDate, Date endDate, boolean flagProjetosUtilizador) {

		int st = 0;
		if (stateSelected != null && !stateSelected.trim().equals("")) {
			if (!stateSelected.equals("All") || !stateSelected.equals("Todos")) {
				State_project tmp = stateProjectDao.findByState(stateSelected);
				if (tmp != null) {
					st = tmp.getIdStateProj();
				}
			}
		}

		List<Role> roles = userDao.findListRoles(userImpl.getLoggedUser().getIduser());
		if (roles == null || roles.size() == 0) {
			logger.debug(getBundle("WARN_004") + " no roles");
			return null;
		}
		int idManager = isDirectorManager(roles);
		if (idManager < 0) {
			logger.debug(getBundle("WARN_004") + " no valid roles ");
			return null;
		} else if (idManager > 0) {
			idManager = userImpl.getLoggedUser().getIduser();
		}

		List<Project> projectsList = projectDao.pesquisaProjects(byCode, byTitulo, byCliente, st, beginDate, endDate,
				idManager);

		// se o user for utilizador vai buscar os projetos onde está alocado mas
		// só no beginArea (neste caso flagProjetosUtilizador = true)
		if (idManager > 0 && flagProjetosUtilizador) {
			List<Project> tmp = allocationDao.pesquisaProjects(byCode, byTitulo, byCliente, st, beginDate, endDate,
					idManager);
			if ((tmp != null && tmp.size() != 0)) {
				if (projectsList == null || projectsList.size() == 0) {
					projectsList = tmp;
				} else {
					projectsList.addAll(tmp);
				}
			}
		}
		if (projectsList == null || projectsList.size() == 0) {
			logger.debug(getBundle("WARN_004"));
			return null;
		}

		return projectsList;

	}

	private int isDirectorManager(List<Role> roles) {
		int id = -1;
		for (int i = 0; i < roles.size(); i++) {
			System.out.println("roles.get(i).getRole() " + roles.get(i).getRole());
			if (roles.get(i).getRole().equals("Diretor")) {
				return 0;
			} else if (roles.get(i).getRole().equals("Utilizador")) {
				id = 1;
			}
		}
		return id;
	}

	public String finishActivities(List<Activity> projectActivities) {
		State_activity stateActivity = state_activityDao.findByState("Terminada");
		if (stateActivity == null) {
			return null;
		}
		for (int i = 0; i < projectActivities.size(); i++) {
			if (!projectActivities.get(i).getStateActivity().equals("Terminada")) {

				projectActivities.get(i).setStateActivity(stateActivity);

				try {
					logger.info(getBundle("Proj_closeTit") + " " + projectActivities.get(i).getIdActivity() + " "
							+ projectActivities.get(i).getNameActivity() + " "
							+ projectActivities.get(i).getStateActivity().getStateActi() + " "
							+ userImpl.getLoggedUser().getEmail());
					activityDao.merge(projectActivities.get(i));
					activityDao.flush();
				} catch (Exception e) {
					logger.debug("Error on finish Activities");
					e.printStackTrace();
				}

			}

		}

		return null;
	}

	public List<User> getUtilizadores() {
		List<User> users = null;
		try {
			users = userDao.findActivRole(3);
			if (users != null ) {
				System.out.println(users.size());
			}
		} catch (Exception e) {
			logger.debug("Error on getUtilizadores");
			e.printStackTrace();
		}
		
		return users;
	}


	
	
	

}
