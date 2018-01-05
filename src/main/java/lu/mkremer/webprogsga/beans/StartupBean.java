package lu.mkremer.webprogsga.beans;

import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.persistence.User;

@Singleton
@Startup
public class StartupBean {
	
	private final static boolean INIT_DEFAULT_VALUES = true;
	
	@EJB private UserManager um;
	@EJB private ChannelManager cm;
	@EJB private ClassManager clm;
	@EJB private MessageManager mm;

	@SuppressWarnings("unused")
	@PostConstruct
	public void init() {
		if(INIT_DEFAULT_VALUES) {
			System.out.println("###############################");
			System.out.println("# Initializing default values #");
			System.out.println("###############################");

			System.out.println("### Creating users ###");
			
			User userAdmin = um.createUser("admin", "admin@admin.admin", "Admin", "Istrator", BCrypt.hashpw("adminadmin", BCrypt.gensalt()), true);
			User userUser = um.createUser("user", "user@email.com", "Some", "User", BCrypt.hashpw("test", BCrypt.gensalt()), false);
			User userBlocked = um.createUser("blocked", "block@me.com", "Iam", "Blocked", BCrypt.hashpw("blocked", BCrypt.gensalt()), false);
			userBlocked.setEnabled(false);
			um.update(userBlocked);

			System.out.println("### Creating programmes ###");

			Programme prog1 = clm.createProgramme("Study programme A", "Description for study programme A");
			Programme prog2 = clm.createProgramme("Study programme B", "Description for study programme B");

			System.out.println("### Creating classes ###");

			Class class1 = clm.createClass("Class A.1", prog1, userAdmin);
			Class class2 = clm.createClass("Class A.2", prog1, userAdmin);
			Class class3 = clm.createClass("Class B.1", prog2, userAdmin);
			Class class4 = clm.createClass("Class B.2", prog2, userAdmin);

			System.out.println("### Creating channels ###");
			
			Channel channel1 = cm.createChannel("Test channel #1", "Test channel description", userAdmin, channel -> {
				channel.getClasses().add(class1);
			});	
			Channel channel2 = cm.createChannel("Test channel #2", "Test channel description", userAdmin, channel -> {
				channel.getClasses().add(class2);
			});
			Channel channel3 = cm.createChannel("Test channel #3", "A channel where no one has subscribed yet", userUser, channel -> {
				channel.getClasses().add(class1);
				channel.getClasses().add(class3);
			});

			System.out.println("### Creating subscriptions ###");

			cm.subscribe(userUser, channel1);
			cm.subscribe(userAdmin, channel1);
			cm.subscribe(userAdmin, channel2);

			System.out.println("### Creating messages ###");

			Calendar baseDate = Calendar.getInstance();
			baseDate.add(Calendar.HOUR_OF_DAY, -1);
			mm.postMessage("#TestA", "This message is only viewable for channel 1 subscribers", userAdmin, channel1, baseDate.getTime());
			baseDate.add(Calendar.MINUTE, 10);
			mm.postMessage("#TestB", "This message is only viewable for channel 2 subscribers", userAdmin, channel2, baseDate.getTime());
			baseDate.add(Calendar.MINUTE, 8);
			mm.postMessage("#TestC", "This message is a response to #TestA written by @admin", userAdmin, channel1, baseDate.getTime());
			baseDate.add(Calendar.MINUTE, 12);
			mm.postMessage("#TestD", "I want to greet @admin and @blocked", userUser, channel2, baseDate.getTime());
			baseDate.add(Calendar.MINUTE, 14);
			mm.postMessage("#TestE", "I wonder if I am going to be blocked...", userBlocked, channel1, baseDate.getTime());
			baseDate.add(Calendar.MINUTE, 5);
			mm.postMessage("#TestF", "I heard that the account of @blocked has been disabled, so he cannot log in anymore. Am I right?", userUser, channel2, baseDate.getTime());

			System.out.println("### Done ###");
		}
	}
	
}
