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

import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="classes")
@ViewScoped
public class ClassesController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8992455822006519878L;
	
	private Programme programme;
	private List<Class> classes;
	
	@NotNull(message="No title provided")
	@Size(min=8, message="Title must have at least {min} characters")
	private String title;
	
	@NotNull(message="No programme provided")
	private Programme classProgramme;
	
	private long remClassId;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	@EJB private ClassManager clm;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		refetch();
	}
	
	public void refetch() {
		if(session.isLoggedIn()) {
			String progFilter = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("prog");
			if(progFilter != null) {
				try {
					programme = clm.findProgramme(Long.parseLong(progFilter), true);
					classes = programme.getClasses();
				}catch(NumberFormatException e) {
					MessageHelper.throwWarningMessage("Could not fetch classes for the requested programme");
				}
			}else {
				classes = clm.getAllClasses();
			}
		}else {
			programme = null;
			classes = Collections.emptyList();
		}
	}
	
	public List<Class> getClasses(){
		return classes;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Programme> getProgrammes(){
		return clm.getAllProgrammes();
	}
	
	public Programme getClassProgramme() {
		return classProgramme;
	}

	public void setClassProgramme(Programme classProgramme) {
		this.classProgramme = classProgramme;
	}

	public long getRemClassId() {
		return remClassId;
	}

	public void setRemClassId(long remClassId) {
		this.remClassId = remClassId;
	}

	public String getProgrammeName() {
		return programme != null ? programme.getName() : "Unknown";
	}
	
	public boolean isProgrammeFiltered() {
		return programme != null;
	}
	
	public String create() {
		if(session.isElevated()) {
			clm.createClass(title, classProgramme, session.getUser());
			MessageHelper.throwInfoMessage("Class \"" + title + "\" has been created");
			title = null;
			classProgramme = null;
			return "classes.xhtml?faces-redirect=true";
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
		return null;
	}
	
	public void delete() {
		if(session.isElevated()) {
			try {
				Class c = clm.deleteClass(remClassId);
				if(c != null) {
					MessageHelper.throwInfoMessage("Class \"" + c.getTitle() + "\" has been removed from the system");
				}else {
					MessageHelper.throwWarningMessage("The requested class has not been found");
				}
			}catch(RuntimeException e) {
				e.printStackTrace();
				MessageHelper.throwDangerMessage("An unexpected error occured: " + e.getMessage());
			}
			remClassId = 0;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}

}
