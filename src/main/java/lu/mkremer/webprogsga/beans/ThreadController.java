package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.persistence.Tweed;

@ManagedBean(name="thread")
@ViewScoped
public class ThreadController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4553211744750698239L;
	
	private Tweed mainTweed;
	private List<Tweed> answers;
	
	@EJB private MessageManager mm;
	
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
		if(session.isLoggedIn()) {
			String tid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(tid != null) {
				mainTweed = mm.findMessage(tid);
				answers = mm.findAnswersFor(tid);
			}
		}
	}

	public Tweed getMainTweed() {
		return mainTweed;
	}

	public List<Tweed> getAnswers() {
		return answers;
	}
	
	//TODO: Don't show tweeds from channels to a user who has not subscribed this channel
	
}
