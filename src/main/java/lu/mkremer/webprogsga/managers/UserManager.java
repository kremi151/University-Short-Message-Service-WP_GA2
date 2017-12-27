package lu.mkremer.webprogsga.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.User;

@Local
public interface UserManager {

	User createUser(String userId, String email, String firstName, String lastName, String hashedPassword, boolean priviledged);
	void removeUser(User user);
	boolean doesUsernameExist(String username);
	User findUser(String username);
	List<User> listAllUsers();
	List<User> listMatchingUsers(String filter);
	void update(User user);
	
}
