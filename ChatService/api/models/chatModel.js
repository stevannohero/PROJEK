'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var ChatSchema = new Schema({
  members: {
      username1: { type: String },
      username2: { type: String }
  },
  messages: [{
      sender: String,
      body: String,
      date: { type: Date, default: Date.now }
  }]
});

module.exports = mongoose.model('Chats', ChatSchema);
