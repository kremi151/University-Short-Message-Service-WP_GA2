package lu.mkremer.webprogsga.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface ClassManager {

	Programme createProgramme(String name, String description);
	Programme deleteProgramme(long id);
	Programme findProgramme(long id, boolean fetchClasses);
	lu.mkremer.webprogsga.persistence.Class createClass(String title, Programme programme, User lecturer);
	lu.mkremer.webprogsga.persistence.Class deleteClass(long id);
	List<Programme> getAllProgrammes();
	List<lu.mkremer.webprogsga.persistence.Class> getProgrammeClasses(Programme p);
	List<lu.mkremer.webprogsga.persistence.Class> getAllClasses();
	
}
