package langar;
import java.util.*;
import java.io.*;


import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.*;



public class Utils {
	
	public static String mkString(List<String>in, String begin, String end, String join) {
		StringBuffer rc = new StringBuffer();
		if (begin != null) rc.append(begin);
		int i=0;
		for (String s: in) {
			if (i>0 && join != null) {
				rc.append(join);
			}
			rc.append(s);
			i++;
		}
		if (end != null) rc.append(end);
		return rc.toString();
	}
	
	public static void createUserSnippet(Writer out1, String _user, String _email, List<Entity> _assignments, String _sewa, String _text) throws IOException {
                  PrintWriter out = new PrintWriter(out1);
		  out.println("<div id=\"" + _sewa + "\"><b>");
		  out.println(_text + "</b><br>");
		  
		  List<String> assignedMe = new ArrayList<String>();
		  List<String> assignedOthers = new ArrayList<String>();
		  StringWriter checkedText = new StringWriter();
		  StringWriter uncheckedText = new StringWriter();
		  int assignedMeCount = 0;
		  out.println("<div class=\"" + _sewa + "\" >");
		  for (Entity assignment: _assignments) {
		      String sewa = (String)assignment.getProperty("sewa");
		      if (sewa == null) continue;
		      if (sewa.equals(_sewa)) {
		    	  String name = (String)assignment.getProperty("name");
			      String email = (String)assignment.getProperty("email");
			      if (email == null) email = "";
			      String counter = (String)assignment.getProperty("counter");
			      if ("not needed".equals(name)) continue;
			      if (!name.equals("")) {
			    	  if (name.equals(_user)) { 
			    		  assignedMe.add(name);
			    		  assignedMeCount++;
			    	  } else
			    		  assignedOthers.add(name);
			    	  
			      }
		    	  if (name.equals("")) {
		    		  uncheckedText.append("<input type=\"checkbox\" id=\"" + sewa + counter +
		    				  "\" value=\"" + name +"\" " +
		    				  "\"  onclick=\"return callServer('" + sewa+"', '" + counter +"');\"/>");
		    	  } else {
		    		  if (email.equals(_email)) 
		    			  checkedText.append("<input type=\"checkbox\" id=\"" + sewa + counter +
		    					  "\" value=\"" + name +"\" " +
		    					  "\" checked onclick=\"return callServer('" + sewa+"', '" + counter +"');\"/>");
		    	  }
		      }
		        
		  }
		  
		  out.print(mkString(assignedMe, "<b>", "</b>", ", "));
		  if (assignedMeCount > 0)
			  out.print(", ");
		  out.println(mkString(assignedOthers, "", "", ", "));
		  out.println("</div>");
		  out.println("<div class=\"s_" + _sewa + "\">");
		  out.println(checkedText.toString());
		  out.println(uncheckedText.toString());
		  out.println("</div></div><br/><br/>");
	}
	
	public static void createReadOnlySnippet(Writer out1, List<Entity> _assignments, String _sewa, String _text) throws IOException {
		
                  PrintWriter out = new PrintWriter(out1);
		  out.println("<tr>");
		  out.println("<td>" + _text + "</td><td width=650><table><tr>");
		  int count = 0;
		  ArrayList<String> assigned = new ArrayList<String>();
		  ArrayList<String> needed = new ArrayList<String>();
		  for (Entity assignment: _assignments) {
			  String sewa = (String)assignment.getProperty("sewa");
		      if (sewa == null) continue;
		      if (sewa.equals(_sewa)) {
		    	  String name = (String)assignment.getProperty("name");
			      String email = (String)assignment.getProperty("email");
			      String counter = (String)assignment.getProperty("counter");
			      if ("not needed".equals(name)) continue;
			      if (name.equals("")) needed.add("y");
			      else assigned.add(name);
			  }
		  }
		  
		  for (String _assigned:assigned) {
		    count = count + 1;
		    if ((count-1) % 3 == 0) {
		      out.println("</tr><tr>");
		    }
		    out.println("<td width='200'>" + _assigned + "</td>");
		  }
		  
		  for (String _needed: needed) {
		    count = count + 1;
		    if ((count-1) % 3 == 0) {
		      out.println("</tr><tr>");
		    }
		    out.println("<td width='200'><span style='color:red;font-weight:bold'>needed</span></td>");
		  }
		  for (int i=0; i<2; i++) {
			count = count + 1;  
		    if ((count-1) % 3 == 0) {
		      break;
		    }
		    out.println("<td width='200'>.</td>");
		  }
		  out.println("</tr></table></td></tr>");
	}
	
	
	public static void createSuperUserSnippet(Writer out1, List<Entity> _assignments, String _sewa, String _text) throws IOException {
		
                  PrintWriter out = new PrintWriter(out1);
		  out.println("<div id=\"" + _sewa + "\">");
		  out.println(_text);
		  out.println("<table>");
		  int count = 0;
		  
		  for (Entity assignment: _assignments) {
			  String sewa = (String)assignment.getProperty("sewa");
		      if (sewa == null) continue;
		      if (sewa.equals(_sewa)) {
		    	  String name = (String)assignment.getProperty("name");
			      String email = (String)assignment.getProperty("email");
			      String counter = (String)assignment.getProperty("counter");
			      if ("not needed".equals(name)) continue;
			      
			      out.println("<tr><td>");
			      
			      if (name.equals("")) {
			    	  out.println("<input type=\"text\" id=\"i" + sewa + counter + "\"/></td>");
			      } else {
			    	  out.println("<input type=\"text\" id=\"i" + sewa + counter + "\" disabled value=\"" + name +"\"/></td>");
			      }
		      	  out.println("<td>");
		      	if (name.equals("")) {
		      		out.println("<input type=\"button\" id=\"b" + sewa + counter +"\" value=\"signup\" onClick=\"return callServer('" + sewa + "', '" + counter + "' )\" />");
			    } else {
			    	out.println("<input type=\"button\" id=\"b" + sewa + counter +"\" value=\"UnSign\" onClick=\"return callServer('" + sewa + "', '" + counter + "' )\" />");
			    }
		      	out.println("<input type=\"hidden\" id=\"h" + sewa + counter + "\" value=\""+ name  + "\" />");
		      	out.println("</td></tr>");
			  }
		      
		  }
		  out.println("</table></div>");
		  
		  
	}

}


