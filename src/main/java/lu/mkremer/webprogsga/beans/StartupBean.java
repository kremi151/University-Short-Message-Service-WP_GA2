package lu.mkremer.webprogsga.beans;

import java.util.List;

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
public class StartupBean {//TODO: Run it with some default values before submission
	
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

			System.out.println("### Creating programmes ###");
			
			Programme prog1 = clm.createProgramme("Programme 1");//TODO: Test

			System.out.println("### Creating classes ###");
			
			Class class1 = clm.createClass("Class 1", prog1, userAdmin);//TODO: Test

			System.out.println("### Creating channels ###");
			
			Channel channel1 = cm.createChannel("Test channel #1", userAdmin, channel -> {
				channel.getClasses().add(class1);
			});//TODO: Test			
			Channel channel2 = cm.createChannel("Test channel #2", userAdmin, channel -> {
				channel.getClasses().add(class1);
			});//TODO: Test

			System.out.println("### Creating subscriptions ###");

			cm.subscribe(userUser, channel1);//TODO: Test
			cm.subscribe(userAdmin, channel1);//TODO: Test
			cm.subscribe(userAdmin, channel2);//TODO: Test

			System.out.println("### Creating messages ###");

			mm.postMessage("#TestA", "This message is only viewable for channel 1 subscribers", userAdmin, channel1);//TODO: Test
			mm.postMessage("#TestB", "This message is only viewable for channel 2 subscribers", userAdmin, channel2);//TODO: Test
			mm.postMessage("#TestC", "This message mentions the users @admin and @user, aswell as the message #TestA. Have a nice day.", userAdmin, channel2);//TODO: Test

			System.out.println("### Executing tests ###");//TODO: Remove?
			
			List<Channel> subs = cm.getChannelSubscriptions(userUser);
			System.out.println("# Channel subscriptions for 'user':");
			for(Channel c : subs)System.out.println("* " + c.getName());
			
			subs = cm.getChannelSubscriptions(userAdmin);
			System.out.println("# Channel subscriptions for 'admin':");
			for(Channel c : subs)System.out.println("* " + c.getName());
			
			System.out.println("### Done ###");
		}
	}
	
}
