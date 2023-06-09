package pt.uc.dei.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractDao<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Class<T> clazz;

	@Inject
	protected EntityManager em;

	public AbstractDao(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T find(Object id) {
		return em.find(clazz, id);
	}

	public void persist(final T entity) {
		em.persist(entity);
	}

	public List<T> findAll() {
		final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
		criteriaQuery.select(criteriaQuery.from(clazz));
		return em.createQuery(criteriaQuery).getResultList();
	}

	public void remove(final T entity) {
		em.remove(em.merge(entity));
	}

	public void merge(final T entity) {
		em.merge(entity);
	}

	public void flush() {
		em.flush();
	}
}
