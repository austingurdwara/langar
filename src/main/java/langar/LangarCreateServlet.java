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
    	
        if (!"amarsinghaustin@gmail.com".equals(user.getEmail().toLowerCase())) {
        	System.out.println("user is " + user.getEmail());
    		resp.getWriter().println("Sorry!");
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
        
        String username = req.getParameter("UserName");
        String email = req.getParameter("UserEmail");
        String superuser = req.getParameter("SuperUser");
        String delete = req.getParameter("Delete");
        UserEntity userE = new UserEntity();
        if (username == null) username = "";
        if (email == null) email = "";
        if ("yes".equals(delete)) {
        	userE.deleteUser(username, email);
        } else if (!username.equals("")) 
        {
        	if (!"yes".equals(superuser)) superuser = "no";
        	userE.createUser(username, email, superuser);
        	req.setAttribute("message", "User " + username + " created!");
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
