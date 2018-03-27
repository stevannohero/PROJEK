(function() {

    'use strict';

    angular
        .module('Pr-Ojek')
        .controller('ChatDriverController', ChatDriverController);

    function ChatDriverController($scope, $http) {
        $scope.chats = [];
        $scope.token = '';
        $scope.message = '';
        $scope.glued = true;
        var messaging = firebase.messaging();
        messaging.requestPermission()
            .then(function() {
                console.log('Notification permission granted.');
                messaging.getToken()
                    .then(function(currentToken) {
                        if (currentToken) {
                            $scope.token = currentToken;
                            var user = {
                                token: $scope.token,
                                username: $scope.username
                            };
                            $http.post('http://localhost:8003/user', user).then(function(response) {
                                console.log(response.data);
                            }).catch(function(response) {
                                console.log(response);
                            });
                        } else {
                            // Show permission request.
                            console.log('No Instance ID token available. Request permission to generate one.');
                        }
                    })
                    .catch(function(err) {
                        console.log('An error occurred while retrieving token. ', err);
                    });
            })
            .catch(function(err) {
                console.log('Unable to get permission to notify.', err);
            });

        $scope.$watch('username',function(newValue) {
            if(newValue) {
                $scope.$watch('driverUsername',function(newValue) {
                    if(newValue) {
                        var driver = {
                            driverUsername: $scope.driverUsername,
                            customer: $scope.username
                        };

                        $http.post('http://localhost:8003/selectDriver', driver).then(function(response) {
                            console.log(response.data);
                        }).catch(function(response) {
                            console.log(response);
                        });

                        $http.get('http://localhost:8003/chat?sender=' + $scope.username + '&receiver=' + $scope.driverUsername).then(function(response) {
                            console.log(response.data);
                            $scope.chats = response.data;
                        }).catch(function(response) {
                            console.log(response);
                        });
                    }
                });
            }
        });

        $scope.sendMessage = function() {
            if ($scope.message !== '') {
                var message = {
                    sender: $scope.username,
                    receiver: $scope.driverUsername,
                    message: $scope.message
                };
                $http.post('http://localhost:8003/chat', message).then(function(response) {
                    console.log(response.data);
                    var chat = {
                        sender: $scope.username,
                        body: $scope.message
                    };
                    $scope.chats.push(chat);
                    $scope.message = '';
                }).catch(function(response) {
                    console.log(response);
                });
            }
        };

        messaging.onMessage(function(payload) {
            console.log("Message received. ", payload);
            var data = payload.data;
            var chat = {
                sender: $scope.driverUsername,
                body: data.message
            };
            $scope.chats.push(chat);
            $scope.$apply();
        });

    }
})();