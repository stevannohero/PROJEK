<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%! String currentPage = "order"; %>
<html>
<head>
    <meta charset="utf-8">
    <title>Driver - Order - PR-OJEK</title>
    <link href="../../assets/css/style.css" rel="stylesheet">
    <link rel="shortcut icon" href="../../assets/favicon.png" type="image/x-icon">
    <link rel="icon" href="../../assets/favicon.png" type="image/x-icon">
    <script src="../../assets/js/angular/angular.min.js"></script>
    <script src="https://www.gstatic.com/firebasejs/4.6.2/firebase.js"></script>
    <script src="https://cdn.rawgit.com/Luegg/angularjs-scroll-glue/master/src/scrollglue.js"></script>
</head>

<%@ include file="header.jsp" %>

<body ng-app="Pr-Ojek" ng-controller="FindOrderController">

    <input type="hidden" ng-model="username" ng-init="username ='<%out.print(CookieManager.getUsername(request));%>'">

    <div class="container">
        <h2>Looking for an Order</h2>
        <div ng-show="status === 'idle'">
            <form ng-submit="findOrder()">
                <input class="green-button" type="submit" value="FIND ORDER">
            </form>
        </div>

        <div ng-hide="status !== 'finding_order'">
            <h4 class="find-order">Finding Order....</h4>
            <form ng-submit="cancelOrder()">
                <input class="red-button" type="submit" value="CANCEL">
            </form>
        </div>

        <div ng-show="status === 'got_order'">
            <h4 class="got-order">Got an Order!</h4>
            <p class="username-order"><span class="bold">{{customer}}</span></p>
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
        </div>
    </div>
</body>

<script src="../../assets/js/angular/app.module.js"></script>
<script src="../../assets/js/firebase.config.js"></script>
<script src="../../assets/js/angular/controllers/FindOrderController.js"></script>
</html>