package lu.mkremer.webprogsga.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Class;

@ManagedBean(name="classConverter")
@RequestScoped
public class ClassConverter implements Converter, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7966992731196701599L;
	@EJB private ClassManager clm;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			return clm.findClass(Long.parseLong(value), false);
		}catch(NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value.getClass() == Class.class) {
			return String.valueOf(((Class)value).getId());
		}else {
			return "";
		}
	}

}
