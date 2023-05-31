package pt.uc.dei.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.implement.ProjectImpl;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Project;
import pt.uc.dei.model.UsersAllocations;

@Stateless
public class ProjectDao extends AbstractDao<Project> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(ProjectDao.class);

	@Inject
	private Internationalization internationalization;

	@Inject
	private Project project;

	@Inject
	private ProjectDao projectDao;

	public ProjectDao() {
		super(Project.class);
	}

	public Project findByCode(String codigoProject) {
		try {
			TypedQuery<Project> q = em.createNamedQuery("Project.findByCode", Project.class);
			q.setParameter("codigoProject", codigoProject);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Project> findProjectOpen() {
		try {
			TypedQuery<Project> q = em.createNamedQuery("Project.findProjectOpen", Project.class);
			return q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Project findById(int idProject) {
		try {
			TypedQuery<Project> q = em.createNamedQuery("Project.findById", Project.class);
			q.setParameter("idProject", idProject);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Project> findProjectByManager(int idManager) {
		try {
			TypedQuery<Project> q = em.createNamedQuery("Project.findProjectByManager", Project.class);
			q.setParameter("idManager", idManager);
			return q.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Project> pesquisaProjects(String byCode, String byTitulo, String byCliente, int stateSelectedId,
			Date beginDate, Date endDate, int idManager) {
 
		// construção do native query para pesquizar projetos
		String nativeQuery = "SELECT  idProject, codigoProject FROM projectmanagement.Projects "
				+ "WHERE ( begindate <= ? AND enddate >= ? )";
		
		if (byCode != null && !byCode.equals("")) {
			nativeQuery = nativeQuery + " AND codigoProject LIKE ?";
		}
		if (byTitulo != null && !byTitulo.equals("")) {
			nativeQuery = nativeQuery + " AND title LIKE ?";
		}
		if (byCliente != null && !byCliente.equals("")) {
			nativeQuery = nativeQuery + " AND client LIKE ?";
		}
		if (stateSelectedId > 0 ) {
			nativeQuery = nativeQuery + " AND state_project_idStateProj = ?";
		}
		if (idManager > 0 ) {
			nativeQuery = nativeQuery + " AND userGestor = ?";
		}	

		Query q = em.createNativeQuery(nativeQuery);
		q = q.setParameter(1, beginDate);
		q = q.setParameter(2, endDate);

		int i = 2;
		if (byCode != null && !byCode.equals("")) {
			q = q.setParameter(++i, "%" + byCode + "%");
			System.out.println(i + " " + q);
		}

		if (byTitulo != null && !byTitulo.equals("")) {
			q = q.setParameter(++i, "%" + byTitulo + "%");
		}

		if (byCliente != null && !byCliente.equals("")) {
			q = q.setParameter(++i, "%" + byCliente + "%");
		}

		if (stateSelectedId > 0 ) {
			q = q.setParameter(++i, stateSelectedId);
		}

		if (idManager > 0 ) {
			q = q.setParameter(++i, idManager);
		}

		List<Object[]> results1 = null;

		try {
			results1 = q.getResultList();
		} catch (Exception e) {
			logger.debug(getBundle("WARN_004"));
			e.printStackTrace();
			return null;
		}

		if (results1 == null || results1.size() == 0) {
			return null;
		}

		List<Project> projectsList = new ArrayList<Project>();

		for (i = 0; i < results1.size(); i++) {

			Object[] tmp = results1.get(i);	
			
			if (tmp[0] != null && String.valueOf(tmp[0]).trim().length() > 0) {

				Project projTmp = findById((int) tmp[0]);
				if (projTmp != null) {
					projectsList.add(projTmp);
				}
			}
		}

		return projectsList;

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

}
