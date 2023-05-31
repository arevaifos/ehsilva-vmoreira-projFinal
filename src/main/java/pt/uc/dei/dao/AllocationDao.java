package pt.uc.dei.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Allocation;
import pt.uc.dei.model.Project;
import pt.uc.dei.model.User;
import pt.uc.dei.model.UsersAllocations;

@Stateless
public class AllocationDao extends AbstractDao<Allocation> implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(AllocationDao.class);

	@Inject
	private Internationalization internationalization;

	@Inject
	private ProjectDao projectDao;

	public AllocationDao() {
		super(Allocation.class);
	}

	public List<User> findProjectUsers(int idProject) {
		try {
			TypedQuery<User> q = em.createNamedQuery("Allocation.findProjectUsers", User.class);
			q.setParameter("idProject", idProject);
			return q.getResultList();
		} catch (Exception e) {
			logger.debug("Erro find project users ");
			logger.error(e);
			return null;
		}

	}

	public List<Allocation> findByActivityId(int idActivity) {
		try {
			TypedQuery<Allocation> q = em.createNamedQuery("Allocation.findByActivityId", Allocation.class);
			q.setParameter("idActivity", idActivity);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	public List<UsersAllocations> findAllocationByUsersInBD(Date beginDateAllocation, Date endDateAllocation) {
		Query q = em.createNativeQuery(
				"SELECT IDUSER , NAME , EMAIL , SUM(PERCENTAGE) PERCENTAGE  FROM View_Users_Allocations "
						+ "WHERE (BEGINDATEALLOCATION <= ? AND ENDDATEALLOCATION >= ?  ) OR  BEGINDATEALLOCATION IS NULL "
						+ "GROUP BY IDUSER , NAME , EMAIL");

		q = q.setParameter(1, beginDateAllocation);
		q = q.setParameter(2, endDateAllocation);

		List<Object[]> results = q.getResultList();

		List<UsersAllocations> usersAllocationsQuery = new ArrayList<>();

		for (Object[] result : results) {

			if (result[0] != null && String.valueOf(result[0]).trim().length() > 0) {

				int idUser = Integer.parseInt(String.valueOf(result[0]));

				String name = String.valueOf(result[1]);

				String email = String.valueOf(result[2]);

				BigDecimal percentage = BigDecimal.ZERO;

				if (result[3] != null && String.valueOf(result[3]).trim().length() > 0) {

					percentage = new BigDecimal(String.valueOf(result[3]));
				}

				usersAllocationsQuery.add(new UsersAllocations(idUser, name, email, percentage));
			}
		}

		for (UsersAllocations usersAllocations : usersAllocationsQuery) {
			System.out.println(usersAllocations.getPercentage());
		}
		return usersAllocationsQuery;

	}

	public List<Project> pesquisaProjects(String byCode, String byTitulo, String byCliente, int stateSelectedId,
			Date beginDate, Date endDate, int idUtilizador) {

		// construção do native query para pesquizar projetos onde um User está
		// alocado
		String nativeQuery = "SELECT DISTINCT aloc.projects_idProject,  aloc.user_idUser FROM Allocation aloc "
				+ " INNER JOIN Projects prj ON prj.idProject = aloc.projects_idProject "
				+ " WHERE aloc.user_idUser=? AND  prj.userGestor <> ? "
				+ " AND ( prj.begindate >= ? AND prj.enddate <= ? ) ";

		if (byCode != null && !byCode.equals("")) {
			nativeQuery = nativeQuery + " AND prj.codigoProject LIKE ?";
		}
		if (byTitulo != null && !byTitulo.equals("")) {
			nativeQuery = nativeQuery + " AND prj.title LIKE ?";
		}
		if (byCliente != null && !byCliente.equals("")) {
			nativeQuery = nativeQuery + " AND prj.client LIKE ?";
		}
		if (stateSelectedId > 0) {
			nativeQuery = nativeQuery + " AND prj.state_project_idStateProj = ?";
		}

		Query q = em.createNativeQuery(nativeQuery);

		int i = 0;
		q = q.setParameter(++i, idUtilizador);
		q = q.setParameter(++i, idUtilizador);
		q = q.setParameter(++i, beginDate);
		q = q.setParameter(++i, endDate);

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

		if (stateSelectedId > 0) {
			q = q.setParameter(++i, stateSelectedId);
		}

		List<Object[]> results1 = null;

		try {
			results1 = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

		if (results1 == null || results1.size() == 0) {
			logger.debug(getBundle("WARN_004") );
			return null;
		}

		List<Project> projectsList = new ArrayList<Project>();

		for (i = 0; i < results1.size(); i++) {

			Object[] tmp = results1.get(i);

			if (tmp[0] != null && String.valueOf(tmp[0]).trim().length() > 0) {

				Project projTmp = projectDao.findById((int) tmp[0]);
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
