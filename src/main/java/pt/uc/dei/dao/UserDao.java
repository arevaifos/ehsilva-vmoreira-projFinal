package pt.uc.dei.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.Role;
import pt.uc.dei.model.User;

@Stateless
public class UserDao extends AbstractDao<User> implements Serializable {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(UserDao.class);

	public UserDao() {
		super(User.class);
	}

	public User findByLogin(String emailLogin, String passwordLogin) {
		try {
			TypedQuery<User> q = em.createNamedQuery("User.findByLogin", User.class);
			q.setParameter("emailLogin", emailLogin);
			q.setParameter("passwordLogin", passwordLogin);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public User findByEmail(String emailLogin) {
		try {
			TypedQuery<User> q = em.createNamedQuery("User.findByEmail", User.class);
			q.setParameter("emailLogin", emailLogin);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public User findByIdUser(int idUser) {
		try {
			TypedQuery<User> q = em.createNamedQuery("User.findByIdUser", User.class);
			q.setParameter("idUser", idUser);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public String findSalt(String emailLogin) {
		try {
			TypedQuery<String> q = em.createNamedQuery("User.findSalt", String.class);
			q.setParameter("emailLogin", emailLogin);
			return q.getSingleResult();
		} catch (NoResultException e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<Role> findListRoles(int iduser) {
		try {
			TypedQuery<Role> q = em.createNamedQuery("User.findListRoles", Role.class);
			q.setParameter("userid", iduser);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	// devolve os utlizadores de um determinado role
	public List<User> findListRole(int idRole) {
		try {
			TypedQuery<User> q = em.createNamedQuery("User.findListRole", User.class);
			q.setParameter("idRole", idRole);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}


	// devolve os utlizadores de um determinado role
	public List<User> findActivRole(int idRole) {
		try {
			TypedQuery<User> q = em.createNamedQuery("User.findActivRole", User.class);
			q.setParameter("idRole", idRole);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

}
