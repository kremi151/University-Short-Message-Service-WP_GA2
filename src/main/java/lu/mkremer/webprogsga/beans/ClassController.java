package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="viewclass")
@ViewScoped
public class ClassController implements Serializable{//TODO: Fix accordion expanding bug

	/**
	 * 
	 */
	private static final long serialVersionUID = 3557111055682521191L;
	
	@EJB private ClassManager clm;
	@EJB private ChannelManager cm;
	@EJB private UserManager um;
	
	//Channel creation
	
	@NotNull(message="No channel name provided")
	@Size(min=5, message="Channel name must have at least {min} characters")
	private String channelName;
	
	//Class values
	
	@NotNull(message="No class title provided")
	@Size(min=5, message="Class name must have at least {min} characters")
	private String className;
	
	@NotNull(message="No programme provided")
	private Programme classProgramme;
	
	@NotNull(message="No lecturer username provided")
	private String classLecturerUsername;
	
	private String classLecturerDisplayName;
	
	private Class cachedClass = null;
	
	//
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		reload();
	}
	
	public void reload() {
		cachedClass = null;
		if(session.isLoggedIn()) {
			String cid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(cid != null) {
				try {
					Class clazz = clm.findClass(Long.parseLong(cid), true);
					if(clazz != null) {
						className = clazz.getTitle();
						classProgramme = clazz.getProgramme();
						classLecturerUsername = clazz.getLecturer().getUsername();
						classLecturerDisplayName = clazz.getLecturer().getFullName();
						cachedClass = clazz;
					}
				}catch(NumberFormatException e) {
					MessageHelper.throwWarningMessage("The requested class has not been found");
				}
			}
		}
	}
	
	public boolean isExistent() {
		return cachedClass != null;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}

	public Programme getClassProgramme() {
		return classProgramme;
	}
	
	public void setClassProgramme(Programme classProgramme) {
		this.classProgramme = classProgramme;
	}

	public List<Channel> getChannels(){
		return cachedClass != null ? cachedClass.getChannels() : Collections.emptyList();
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getClassLecturerUsername() {
		return classLecturerUsername;
	}

	public void setClassLecturerUsername(String classLecturerUsername) {
		this.classLecturerUsername = classLecturerUsername;
	}

	public String getClassLecturerDisplayName() {
		return classLecturerDisplayName;
	}

	public void createChannel() {
		if(cachedClass != null && session.isElevated()) {
			cm.createChannel(channelName, session.getUser(), channel -> channel.getClasses().add(cachedClass));
			reload();
		}
	}
	
	public void modifyClass() {
		if(cachedClass != null && session.isElevated()) {
			User nlecturer = um.findUser(classLecturerUsername);
			if(nlecturer != null) {
				cachedClass.setTitle(className);
				cachedClass.setProgramme(classProgramme);
				cachedClass.setLecturer(nlecturer);
				clm.update(cachedClass);
			}else {
				MessageHelper.throwWarningMessage("The specified user has not been found");
			}
			reload();
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public List<Programme> getAllProgrammes(){
		return clm.getAllProgrammes();
	}
	
}
