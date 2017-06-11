package langar;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.*;

import java.util.*;


@SuppressWarnings("serial")
public class LangarUpdateServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getWriter().println("Error: Get is not supported");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        
        if (user == null || user.equals("")) {
        	resp.getWriter().println("{\"rc\": \"error\", \"msg\": \"User not logged in\"}");
        	return;
        }
        //TODO
        //is user super user
        //is user authorized for changes they are asking?
        //do we have all the data we need for update?
        String email = user.getEmail();
        UserEntity userE = new UserEntity();
        Entity entity = userE.findUser(email);
        
        boolean isSuperUser = false;
        String username = user.getNickname();
        if (entity != null) {
        	username = (String)entity.getProperty(UserEntity.NAME);
        	String s = (String)entity.getProperty(UserEntity.SUPERUSER);
        	if (s.equals(UserEntity.SUPERUSERYES))
        		isSuperUser = true;
        } else {
        	System.out.println("User email " + email + " does not have an account");
        }
        
        String sewa = req.getParameter("sewa");
        String counter = req.getParameter("counter");
        String prevName = req.getParameter("prevName");
        String newName = req.getParameter("newName");
        String newEmail = req.getParameter("newEmail");
        String action = req.getParameter("action");
        if (sewa == null || sewa.equals("")) {
        	resp.getWriter().println("{\"rc\": \"error\", \"msg\": \"Missing sewa parameter\"");
        	return;
        }
        
        if (counter == null || counter.equals("")) {
        	resp.getWriter().println("{\"rc\":\"error\", msg: \"Missing counter parameter\"");
        	return;
        }
        
        if (isSuperUser) {
        	//superuser can enter name and email. if not given assume super user is signing up for him or herself
        	if (newName == null || newName.equals("")) {
        		//if no name is given, assign to logged in user
        		newName = username;
        		newEmail = user.getEmail();
        	}
        } else {
        	 //force to logged in user, ignoring what is passed
        	newEmail = user.getEmail();
        	newName = username;
        }
        
        if ("delete".equals(action)) {
        	newEmail = "";
        	newName = "";
        }
        
        //System.out.println("Inside LangarUpdate Servlet newName=" + newName);
        
        try {
        	
        	LangarEntity l = new LangarEntity();
        	ArrayList<String> assignedNames = l.updateSewa(sewa, counter, prevName, newName, newEmail);
        	if (isSuperUser) {
        		resp.getWriter().println("{ \"rc\": \"success\", \"email\": \"" + newEmail + "\", \"name\": \"" + newName +"\" }");
        	} else {
        		StringBuffer names = new StringBuffer(100);
        		ArrayList<String> assignedMe = new ArrayList<String>();
        		
        		int assignedMeCount=0;
        		for (String n: assignedNames) {
        			if (n.equals(username)) {
        				assignedMe.add(n);
        				assignedMeCount++;
        			}
        		}
        		names.append(Utils.mkString(assignedMe, "<b>", "</b>", ", "));
        		
        		ArrayList<String> assignedOthers = new ArrayList<String>();
        		
        		for (String n: assignedNames) {
        			if (!n.equals(username)) {
        				assignedOthers.add(n);
        			}
        		}
        		
        		if (assignedMeCount > 0) {
        			names.append(", ");
        		}
        		
        		names.append(Utils.mkString(assignedOthers, "", "", ", "));
        		resp.getWriter().println("{ \"rc\": \"success\", \"value\": \"" + newName + "\", \"text\": \"" + names.toString() + "\" }");
        	}
        	return;
        } catch (Exception e) {
        	System.out.println("Faied " + e);
        	resp.getWriter().println("{\"rc\": \"error\", \"msg\": \"Sorry! failed to update. Please try again!\"}");
        }
        
	}

}
