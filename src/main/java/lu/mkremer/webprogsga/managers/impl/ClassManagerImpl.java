package lu.mkremer.webprogsga.managers.impl;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
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
	public Programme createProgramme(String name) {
		Programme p = new Programme(name);
		em.persist(p);;
		return p;
	}

	@Override
	public Class createClass(String title, Programme programme, User lecturer) {
		Class c = new Class(title, programme, lecturer);
		em.persist(c);
		return c;
	}

}
