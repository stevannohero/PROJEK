'use strict';
module.exports = function(app) {
  var chat = require('../controllers/chatController');
  var onlineUser = require('../controllers/onlineUserController');

  // chat routes
  app.route('/chat')
    .get(chat.get)
    .post(chat.send);

  // online users routes
  app.route('/user')
    .post(onlineUser.store)
    .put(onlineUser.updateFindingOrder);

    app.route('/user/:username')
      .delete(onlineUser.destroy);

  app.route('/selectDriver')
    .post(onlineUser.selectDriver);

  app.route('/completeOrder')
    .post(onlineUser.completeOrder);

    app.route('/driver')
      .get(onlineUser.getAvailableDrivers);
};
