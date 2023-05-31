package pt.uc.dei.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.State_project;
import pt.uc.dei.model.User;


@Stateless
public class StateProjectDao extends AbstractDao<State_project> implements Serializable  {

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(StateProjectDao.class);

	public StateProjectDao() {
		super(State_project.class);
	}
	
	public State_project findById(int idStateProj) {
			try {
				TypedQuery<State_project> q = em.createNamedQuery("State_project.findById", State_project.class);
				q.setParameter("idStateProj", idStateProj);
				return q.getSingleResult();
			} catch (NoResultException e) {
				logger.error(e.getMessage());
				return null;
			}
		}
	
	public State_project findByState(String stateproj) {
			try {
				TypedQuery<State_project> q = em.createNamedQuery("State_project.findByState", State_project.class);
				q.setParameter("stateproj", stateproj);
				return q.getSingleResult();
			} catch (NoResultException e) {
				logger.error(e.getMessage());
				return null;
			}
		}

}
