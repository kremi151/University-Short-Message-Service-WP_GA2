package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.User;

@ManagedBean(name="selectuser")
@ViewScoped
public class SelectUserController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 619884985891934566L;

	@EJB
	private UserManager um;
	
	private String filter = null;
	
	public String getFilter() {
		return filter;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public List<User> getDisplayingUsers(){
		if(filter == null || filter.length() == 0) {
			return um.listAllUsers();
		}else {
			return um.listMatchingUsers(filter);
		}
	}
	
	public void onChange() {
		System.out.println("### Current filter: " + filter);
	}
	//TODO: Fix this AJAX bug which is impossible to be fixed no matter how hard I try...
	
}
