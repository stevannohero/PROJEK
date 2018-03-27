(function() {

    'use strict';

    angular
        .module('Pr-Ojek')
        .controller('FindOrderController', FindOrderController);

    function FindOrderController($scope, $http) {
        $scope.status = 'idle';
        $scope.chats = [];
        $scope.message = '';
        $scope.glued = true;
        var messaging = firebase.messaging();
        messaging.requestPermission()
            .then(function() {
                console.log('Notification permission granted.');
                messaging.getToken()
                    .then(function(currentToken) {
                        if (currentToken) {
                            console.log(currentToken);
                            $scope.token = currentToken;
                            $scope.$apply();
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
            if (newValue) {
                $scope.$watch('token', function (newValue) {
                    if (newValue) {
                        var user = {
                            token: $scope.token,
                            username: $scope.username
                        };
                        $http.post('http://localhost:8003/user', user).then(function (response) {
                            console.log(response.data);
                        }).catch(function (response) {
                            console.log(response);
                        });
                    }
                });
            }
        });

        $scope.findOrder = function() {
            var user = {
                token: $scope.token,
                username: $scope.username
            };
            $http.post('http://localhost:8003/user', user).then(function (response) {
                console.log(response.data);
                var status = {
                    token: $scope.token,
                    username: $scope.username,
                    finding_order: true
                };
                $http.put('http://localhost:8003/user', status).then(function(response) {
                    console.log(response.data);
                    $scope.status = 'finding_order';
                }).catch(function(response) {
                    console.log(response);
                });
            }).catch(function (response) {
                console.log(response);
            });
        };

        $scope.cancelOrder = function() {
            $http.delete('http://localhost:8003/user/' + $scope.username).then(function(response) {
                console.log(response.data);
                $scope.status = 'idle';
            }).catch(function(response) {
                console.log(response);
            });
        };

        $scope.sendMessage = function() {
            if ($scope.message !== '') {
                var message = {
                    sender: $scope.username,
                    receiver: $scope.customer,
                    message: $scope.message
                };
                $http.post('http://localhost:8003/chat', message).then(function (response) {
                    console.log(response.data);
                    var chat = {
                        sender: $scope.username,
                        body: $scope.message
                    };
                    $scope.chats.push(chat);
                    $scope.message = '';
                }).catch(function (response) {
                    console.log(response);
                });
            }
        };

        messaging.onMessage(function(payload) {
            console.log("Message received. ", payload);
            var data = payload.data;
            if (data.type === 'init' && $scope.status === 'finding_order') {
                var status = { token: $scope.token,
                    username: $scope.username,
                    finding_order: false
                };
                $http.put('http://localhost:8003/user', status).then(function(response) {
                   console.log(response.data);
                }).catch(function(response) {
                    console.log(response);
                });
                $scope.status = 'got_order';
                $scope.customer = data.customer;
                $scope.$apply();
                $http.get('http://localhost:8003/chat?sender=' + $scope.username + '&receiver=' + $scope.customer).then(function(response) {
                    console.log(response.data);
                    $scope.chats = response.data;
                }).catch(function(response) {
                    console.log(response);
                });
            } else if (data.type === 'end' && $scope.status === 'got_order' && data.customer === $scope.customer) {
                $scope.status = 'idle';
                $scope.customer = '';
                $scope.chats = [];
                $scope.$apply();

            } else {
                var chat = {
                    sender: $scope.customer,
                    body: data.message
                };
                $scope.chats.push(chat);
                $scope.$apply();
            }
        });

    }
})();