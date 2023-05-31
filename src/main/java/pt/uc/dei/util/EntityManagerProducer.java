package pt.uc.dei.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceContext(unitName = "ehsilva-vmoreira-projFinal")
	protected EntityManager em;

	@Produces
	public EntityManager em() {
		return em;
	}

	public void dispose(@Disposes EntityManager em) {
		em.close();
	}
}
