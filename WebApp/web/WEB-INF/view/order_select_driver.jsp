<%--
  Created by IntelliJ IDEA.
  User: Jordhy
  Date: 11/6/2017
  Time: 7:16 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.sceptre.projek.webapp.model.User" %>
<%@ page import="java.util.List" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! String currentPage = "order"; %>
<%! String currentSubPage = "select_driver"; %>
<% List<User> preferredDrivers = (List<User>) request.getAttribute("preferred_drivers"); %>
<% List<User> otherDrivers = (List<User>) request.getAttribute("other_drivers"); %>

<html>
<head>
    <meta charset="utf-8">
    <title>Order - Select Driver - PR-OJEK</title>
    <link href="../../assets/css/style.css" rel="stylesheet">
    <link rel="shortcut icon" href="../../assets/favicon.png" type="image/x-icon">
    <link rel="icon" href="../../assets/favicon.png" type="image/x-icon">
</head>

<%@ include file="header.jsp" %>

<body>
    <div class="container dark-grey">
        <%@ include file="order_header.jsp" %>
        <div class="select-driver-border">
            <h1>Preferred Drivers:</h1>
            <% if (!preferredDrivers.isEmpty()) {
                for (User preferredDriver : preferredDrivers) { %>
                <table class="table-select-driver">
                    <tr>
                        <td>
                            <img class="img-driver-pic" src="<% out.print(preferredDriver.getProfilePicture()); %>" alt="DRIVER PICTURE">
                        </td>
                        <td>
                            <p class="driver-name"> <% out.print(preferredDriver.getName()); %> </p>
                            <p class="star"><span class="orange">&#10025; <% out.print(String.format("%.1f", preferredDriver.getRatings())); %> </span> (<% out.print(preferredDriver.getVotes()); %>
                        <% if (preferredDriver.getVotes()> 1) { %>
                                 votes)
                        <% } else { %>
                                 vote)
                        <% } %>
                            </p>
                            <form action="chat_driver" method="get">
                                <input type="hidden" name="picking_point" value="<% out.print(request.getParameter("picking_point")); %>">
                                <input type="hidden" name="destination" value="<% out.print(request.getParameter("destination")); %>">
                                <input type="hidden" name="preferred_driver" value="<% out.print(request.getParameter("preferred_driver")); %>">
                                <input type="hidden" name="driver_id" value="<% out.print(preferredDriver.getId()); %>">
                                <input class="button-i-choose-you right" type="submit" value="I CHOOSE YOU!!">
                            </form>
                        </td>
                    </tr>
                </table>
            <%  }
            } else { %>
                <p class="align-center nothing-to-display-margin">Nothing to display :(</p>
            <% } %>
        </div>

        <div class="select-driver-border dark-grey">
            <h1>Other Drivers:</h1>
            <% if (!otherDrivers.isEmpty()) {
                for (User otherDriver : otherDrivers) { %>
            <table class="table-select-driver">
                <tr>
                    <td>
                        <img class="img-driver-pic" src="<% out.print(otherDriver.getProfilePicture()); %>" alt="DRIVER PICTURE">
                    </td>
                    <td>
                        <p class="driver-name"> <% out.print(otherDriver.getName()); %> </p>
                        <p class="star"><span class="orange">&#10025; <% out.print(String.format("%.1f", otherDriver.getRatings())); %> </span> (<% out.print(otherDriver.getVotes()); %>
                            <% if (otherDriver.getVotes()> 1) { %>
                            votes)
                            <% } else { %>
                            vote)
                            <% } %>
                        </p>
                        <form action="chat_driver" method="get">
                            <input type="hidden" name="picking_point" value="<% out.print(request.getParameter("picking_point")); %>">
                            <input type="hidden" name="destination" value="<% out.print(request.getParameter("destination")); %>">
                            <input type="hidden" name="preferred_driver" value="<% out.print(request.getParameter("preferred_driver")); %>">
                            <input id="driver_id" type="hidden" name="driver_id" value="<% out.print(otherDriver.getId()); %>">
                            <input class="button-i-choose-you right" type="submit" value="I CHOOSE YOU!!">
                        </form>
                    </td>
                </tr>
            </table>
            <%  }
            } else { %>
            <p class="align-center nothing-to-display-margin">Nothing to display :(</p>
            <% } %>
        </div>
    </div>
</body>
</html>
