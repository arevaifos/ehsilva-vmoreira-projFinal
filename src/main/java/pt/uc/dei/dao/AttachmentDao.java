package pt.uc.dei.dao;

import java.io.Serializable;
import java.sql.Blob;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import pt.uc.dei.model.Attachment;

@Stateless
public class AttachmentDao extends AbstractDao<Attachment> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(AttachmentDao.class);

	public AttachmentDao() {
		super(Attachment.class);
	}
	
	public List<Attachment> findByIdActivity(int idActivity) {
		try {
			TypedQuery<Attachment> q = em.createNamedQuery("Attachment.findByIdActivity", Attachment.class);
			q.setParameter("idActivity", idActivity);
			return q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}		
	}
	
}