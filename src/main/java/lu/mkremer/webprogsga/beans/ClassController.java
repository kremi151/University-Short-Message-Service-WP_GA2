package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="viewclass")
@ViewScoped
public class ClassController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3557111055682521191L;
	
	@EJB private ClassManager clm;
	
	private Class clazz;
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		if(session.isLoggedIn()) {
			String cid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(cid != null) {
				try {
					clazz = clm.findClass(Long.parseLong(cid), true);
				}catch(NumberFormatException e) {
					MessageHelper.throwWarningMessage("The requested class has not been found");
				}
			}
		}
	}
	
	public String getClassName() {
		return clazz.getTitle();
	}
	
	public Programme getProgramme() {
		return clazz.getProgramme();
	}
	
	public List<Channel> getChannels(){
		return clazz.getChannels();
	}
	
}
