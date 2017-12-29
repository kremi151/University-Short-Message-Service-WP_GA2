package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="programmes")
@ViewScoped
public class ProgrammesController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6968235022495782467L;
	
	@EJB private ClassManager clm;
	
	@NotNull(message="No title provided")
	@Size(min=8, message="Title must have at least {min} characters")
	private String title;
	
	@NotNull(message="No description provided")
	@Size(min=10, message="Description must have at least {min} characters")
	private String description;
	
	private long remProgId;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getRemProgId() {
		return remProgId;
	}

	public void setRemProgId(long remProgId) {
		this.remProgId = remProgId;
	}

	public List<Programme> getAllProgrammes(){
		return clm.getAllProgrammes();
	}
	
	public void create() {
		if(session.isElevated()) {
			clm.createProgramme(title, description);
			MessageHelper.throwInfoMessage("Programme \"" + title + "\" has been created");
			title = null;
			description = null;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void delete() {
		if(session.isElevated()) {
			try {
				Programme p = clm.deleteProgramme(remProgId);
				if(p != null) {
					MessageHelper.throwInfoMessage("Programme \"" + p.getName() + "\" has been removed from the system");
				}else {
					MessageHelper.throwWarningMessage("The requested programme has not been found");
				}
			}catch(RuntimeException e) {
				e.printStackTrace();
				MessageHelper.throwDangerMessage("An unexpected error occured: " + e.getMessage());
			}
			remProgId = 0;
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}

}
