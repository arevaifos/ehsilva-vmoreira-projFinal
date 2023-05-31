package pt.uc.dei.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.implement.ActivitiesImpl;
import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Activity_historic;
import pt.uc.dei.model.User;
import pt.uc.dei.model.UsersAllocations;

@Stateless
public class ActivityDao extends AbstractDao<Activity> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(ActivityDao.class);

	public ActivityDao() {
		super(Activity.class);
	}

	/**
	 * DEVOLVE AS ATIVIDADE QUE PERTENCEM A UM PROJECTO IDENTIFICADO POR UM ID
	 * @param idProject
	 * @return
	 */
	public List<Activity> findByIdProject(int idProject) {
		try {
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findListByIdProject", Activity.class);
			q.setParameter("idProject", idProject);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	 
	/**
	 * DEVOLVE AS ATIVIDADES DE UM DETERMINADO TIPO
	 * 
	 * @param idActivity
	 * @return
	 */
	public List<Activity> findListType(int idActivity) {
		try {
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findListType", Activity.class);
			q.setParameter("idActivity", idActivity);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}


	/**
	 * DEVOLVE TODAS AS ACTIVIDADES DE UM USER IDENTIFICADO PELO EMAIL
	 * @param email
	 * @return
	 */
	public List<Activity> findListUser(String email) {
		try {
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findListUser", Activity.class);
			q.setParameter("email", email);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * DEVOLVE AS ATIVIDADE QUE PODEM SER PRECEDENTES DE ACORDO COM AS DATAS
	 * @param idProject
	 * @param endDate
	 * @return
	 */
	public List<Activity> findValidPrecedentesNew(int idProject, Date endDate ) {
		try {
			logger.debug("Entrei no Activities findValidPrecedentesNew: EndDate "+endDate);
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findValidPrecedentesNew", Activity.class);
			q.setParameter("idProject", idProject);
			q.setParameter("endDate", endDate);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * DEVOLVE AS ATIVIDADES QUE PODEM SER PRECEDENTES DE ACORDO COM O ID DA ATIVIDADE QUE DEVE SER PRECEDIDA
	 * @param idActivity
	 * @return
	 */
	public List<Activity> findValidPrecedentes(int idActivity) {
		try {
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findValidPrecedentes", Activity.class);
			q.setParameter("idActivity", idActivity);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}		
	}
	
	
	/**
	 * DEVOLVE AS ATIVIDADES QUE SÃO PRECEDENTE DE UMA OUTRA ATIVIDADE
	 * @param idActivity
	 * @return
	 */
	public List<Activity> findByIdActivityPrecedentes(int idActivity) {
		try {
			TypedQuery<Activity> q = em.createNamedQuery("Activity.findByIdActivityPrecedentes", Activity.class);
			q.setParameter("idActivity", idActivity);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}		
	}

	

}



