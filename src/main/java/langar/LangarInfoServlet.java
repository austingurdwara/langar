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
public class LangarInfoServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		boolean readOnly = req.getParameter("view") != null;
		
		LangarEntity langarE = new LangarEntity();
		List<Entity> assignments = langarE.getLangar();
		Date lastmodified = langarE.getLangarModificationDate();
		req.setAttribute("assignments", assignments);
		req.setAttribute("lastmodified", lastmodified);
		
		if (readOnly) {
			try {
				req.getRequestDispatcher("/ReadOnlyLangar.jsp").forward(req, resp);
			} catch (ServletException se) {
				resp.getWriter().println("Something went wrong. Exception:" + se);
			}
			
			return;
		}
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null) {
        	 resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        	 return;
        }
        
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
        }
        
        req.setAttribute("currentuser", username);
        req.setAttribute("currentemail", email);
        try {
        	if (isSuperUser) {
        		List<Entity> users = userE.listUsers();
        		req.setAttribute("users", users);
        		req.getRequestDispatcher("/SuperUserLangar.jsp").forward(req, resp);
        	} else
        		req.getRequestDispatcher("/Langar.jsp").forward(req, resp);
        } catch (ServletException se) {
        	resp.getWriter().println("Something went wrong. Exception:" + se);
        }
	}
	

}
