<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> 
  </head>
  <body>
    <form action="/clearall" method="post">
      <p>Are you sure, you want to clear all langar assignments? This action cannot be undone.
      <br/><br/>
      <input id="response" name="response" type="submit" value="No"/>
      <input id="response" name="response" type="submit" value="Yes"/>
    </form>

</body>
</html>
