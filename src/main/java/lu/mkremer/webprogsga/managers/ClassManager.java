package lu.mkremer.webprogsga.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface ClassManager {

	Programme createProgramme(String name, String description);
	Programme deleteProgramme(long id);
	Programme findProgramme(long id, boolean fetchClasses);
	Class createClass(String title, Programme programme, User lecturer);
	Class deleteClass(long id);
	Class findClass(long id, boolean fetchChannels);
	List<Programme> getAllProgrammes();
	List<Class> getProgrammeClasses(Programme p);
	List<Class> getAllClasses();
	
}
