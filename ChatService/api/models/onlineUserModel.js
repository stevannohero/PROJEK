'use strict';
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var OnlineUserSchema = new Schema({
  token: { type: String },
  username: { type: String },
  finding_order: { type: Boolean, default: false }
});

module.exports = mongoose.model('OnlineUsers', OnlineUserSchema);
