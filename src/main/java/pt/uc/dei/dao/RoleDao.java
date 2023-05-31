package pt.uc.dei.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.Role;


@Stateless
public class RoleDao extends AbstractDao<Role> implements Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(RoleDao.class);

	public RoleDao() {
		super(Role.class);	
	}

	public Role findByRole(String role) {
		try {
			TypedQuery<Role> q = em.createNamedQuery("Role.findByRole", Role.class);
			q.setParameter("role", role);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
		
	}
	
}
