<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.google.appengine.api.datastore.*" %>
<%@ page import="langar.*" %>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> 
  </head>
  <body>
   <div>
    <form>
    <%
        List<Entity> assignments = (List<Entity>)request.getAttribute("assignments");
        String currentuser = (String)request.getAttribute("currentuser");
        String currentemail = (String)request.getAttribute("currentemail");
    
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Chapattis", "Chappatis:"); 
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Daal", "Dal:") ;
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Sabji", "Sabji:"); 
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Rice", "Rice:") ;
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Raita", "Raita:"); 
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Salad", "Salad:") ;
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "Prashad", "Prashad:"); 
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "PaperGoods", "Paper Goods:"); 
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "DewanSponsor", "Dewan Sponsor:"); 
  		Utils.createUserSnippet(out, currentuser, currentemail, assignments, "SweetDish", "Sweet Dish:") ;
  
  %>
  </form>
  </div>

<script>
function callServer(sewa, id) {
  
  var elem_id=sewa + id;
  var elem = document.getElementById(elem_id);
  var oldvalue = elem.value;
  
  var action = "signup";
  if (elem.checked) {
	  action = "signup";
	  elem.checked = false;
  } else {
	  action = "delete";
	  elem.checked = true;
  }
  //alert("callServer id="+id + " action=" + action);
  elem.disabled = true;
  	
  $.ajax({
	  type: "POST",
	  url: "/update",
	  data: { "sewa": sewa, "prevName": oldvalue, "counter": id, "action": action },
	  dataType: "json"
	}).done(function(data) {
	  elem.disabled = false;
	  if (data.rc === "success") {
		  if (action === "signup") elem.checked = true;
		  else elem.checked = false;
		  $("." + sewa).html(data.text);
		  elem.value = data.value;
		  //alert(data.text);
	  } else {
		  alert(data.msg);
		  location.reload();
	  }
  }).error(function(data) {
	  alert("Oops! " + data);
	  location.reload();
  });
}

</script>
</body>
</html>
