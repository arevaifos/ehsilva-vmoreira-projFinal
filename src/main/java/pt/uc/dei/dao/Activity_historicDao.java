package pt.uc.dei.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.Activity;
import pt.uc.dei.model.Activity_historic;

@Stateless
public class Activity_historicDao extends AbstractDao<Activity_historic> implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final static Logger logger = Logger.getLogger(Activity_historicDao.class);

		public  Activity_historicDao() {
			super( Activity_historic.class);
		}

		public List<Activity_historic> findByIdActivity(int idActivity) {
			try {
				TypedQuery<Activity_historic> q = em.createNamedQuery("Activity_historic.findByIdActivity", Activity_historic.class);
				q.setParameter("idActivity", idActivity);
				return q.getResultList();
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				return null;
			}		
		}
		
}
