package langar;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.*;
import java.util.*;


@SuppressWarnings("serial")
public class LangarCreateServlet extends HttpServlet {
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		boolean firstTime = req.getParameter("first") != null;
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if (user == null) {
       	 resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
       	 return;
        }
        

        /* Only User with UserManagement or amarsignhatustin are allowed access*/
        boolean isSuperUser = false;
        if ("amarsinghaustin@gmail.com".equals(user.getEmail().toLowerCase())) {
        	isSuperUser = true;
        	req.setAttribute("superuser", "true");
    	}

        if (firstTime && !isSuperUser) {
        	resp.getWriter().println("User not authorized");
        	return;
        }

        if (firstTime) {
        	LangarEntity langarE = new LangarEntity();
        	langarE.createLangar();
        	
        	UserEntity userE = new UserEntity();
        	userE.createFirstUser();
        	resp.getWriter().println("Done!");
        	return;
        }


        UserEntity userE = new UserEntity();
        Entity entity = userE.findUser(user.getEmail().toLowerCase());
        
        boolean isAllowUserMgmt = false;
        if (entity != null) {
        	String s = (String)entity.getProperty(UserEntity.ALLOWUSERMGMT);
        	if (UserEntity.SUPERUSERYES.equals(s))
        		isAllowUserMgmt = true;
        } 

 
        if (!isSuperUser && !isAllowUserMgmt) {
        	resp.getWriter().println("User not authorized");
        	return;
        }

        
        
        String username = req.getParameter("UserName");
        String email = req.getParameter("UserEmail");
        String superuser = req.getParameter("SuperUser");
        String delete = req.getParameter("Delete");
        String allowUserMgmt = req.getParameter("AllowUserMgmt");
        userE = new UserEntity();
        if (username == null) username = "";
        if (email == null) email = "";
        if ("yes".equals(delete)) {
                if (!isSuperUser) {
        	  resp.getWriter().println("User not authorized");
        	  return;
                }
        	userE.deleteUser(username, email);
        	req.setAttribute("message", "User " + email + " deleted");
            
        } else if (!username.equals("")) 
        {
        	if (!"yes".equals(superuser)) superuser = "no";
        	if (!"yes".equals(allowUserMgmt)) allowUserMgmt = "no";
                if (!isSuperUser) {
        	   userE.createOrUpdateUser(username, email);
                } else {
        	   userE.createOrUpdateUser(username, email, superuser, allowUserMgmt);
                }
        	req.setAttribute("message", "User " + username + " created/updated!");
        }
        
    	
    	List<Entity> users = userE.listUsers();
    	req.setAttribute("users", users);
		
		try {
			req.getRequestDispatcher("/Users.jsp").forward(req, resp);
		} catch (ServletException se) {
			resp.getWriter().println("Something went wrong. Exception:" + se);
		}
			
	}
}
