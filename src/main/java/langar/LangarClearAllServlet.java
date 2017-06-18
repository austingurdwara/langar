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
public class LangarClearAllServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, javax.servlet.ServletException {
        	req.getRequestDispatcher("/ConfirmClearAllLangar.jsp").forward(req, resp);
    }
	
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {

	String response  = (String)req.getParameter("response");

        if (!"Yes".equals(response)) {
    		resp.sendRedirect( resp.encodeRedirectURL("/langar?No"));
                return;
        }
	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        
        if (user == null || user.equals("")) {
        	resp.getWriter().println("{\"rc\": \"error\", \"msg\": \"User not logged in\"}");
        	return;
        }
        //TODO
        //is user super user
        //is user authorized for changes they are asking?
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
 
        if (!isSuperUser) {
        	resp.getWriter().println("{\"rc\": \"error\", \"msg\": \"User not authorized\"}");
        	return;
        }
        
        
        
        try {
        	
        	LangarEntity l = new LangarEntity();
        	l.clearallSewa();
                l.setLangarClearanceDate();
    		resp.sendRedirect( resp.encodeRedirectURL("/langar?Yes"));
        } catch (Exception e) {
        	System.out.println("Faied " + e);
        	resp.getWriter().println("{\"rc\": \"error\", \"msg\": \"Sorry! failed to update. Please try again!\"}");
        }
        
	}

}
