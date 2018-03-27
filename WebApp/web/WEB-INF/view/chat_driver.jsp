<%@ page import="com.sceptre.projek.webapp.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! String currentPage = "order"; %>
<%! String currentSubPage = "chat_driver"; %>
<% User driver = (User) request.getAttribute("driver"); %>

<html>
<head>
    <meta charset="utf-8">
    <title>Chat Driver - PR-OJEK</title>
    <link href="../../assets/css/style.css" rel="stylesheet">
    <link rel="shortcut icon" href="../../assets/favicon.png" type="image/x-icon">
    <link rel="icon" href="../../assets/favicon.png" type="image/x-icon">
    <script src="../../assets/js/angular/angular.min.js"></script>
    <script src="https://www.gstatic.com/firebasejs/4.6.2/firebase.js"></script>
    <script src="https://cdn.rawgit.com/Luegg/angularjs-scroll-glue/master/src/scrollglue.js"></script>
</head>

<%@ include file="header.jsp" %>

<body ng-app="Pr-Ojek" ng-controller="ChatDriverController">
    <div class="container">
        <%@ include file="order_header.jsp" %>

        <input type="hidden" ng-model="username" ng-init="username ='<%out.print(CookieManager.getUsername(request));%>'">
        <input type="hidden" ng-model="driverUsername" ng-init="driverUsername ='<%out.print(driver.getUsername());%>'">

        <div class="chat-container">
            <div class="chatbox" scroll-glue="glued">
                <div ng-repeat="chat in chats">
                    <div ng-if="chat.sender === username" class="chat-right">
                        {{chat.body}}
                    </div>
                    <div ng-if="chat.sender !== username" class="chat-left">
                        {{chat.body}}
                    </div>
                </div>
            </div>
            <div class="sendbox">
                <form ng-submit="sendMessage()">
                    <input id="message" type="text" name="message" ng-model="message" placeholder="Masukkan pesan Anda">
                    <input type="submit" class="button-send" value="Kirim">
                </form>
            </div>
        </div>

        <form action="complete_order" method="get">
            <input type="hidden" name="picking_point" value="<% out.print(request.getParameter("picking_point")); %>">
            <input type="hidden" name="destination" value="<% out.print(request.getParameter("destination")); %>">
            <input type="hidden" name="preferred_driver" value="<% out.print(request.getParameter("preferred_driver")); %>">
            <input type="hidden" name="driver_id" value="<% out.print(request.getParameter("driver_id")); %>">
            <input class="red-button" type="submit" value="CLOSE">
        </form>
    </div>
</body>

<script src="../../assets/js/angular/app.module.js"></script>
<script src="../../assets/js/firebase.config.js"></script>
<script src="../../assets/js/angular/controllers/ChatDriverController.js"></script>
</html>