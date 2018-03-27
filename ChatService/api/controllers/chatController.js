'use strict';

var mongoose = require('mongoose'),
  request = require('request'),
  Chat = mongoose.model('Chats'),
  OnlineUser = mongoose.model('OnlineUsers');;

// Get chat history
exports.get = function(req, res) {
    Chat.findOne(
        {
            $or: [
                { members: { username1: req.query.sender, username2: req.query.receiver } },
                { members: { username1: req.query.receiver, username2: req.query.sender } }
            ]
        },
        {
            _id: 0, "messages.sender": 1, "messages.body": 1, "messages.date": 1
        },
        function(err, chat) {
            if (err) {
                res.status(400).send(err);
            }
            if (chat) {
                res.json(chat.messages.sort(function(a, b){
                    return a.date > b.date;
                }));
            } else {
                res.json([])
            }
        });
};

// Send chat message (use FCM)
exports.send = function(req, res) {
    OnlineUser.findOne( { username: req.body.receiver },
         function(err, onlineUser) {
             if (err) {
                 res.send(err);
             }
             if (onlineUser) {
                 var token = onlineUser.token;

                 var JSONMessage = {
                     "data": {
                        "type": "chat",
                        "message": req.body.message
                    },
                    "to": token
                    };

                 request.post({
                     url: "http://fcm.googleapis.com/fcm/send",
                     headers: {
                         "content-type": "application/json",
                         "Authorization": "key=AIzaSyAGKDzyepQO51hyaQrtKry7hGHu7aZdQP8"
                     },
                     json: true,
                     body: JSONMessage
                 }, function (error, response, body){
                     if (body && body.success == 1) {
                         Chat.update(
                             {
                                 $or: [
                                     { members: { username1: req.body.sender, username2: req.body.receiver } },
                                     { members: { username1: req.body.receiver, username2: req.body.sender } }
                                 ]
                             },
                             {
                                 members: { username1: req.body.sender, username2: req.body.receiver },
                                 $push: {
                                     messages: {
                                         sender: req.body.sender,
                                         body: req.body.message
                                     }
                                 }
                             },
                             { upsert: true },
                             function(err, chat) {
                                 if (err) {
                                     res.status(400).send(err);
                                 } else {
                                     res.json({ message: 'Message sent' });
                                 }
                             });
                     } else {
                         res.status(400).json({ error: 'Failed to send message' });
                     }
                 });
             } else {
                  res.status(400).json({ error: 'Failed to find user' });
             }
         });
};
