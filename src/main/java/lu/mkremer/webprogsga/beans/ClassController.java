package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.LinkedList;
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
	
	@NotNull(message="No lecturer provided")
	private User classLecturer;//TODO: Make lecturer modifiable over UI
	
	private List<Channel> classChannels = new LinkedList<>();//TODO: Make class channels modifiable over UI
	
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
						classChannels = clazz.getChannels();
						classLecturer = clazz.getLecturer();
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
		return classChannels;
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public User getClassLecturer() {
		return classLecturer;
	}

	public void setClassLecturer(User classLecturer) {
		this.classLecturer = classLecturer;
	}

	public void createChannel() {
		if(cachedClass != null && session.isElevated()) {
			cm.createChannel(channelName, session.getUser(), channel -> channel.getClasses().add(cachedClass));
			reload();
		}
	}
	
	public void modifyClass() {
		if(cachedClass != null && session.isElevated()) {
			cachedClass.setTitle(className);
			cachedClass.setProgramme(classProgramme);
			cachedClass.setChannels(classChannels);
			cachedClass.setLecturer(classLecturer);
			clm.update(cachedClass);
			reload();
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public List<Programme> getAllProgrammes(){
		return clm.getAllProgrammes();
	}
	
}
