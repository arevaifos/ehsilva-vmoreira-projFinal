package pt.uc.dei.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.State_activity;
import pt.uc.dei.model.State_project;

@Stateless
public class State_activityDao extends AbstractDao<State_activity> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(State_activityDao.class);

	public State_activityDao() {
		super(State_activity.class);
	}

	public State_activity findByState(String stateActi) {
		try {
			TypedQuery<State_activity> q = em.createNamedQuery("State_activity.findByState", State_activity.class);
			q.setParameter("stateActi", stateActi);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
