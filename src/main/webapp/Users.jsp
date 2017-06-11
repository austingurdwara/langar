<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.google.appengine.api.datastore.*" %>
<%@ page import="langar.*" %>
<html>

  <body>
  <div class="message">
  <p><%=request.getAttribute("message")%>
  </div>
  <div class="form">
  comments:
  
  <%
        List<Entity> users = (List<Entity>)request.getAttribute("users");
  		//langar.createLangar();
        for (Entity entity : users) {
  %>
  <br><%= entity.getProperty(UserEntity.NAME)%> <%= entity.getProperty(UserEntity.EMAIL)%> <%= entity.getProperty(UserEntity.SUPERUSER) %> 
  <% }
  %>
  </div>
  <div class="form">
    <form action="/create" method="post">
      <div>User Name: <input type="text" name="UserName" ></input></div>
      <div>User Email: <input type="text" name="UserEmail"></input></div>
      <div>Super User? <input type="checkbox" name="SuperUser" value="yes"></input></div>
      <div>Delete Entry? <input type="checkbox" name="Delete" value="yes"></input></div>
      <div><input type="submit" value="Create User" /></div>
    </form>
  </div>
  </body>
</html>
