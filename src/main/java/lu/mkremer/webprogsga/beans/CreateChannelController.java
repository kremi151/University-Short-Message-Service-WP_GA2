package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.validators.ArraySize;

@ManagedBean(name="createchannel")
@RequestScoped
public class CreateChannelController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8542024660353278297L;

	@NotNull(message="No name provided")
	@Size(min=5, message="Name must have at least {min} characters")
	private String name;

	@NotNull(message="No description provided")
	@Size(min=10, message="Description must have at least {min} characters")
	private String description;
	
	@ArraySize(min=1, message="At least one class has to be linked to this channel")
	private Class selectedClasses[] = new Class[0];
	
	private List<Class> classes;
	
	@EJB private ClassManager clm;
	@EJB private ChannelManager cm;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		classes = clm.getAllClasses();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Class[] getSelectedClasses() {
		return selectedClasses;
	}

	public void setSelectedClasses(Class[] selectedClasses) {
		this.selectedClasses = selectedClasses;
	}

	public List<SelectItem> getSelectableClasses(){
		ArrayList<SelectItem> res = new ArrayList<>(classes.size());
		for(Class clazz : classes) {
			res.add(new SelectItem(clazz, clazz.getTitle()));
		}
		return res;
	}
	
	public String create() {
		if(session.isLoggedIn()) {
			Channel c = cm.createChannel(name, description, session.getUser(), channel -> channel.getClasses().addAll(Arrays.asList(selectedClasses)));
			cm.subscribe(session.getUser(), c);
			name = null;
			description = null;
			return "index.xhtml?faces-redirect=true&channel=" + c.getId();
		}
		return null;
	}
}
