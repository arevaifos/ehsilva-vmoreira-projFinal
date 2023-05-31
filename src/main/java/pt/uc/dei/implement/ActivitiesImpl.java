package pt.uc.dei.implement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import pt.uc.dei.dao.ActivityDao;
import pt.uc.dei.dao.Activity_historicDao;
import pt.uc.dei.dao.AllocationDao;
import pt.uc.dei.dao.AttachmentDao;
import pt.uc.dei.dao.State_activityDao;
import pt.uc.dei.dao.Type_activityDao;
import pt.uc.dei.dao.UserDao;
import pt.uc.dei.exceptions.UserDataInvalidException;
import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Activity_historic;
import pt.uc.dei.model.Allocation;
import pt.uc.dei.model.Attachment;
import pt.uc.dei.model.State_activity;
import pt.uc.dei.model.Type_activity;
import pt.uc.dei.model.User;
import pt.uc.dei.model.UsersAllocations;

@Named("activitiesImpl")
@SessionScoped
public class ActivitiesImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(ActivitiesImpl.class);

	@Inject
	private UserDao userDao;

	@Inject
	private ActivityDao activityDao;

	@Inject
	private AllocationDao allocationDao;

	@Inject
	private Type_activityDao type_activityDao;

	@Inject
	private State_activityDao state_activityDao;

	@Inject
	private Activity_historicDao activityHistoricDao;

	@Inject
	private UserImpl userImpl;
	
	@Inject
	private AttachmentDao attachmentDao;

	
	public List<Attachment> fillAllAttachs(int idActivity) {
		List<Attachment> allAttachsByAtivity=attachmentDao.findByIdActivity(idActivity);
		return allAttachsByAtivity;
	}
	

	/**
	 * METODO QUE SALVA NA BASE DE DADOS UMA NOVA ATIVIDADE, UMA NOVA ALOCAÇÃO, SE ESTA NAO VIER A NULL, E UMA ENTRADA À TABELA DE HISTORICO DE ATIVIDADES.
	 * 
	 * @param newActivity
	 * @param newAllocation
	 * @return
	 * @throws UserDataInvalidException
	 */
	public boolean saveNewActivityIndDB(Activity newActivity, Allocation newAllocation) throws UserDataInvalidException {
		Activity_historic activityStoric=new Activity_historic();
		if (userImpl.getLoggedUser() != null) {

			if (userImpl.getLoggedUser().getIsactive()) {
				if (userImpl.isDirectorUser()
						|| userImpl.getLoggedUser().getEmail().equals(newActivity.getProject().getUser().getEmail())) {

					try {
						logger.debug("XIIIIIIIIIIIIIIIIIIIIIIIII " + newActivity);
						State_activity state = state_activityDao.find(1);
						newActivity.setStateActivity(state);

						if (newAllocation != null && newAllocation.getProject() != null) {
							logger.debug("NEW ACTIVITY WITH TITLE1");
							activityDao.persist(newActivity);
							activityDao.flush();
							newAllocation.setActivity(newActivity);
							
							allocationDao.persist(newAllocation);
							allocationDao.flush();
							activityStoric.setBegindateactnew(newActivity.getBeginDateact());
							activityStoric.setDescriptionnew(newActivity.getDescriptionActivity());
							activityStoric.setEnddateactnew(newActivity.getEndDateact());
							activityStoric.setNamenew(newActivity.getNameActivity());
							activityStoric.setTimeleftnew(newActivity.getTimeLeft());
							activityStoric.setTimeplannew(newActivity.getTimePlan());
							activityStoric.setTimespendnew(newActivity.getTimeSpend());
							activityStoric.setTypeActivity(newActivity.getTypeActivity());
							activityStoric.setActivity(newActivity);
							activityStoric.setAllocation(newAllocation);
							activityStoric.setStateActivity(state);
							activityStoric.setHistoricdate(new Date());
							activityStoric.setUser(userImpl.getLoggedUser());
							activityHistoricDao.persist(activityStoric);
							activityHistoricDao.flush();

							logger.debug("NEW ACTIVITY WITH TITLE " + newActivity.getNameActivity()
							+ "AND NEW ALLOCATION WITH ID " + newAllocation.getIdAllocation() + " SAVE IN BD ");

						} else {
							logger.debug("NEW ACTIVITY WITH TITLE2");
							activityDao.persist(newActivity);
							activityDao.flush();
							activityStoric.setBegindateactnew(newActivity.getBeginDateact());
							activityStoric.setDescriptionnew(newActivity.getDescriptionActivity());
							activityStoric.setEnddateactnew(newActivity.getEndDateact());
							activityStoric.setNamenew(newActivity.getNameActivity());
							activityStoric.setTimeleftnew(newActivity.getTimeLeft());
							activityStoric.setTimeplannew(newActivity.getTimePlan());
							activityStoric.setTimespendnew(newActivity.getTimeSpend());
							activityStoric.setTypeActivity(newActivity.getTypeActivity());
							activityStoric.setActivity(newActivity);
							activityStoric.setStateActivity(state);
							activityStoric.setHistoricdate(new Date());
							activityStoric.setUser(userImpl.getLoggedUser());
							activityHistoricDao.persist(activityStoric);
							activityHistoricDao.flush();
							logger.debug("NEW ACTIVITY WITH TITLE " + newActivity.getNameActivity() + " SAVE IN BD ");
						}


						return true;
					} catch (Exception e) {
						logger.error("ERROR TO SAVE IN BD! " + e.toString());
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return false;
	}

	/**
	 * METODO QUE SALVA AS ALTERAÇÕES DE UMA ATIVIDADE NA BD E CRIA UMA NOVA ALOCAÇÃO, ASSIM COMO ADICONA UMA LINHA À TABELA DE HISTORICO DE ATIVIDADES.
	 * 
	 * @param editActivity
	 * @param editAllocation
	 * @return
	 * @throws UserDataInvalidException
	 */
	public boolean saveEditActivityIndDB(Activity editActivity, Allocation editAllocation) throws UserDataInvalidException {		
		Activity_historic activityStoric=new Activity_historic();
		if (userImpl.getLoggedUser() != null) {

			if (userImpl.getLoggedUser().getIsactive()) {
				if (userImpl.isDirectorUser()
						|| userImpl.getLoggedUser().getEmail().equals(editActivity.getProject().getUser().getEmail())) {

					try {
						
						if (editAllocation != null && editAllocation.getProject() != null) {
							activityDao.merge(editActivity);
							editAllocation.setActivity(editActivity);
							activityDao.flush();
							allocationDao.persist(editAllocation);
							allocationDao.flush();
							activityStoric.setBegindateactnew(editActivity.getBeginDateact());
							activityStoric.setDescriptionnew(editActivity.getDescriptionActivity());
							activityStoric.setEnddateactnew(editActivity.getEndDateact());
							activityStoric.setNamenew(editActivity.getNameActivity());
							activityStoric.setTimeleftnew(editActivity.getTimeLeft());
							activityStoric.setTimeplannew(editActivity.getTimePlan());
							activityStoric.setTimespendnew(editActivity.getTimeSpend());
							activityStoric.setTypeActivity(editActivity.getTypeActivity());
							activityStoric.setActivity(editActivity);
							activityStoric.setAllocation(editAllocation);
							activityStoric.setStateActivity(editActivity.getStateActivity());
							activityStoric.setHistoricdate(new Date());
							activityStoric.setUser(userImpl.getLoggedUser());
							activityHistoricDao.persist(activityStoric);
							activityHistoricDao.flush();
							logger.debug("edit ACTIVITY WITH TITLE " + editActivity.getNameActivity()
							+ "AND edit ALLOCATION WITH ID " + editAllocation.getIdAllocation() + " SAVE IN BD ");

						} else {
							logger.debug("edit ACTIVITY WITH TITLE2");
							activityDao.merge(editActivity);
							activityDao.flush();
							activityStoric.setBegindateactnew(editActivity.getBeginDateact());
							activityStoric.setDescriptionnew(editActivity.getDescriptionActivity());
							activityStoric.setEnddateactnew(editActivity.getEndDateact());
							activityStoric.setNamenew(editActivity.getNameActivity());
							activityStoric.setTimeleftnew(editActivity.getTimeLeft());
							activityStoric.setTimeplannew(editActivity.getTimePlan());
							activityStoric.setTimespendnew(editActivity.getTimeSpend());
							activityStoric.setTypeActivity(editActivity.getTypeActivity());
							activityStoric.setActivity(editActivity);
							activityStoric.setStateActivity(editActivity.getStateActivity());
							activityStoric.setHistoricdate(new Date());
							activityStoric.setUser(userImpl.getLoggedUser());
							activityHistoricDao.persist(activityStoric);
							activityHistoricDao.flush();
							logger.debug("edit ACTIVITY WITH TITLE " + editActivity.getNameActivity() + " SAVE IN BD ");
						}
						return true;
					} catch (Exception e) {
						logger.error("ERROR TO SAVE IN BD! " + e.toString());
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return false;
	}

	
	/**
	 * MÉTODO PARA REPORTAR ESFORÇO
	 * 
	 * @return
	 */	
	public boolean reportEffortBD(Activity editActivity, String comment, List<byte[]> byteFileList,
			List<String> nameFile, List<String> nameMimeFile) {
		try {
			activityDao.merge(editActivity);
			activityDao.flush();
			Activity_historic activityHistoric= new Activity_historic();
			activityHistoric.setBegindateactnew(editActivity.getBeginDateact());
			activityHistoric.setDescriptionnew(editActivity.getDescriptionActivity());
			activityHistoric.setEnddateactnew(editActivity.getEndDateact());
			activityHistoric.setNamenew(editActivity.getNameActivity());
			activityHistoric.setTimeleftnew(editActivity.getTimeLeft());
			activityHistoric.setTimeplannew(editActivity.getTimePlan());
			activityHistoric.setTimespendnew(editActivity.getTimeSpend());
			activityHistoric.setTypeActivity(editActivity.getTypeActivity());
			activityHistoric.setActivity(editActivity);
			activityHistoric.setStateActivity(editActivity.getStateActivity());
			activityHistoric.setHistoricdate(new Date());
			activityHistoric.setUser(userImpl.getLoggedUser());
			activityHistoric.setDetails(comment);
			activityHistoricDao.persist(activityHistoric);
			activityHistoricDao.flush();
			if(byteFileList.size()>0){
				for(int i=0; i<byteFileList.size();i++){
					Attachment newAttach = new Attachment();
					newAttach.setAttachmentFile(byteFileList.get(i));
					newAttach.setActivityHistoric(activityHistoric);
					newAttach.setMimefile(nameMimeFile.get(i));
					newAttach.setNamefile(nameFile.get(i));
					attachmentDao.persist(newAttach);
					attachmentDao.flush();
				}
			}
			return true;
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	

	public User gatherAllocationUser(int id) {
		User userAllocation = userDao.find(id);
		return userAllocation;
	}

	/**
	 * @param idActivity
	 * @return
	 */
	public Allocation gatherAllocation(int idActivity) {
		List<Allocation> allocationByIdActivity = allocationDao.findByActivityId(idActivity);
		if (allocationByIdActivity != null || !allocationByIdActivity.isEmpty()) {
			int allocationID = 0;
			Allocation allocation = new Allocation();
			for (int i = 0; i < allocationByIdActivity.size(); i++) {
				if (allocationByIdActivity.get(i).getIdAllocation() > allocationID) {
					allocationID = allocationByIdActivity.get(i).getIdAllocation();
					allocation = allocationByIdActivity.get(i);
				}

				return allocation;
			}

		} else {
			return null;
		}
		return null;
	}


	/**
	 * @return
	 */
	public List<User> fillEmployers() {
		List<User> allUsers = userDao.findAll();
		for (User u : allUsers) {
			u.setRoles(userDao.findListRoles(u.getIduser()));
		}
		return allUsers;
	}

	/**
	 * @param idProject
	 * @return
	 */
	public List<Activity> fillactivities(int idProject) {

		List<Activity> activitiesByProject = activityDao.findByIdProject(idProject);
		return activitiesByProject;
	}

	/**
	 * @return
	 */
	public List<Type_activity> fillTypeActivitys() {
		List<Type_activity> allTypes = type_activityDao.findAll();
		for (Type_activity a : allTypes) {

			a.setActivities(activityDao.findListType(a.getIdtypeActivity()));
		}
		for (int i = 0; i < allTypes.size(); i++) {
			logger.debug(allTypes.get(i).toString());
		}
		return allTypes;
	}

	/**
	 * @param newActvity
	 */
	public void savePrecedentes(Activity newActvity) {

		try {
			activityDao.merge(newActvity);
			activityDao.flush();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}


	/**
	 * @return
	 */
	public List<Activity> fillBeginActivity() {
		if (userImpl.isDirectorUser()) {
			return activityDao.findAll();
		} else {
			return activityDao.findListUser(userImpl.getLoggedUser().getEmail());
		}

	}

	/**
	 * @param idProject
	 * @param endDate
	 * @return
	 */
	public List<Activity> fillPrecedencesActivitiesNew(int idProject, Date endDate) {
		List<Activity> activitiesByProject = activityDao.findValidPrecedentesNew(idProject, endDate);
		return activitiesByProject;
	}

	/**
	 * @param idActivity
	 * @return
	 */
	public List<Activity> fillPrecedencesActivitiesEdit(int idActivity){
		List<Activity> activitiesPrecedences=activityDao.findByIdActivityPrecedentes(idActivity);
		return activitiesPrecedences;
	}

	
	/**
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<UsersAllocations> findAllocationByUsers(Date beginDate, Date endDate){
		List<UsersAllocations> percentageAllocation=new ArrayList<>();
		List<UsersAllocations> percentageByUser=allocationDao.findAllocationByUsersInBD(beginDate, endDate);
		for(int i=0; i<percentageByUser.size();i++){
			BigDecimal value = new BigDecimal("100");
			if(percentageByUser.get(i).getPercentage().compareTo(value)==-1){
				percentageAllocation.add(percentageByUser.get(i));
			}
		}
		return percentageAllocation;

	}

	/**
	 * @return
	 */
	public List<State_activity> fillStatesActivity() {
		return state_activityDao.findAll();
	}

	/**
	 * @param id
	 * @return
	 */
	public Activity findActivityToMerge(int id) {
		
		return activityDao.find(id);
	}

	/**
	 * @param activityStoric
	 */
	public void saveActivityHistoricInDB(Activity_historic activityStoric) {
		activityStoric.setHistoricdate(new Date());
		activityHistoricDao.persist(activityStoric);
		activityHistoricDao.flush();

	}	
}
