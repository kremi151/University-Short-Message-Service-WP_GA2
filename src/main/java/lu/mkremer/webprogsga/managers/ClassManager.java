package lu.mkremer.webprogsga.managers;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface ClassManager {

	Programme createProgramme(String name);
	lu.mkremer.webprogsga.persistence.Class createClass(String title, Programme programme, User lecturer);
	
}
