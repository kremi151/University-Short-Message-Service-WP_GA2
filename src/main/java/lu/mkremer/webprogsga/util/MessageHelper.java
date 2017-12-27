package lu.mkremer.webprogsga.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageHelper {

	public static void throwDangerMessage(String message) {
		throwMessage(message, FacesMessage.SEVERITY_ERROR);
	}

	public static void throwInfoMessage(String message) {
		throwMessage(message, FacesMessage.SEVERITY_INFO);
	}
	
	public static void throwWarningMessage(String message) {
		throwMessage(message, FacesMessage.SEVERITY_WARN);
	}
	
	public static void throwMessage(String message, FacesMessage.Severity severity) {
		FacesMessage msg = new FacesMessage(message);
		msg.setSeverity(severity);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
}
