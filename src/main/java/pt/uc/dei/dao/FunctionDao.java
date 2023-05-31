package pt.uc.dei.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.Function;



@Stateless
public class FunctionDao extends AbstractDao<Function> implements Serializable {

	private static final long serialVersionUID = -1347018103313599109L;
	final static Logger logger = Logger.getLogger(FunctionDao.class);

	public FunctionDao() {
		super(Function.class);
	}
	
	public Function findByDesignation(String designation) {
		try {
			TypedQuery<Function> q = em.createNamedQuery("Function.findByDesignation", Function.class);
			q.setParameter("designation", designation);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
}
