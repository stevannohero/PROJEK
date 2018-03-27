var express = require('express'),
  app = express(),
  port = 8003,
  mongoose = require('mongoose'),
  Chat = require('./api/models/chatModel'), //created model loading here
  OnlineUser = require('./api/models/onlineUserModel'),
  bodyParser = require('body-parser'),
  cors = require('cors');

// mongoose instance connection url connection
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/chatService');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(cors());

var routes = require('./api/routes/routes'); //importing route
routes(app); //register the route

app.listen(port);

app.use(function(req, res) {
  res.status(404).send({url: req.originalUrl + ' not found'})
});

console.log('Chat Service started on: ' + port);
