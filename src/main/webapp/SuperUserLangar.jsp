<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.google.appengine.api.datastore.*" %>
<%@ page import="langar.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>

<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/themes/smoothness/jquery-ui.css">
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> 
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
  </head>
  <body>
   <div>
    <form  action="/clearall" method="get">
    <%
        Date lastclearancedate = (Date)request.getAttribute("lastclearancedate");
        String mydate = "";
        if (lastclearancedate != null) {
                TimeZone timeZone = TimeZone.getTimeZone("America/Chicago");
                DateFormat requiredFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm");
                requiredFormat.setTimeZone(timeZone);
                mydate = requiredFormat.format(lastclearancedate);
        }

    %>
       Last Clearance date for Langar is <%= mydate %>
       <input id="clearall" name="clearapp" type="submit" value="Clear All Langar Assignments"/>
       <br/><br/>
    <%
        List<Entity> assignments = (List<Entity>)request.getAttribute("assignments");
        String currentuser = (String)request.getAttribute("currentuser");
        String currentemail = (String)request.getAttribute("currentemail");
    
  		Utils.createSuperUserSnippet(out,  assignments, "Chapattis", "Chappatis:"); 
  		Utils.createSuperUserSnippet(out,  assignments, "Daal", "Dal:") ;
  		Utils.createSuperUserSnippet(out,  assignments, "Sabji", "Sabji:"); 
  		Utils.createSuperUserSnippet(out,  assignments, "Rice", "Rice:") ;
  		Utils.createSuperUserSnippet(out,  assignments, "Raita", "Raita:"); 
  		Utils.createSuperUserSnippet(out,  assignments, "Salad", "Salad:") ;
  		Utils.createSuperUserSnippet(out,  assignments, "Prashad", "Prashad:"); 
  		Utils.createSuperUserSnippet(out,  assignments, "PaperGoods", "Paper Goods:"); 
  		Utils.createSuperUserSnippet(out,  assignments, "DewanSponsor", "Dewan Sponsor:"); 
  		Utils.createSuperUserSnippet(out,  assignments, "SweetDish", "Sweet Dish:") ;
  
  %>
      <br/><br/>
      <input id="clearall" name="clearapp" type="submit" value="Clear All Langar Assignments"/>
  </form>
  </div>

<script>
function callServer(sewa, id) {
  
  var elem_id=sewa + id;
  var belem = document.getElementById("b" + elem_id);
  var ielem = document.getElementById("i" + elem_id);
  var helem = document.getElementById("h" + elem_id);
  var newValue = ielem.value; 
  var oldValue = helem.value;
  var action = "signup";
  if (belem.value === "UnSign")
	  action = "delete";
  
  //signup without any data means signup for super user
  
  //alert("callServer id="+id + " action=" + action);
  belem.disabled = true;
  $.ajax({
	  type: "POST",
	  url: "/update",
	  data: { "sewa": sewa, "prevName": oldValue, "newName": newValue, "counter": id, "action": action },
	  dataType: "json"
	}).done(function(data) {
	  belem.disabled = false;
	  helem.value = data.name;
	  ielem.value = data.name;
	  if (data.rc === "success") {
		  if (action === "signup") {
			  belem.value = "UnSign";
			  ielem.disabled = true;
		  } else {
			  belem.value = "signup";
			  ielem.disabled = false;
		  }
	  } else {
		  alert(data.msg);
		  location.reload();
	  }
  }).error(function(data) {
	  alert("Oops! " + data);
	  location.reload();
  });
}

var availableNames = [
 <%
 	List<Entity> users = (List<Entity>)request.getAttribute("users");
    for (Entity user: users) {
    	
 %>
 
                      "<%= user.getProperty(UserEntity.NAME)%>",
                      
 <%
    }
 %>
    
                      ];

$( "input[type=text]" ).autocomplete({
    source: availableNames
  });
</script>
</body>
</html>
