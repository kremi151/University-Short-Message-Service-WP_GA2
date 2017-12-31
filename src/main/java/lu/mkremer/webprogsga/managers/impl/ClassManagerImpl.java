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
			Programme p = em.createQuery("select p from Programme p left join fetch p.classes where p.id = :id", Programme.class).setParameter("id", id).getSingleResult();
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

	@Override
	public List<Class> getAllClasses() {
		return em.createQuery("select c from Class c", Class.class).getResultList();
	}

	@Override
	public Programme findProgramme(long id, boolean fetchClasses) {
		if(fetchClasses) {
			try {
				return em.createQuery("select p from Programme p left join fetch p.classes where p.id = :id", Programme.class).setParameter("id", id).getSingleResult();
			}catch(NoResultException e) {
				return null;
			}
		}else {
			return em.find(Programme.class, id);
		}
	}

	@Override
	public Class deleteClass(long id) {
		try {
			Class c = em.createQuery("select c from Class c where c.id = :id", Class.class).setParameter("id", id).getSingleResult();
			em.remove(c);//TODO: Fix cascading deletion bug
			return c;
		}catch(NoResultException e) {
			return null;
		}
	}

	@Override
	public Class findClass(long id, boolean fetchChannels) {
		if(fetchChannels) {
			try {
				return em.createQuery("select c from Class c left join fetch c.channels where c.id = :id", Class.class).setParameter("id", id).getSingleResult();
			}catch(NoResultException e) {
				return null;
			}
		}else {
			return em.find(Class.class, id);
		}
	}

	@Override
	public void update(Class clazz) {
		em.merge(clazz);
	}

}
