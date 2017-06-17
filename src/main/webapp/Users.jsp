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
  <div class="message">
  <% 
     String msg = (String)request.getAttribute("message"); 
     if (msg != null) {
       out.println(msg);
     }
  %>
  </div>
  <p>
  <div class="form">
    <form action="/create" method="post">
      <div>User Name: <input type="text" id="UserName" name="UserName" ></input></div>
      <div>User Email: <input type="text" id="UserEmail" name="UserEmail"></input></div>
      <% 
         String superUS = (String) request.getAttribute("superuser");
         boolean superuser = false;
         if (superUS != null) {
           superuser = Boolean.parseBoolean(superUS);
         }
         if (superuser) {
      %>
        <div>Super User? <input type="checkbox" id="SuperUser" name="SuperUser" value="yes"></input></div>
        <div>Allow UserManagement? <input type="checkbox" id="AllowUserMgmt" name="AllowUserMgmt" value="yes"></input></div>
        <div>Delete Entry? <input type="checkbox" id="Delete" name="Delete" value="yes"></input></div>
      <%
      }
      %>
      <div><input type="submit" value="Create User" /></div>
    </form>
  </div>

  <div class="listUser">
  <b>List of Users:
  <table id="users">
   <tr>
     <th>Name</th>
     <th>Email</th>
     <th>Super User</th>
     <th>Allow User Mgmt</th>
   </tr>
  
  <%
        List<Entity> users = (List<Entity>)request.getAttribute("users");
  		//langar.createLangar();
        int count =1;
        for (Entity entity : users) {
  %>
      <tr>
        <td><a href="#" onclick="return updateUser(<%= count %>)"> <%= entity.getProperty(UserEntity.NAME)%> </a></td>
        <td><%= entity.getProperty(UserEntity.EMAIL)%> </td>
        <td><%= entity.getProperty(UserEntity.SUPERUSER) %> </td>
        <td><%= entity.getProperty(UserEntity.ALLOWUSERMGMT) %> </td>
      </tr>
  <% count++; }
  %>
  </div>
<script>
function updateUser(row) {
  var row = document.getElementById("users").rows[row];
  var nameE = document.getElementById("UserName");
  nameE.value = row.cells[0].innerText;
  var emailE = document.getElementById("UserEmail");
  emailE.value = row.cells[1].innerText;
  var superuserE = document.getElementById("SuperUser");
  if (superuserE) {
     if (row.cells[2].innerText == 'yes') {
         superuserE.checked = true;
     } else {
         superuserE.checked = false;
     }
  }
  var allowusermgmtE = document.getElementById("AllowUserMgmt");
  if (allowusermgmtE) {
     if (row.cells[3].innerText == 'yes') {
         allowusermgmtE.checked = true;
     } else {
         allowusermgmtE.checked = false;
     }
  }

  return false;
}
</script>
  </body>
</html>
