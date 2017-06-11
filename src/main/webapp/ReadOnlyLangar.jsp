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
  </head>
 
  <body>
   <div>
             <p align="right">Last Modified: <%= request.getAttribute("lastmodified") %></p>
             <table border="1" cellpadding="0" cellspacing="0" width="100%" style="border:1px solid #000000" >
            <tbody>
              <tr>

                <th  valign="top"  width="275px">
                    <p><strong>Item </strong></p>                 </th>
                <th  valign="top" style="padding-left:0px" width="613px">
                    <p ><strong>Sevadar </strong></p>                 </th>
              </tr>
<%
   List<Entity> assignments = (List<Entity>)request.getAttribute("assignments");

	Utils.createReadOnlySnippet(out, assignments, "Chapattis", "25 Chappatis"); 
	Utils.createReadOnlySnippet(out, assignments, "Daal", "Daal (2 pounds uncooked daal)") ;
	Utils.createReadOnlySnippet(out, assignments, "Sabji", "Sabji (One 5 - 6 quart pan full)"); 
	Utils.createReadOnlySnippet(out, assignments, "Rice", "Rice - 6 cups") ;
	Utils.createReadOnlySnippet(out, assignments, "Raita", "Raita / Yogurt (Seven 32 oz jars)"); 
	Utils.createReadOnlySnippet(out, assignments, "Salad", "Salad - optional") ;
	Utils.createReadOnlySnippet(out, assignments, "Prashad", "PRASHAD (about 8 cups of Atta)"); 
	Utils.createReadOnlySnippet(out, assignments, "PaperGoods", "Paper Goods (120 compartment plates - rectangular style; 120 water glasses; 500 napkins; 120 bowls;"); 
	Utils.createReadOnlySnippet(out, assignments, "DewanSponsor", "Dewan Sponsor"); 
	Utils.createReadOnlySnippet(out, assignments, "SweetDish", "Optional Sweet dish - can be any dish the sewadar chooses to make") ;
%>  
  </tbody>
  </table>
  </div>
  </body>
  </html>
