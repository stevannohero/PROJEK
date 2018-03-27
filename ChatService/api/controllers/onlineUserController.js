'use strict';

var mongoose = require('mongoose'),
  request = require('request'),
  OnlineUser = mongoose.model('OnlineUsers');

// Update finding order status
exports.updateFindingOrder = function(req, res) {
	OnlineUser.findOneAndUpdate({ username: req.body.username },
		{
            token: req.body.token,
    		username: req.body.username,
            finding_order: req.body.finding_order
        },
        { upsert: true},
		function(err, onlineUser) {
			if (err) {
				res.status(400).send(err);
			} else {
				res.json({ message: 'Finding order status updated' });
			}
		});
};

// Save online user
exports.store = function(req, res) {
    OnlineUser.findOneAndUpdate(
        {
            token: req.body.token,
    		username: req.body.username
        },
        {
            token: req.body.token,
    		username: req.body.username,
            finding_order: false
        },
        { upsert: true},
        function(err, onlineUser) {
    		if (err) {
    			res.status(400).send(err);
    		} else {
                res.json({ message: 'User saved' });
            }
    	});
};

// Delete online user
exports.destroy = function(req, res) {
	OnlineUser.remove({ username: req.params.username },
		function(err, onlineUser) {
			if (err) {
				res.status(400).send(err);
			} else {
				res.json({ message: 'User deleted' });
			}
		});
};

exports.selectDriver = function(req, res) {
    OnlineUser.findOne( { username: req.body.driverUsername },
         function(err, driver) {
             if (err) {
                 res.send(err);
             }
             if (driver) {
                 var token = driver.token;

                 var JSONMessage = {
                     "data": {
                        "type": "init",
                        "customer": req.body.customer
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
                         res.json({ message: 'Message sent' });
                     } else {
                         res.status(400).json({ error: 'Failed to send message' });
                     }
                 });
             } else {
                 res.status(400).json({ error: 'Failed to find user' });
             }
         });
};

exports.completeOrder = function(req, res) {
    OnlineUser.findOne( { username: req.body.driverUsername },
         function(err, driver) {
             if (err) {
                 res.send(err);
             }
             if (driver) {
                 var token = driver.token;

                 var JSONMessage = {
                     "data": {
                        "type": "end",
                        "customer": req.body.customer
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
                         res.json({ message: 'Message sent' });
                     } else {
                         res.status(400).json({ error: 'Failed to send message' });
                     }
                 });
             } else {
                 res.status(400).json({ error: 'Failed to find user' });
             }
          });
};

// Filter driver
exports.getAvailableDrivers = function(req, res) {
	OnlineUser.find(
	{
		"finding_order" : true
	},
    {
        _id: 0, "username": 1
    },
	function(err, drivers) {
		if (err) {
			res.status(400).send(err);
		} else {
			res.json(drivers);
		}
	});
};
