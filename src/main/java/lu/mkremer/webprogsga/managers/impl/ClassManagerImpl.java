package lu.mkremer.webprogsga.managers.impl;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;

@Singleton
public class ClassManagerImpl implements ClassManager{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Programme createProgramme(String name, String description) {
		Programme p = new Programme(name, description);
		em.persist(p);
		return p;
	}

	@Override
	public Class createClass(String title, Programme programme, User lecturer) {
		Class c = new Class(title, programme, lecturer);
		em.persist(c);
		return c;
	}

	@Override
	public List<Programme> getAllProgrammes() {
		return em.createQuery("select p from Programme p", Programme.class).getResultList();
	}

	@Override
	public Programme deleteProgramme(long id) {
		try {
			Programme p = em.createQuery("select p from Programme p join fetch p.classes where p.id = :id", Programme.class).setParameter("id", id).getSingleResult();
			em.remove(p);//TODO: Fix cascading deletion bug
			return p;
		}catch(NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Class> getProgrammeClasses(Programme p) {
		List<Class> res = em.createQuery("select c from Class c where c.programme.id = :id", Class.class).setParameter("id", p.getId()).getResultList();
		p.setClasses(res);
		return res;
	}

}
