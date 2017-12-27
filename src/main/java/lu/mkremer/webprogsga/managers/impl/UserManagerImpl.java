package lu.mkremer.webprogsga.managers.impl;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.User;

@Singleton
public class UserManagerImpl implements UserManager{

	@PersistenceContext
	private EntityManager em;

	@Override
	public User createUser(String userId, String email, String firstName, String lastName, String hashedPassword, boolean priviledged) {
		User user = new User(userId, email, firstName, lastName, hashedPassword, priviledged);
		em.persist(user);
		return user;
	}

	@Override
	public void removeUser(User user) {
		em.remove(user);
	}

	@Override
	public boolean doesUsernameExist(String username) {
		return !em.createQuery("select u from User u where username = :id").setParameter("id", username).getResultList().isEmpty();
	}

	@Override
	public User findUser(String username) {
		List<User> results = em.createQuery("select u from User u where username = :username", User.class)
				.setParameter("username", username)
				.getResultList();
		if(results.isEmpty()) {
			return null;
		}else {
			return results.get(0);
		}
	}

	@Override
	public List<User> listAllUsers() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}

	@Override
	public List<User> listMatchingUsers(String filter) {
		return em.createQuery("select u from User u where u.username like %:filter% or u.firstName like %:filter% or u.lastName like %:filter%", User.class)
				.setParameter("filter", filter).getResultList();
	}

	@Override
	public void update(User user) {
		em.merge(user);
	}
	
}
