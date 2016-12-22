var exec = require('cordova/exec');

var AliPay = function () {
};

AliPay.prototype.pay = function (info, successCallback, errorCallback) {
  exec(successCallback, errorCallback, "AliPay", "pay", [info]);
};

module.exports = new AliPay();
