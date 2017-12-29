package lu.mkremer.webprogsga.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface ClassManager {

	Programme createProgramme(String name, String description);
	Programme deleteProgramme(long id);
	lu.mkremer.webprogsga.persistence.Class createClass(String title, Programme programme, User lecturer);
	List<Programme> getAllProgrammes();
	List<lu.mkremer.webprogsga.persistence.Class> getProgrammeClasses(Programme p);
	
}
