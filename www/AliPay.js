var exec = require('cordova/exec');

var AliPay = function () {
};

AliPay.prototype.alipay = function (info, successCallback, errorCallback) {
  exec(successCallback, errorCallback, "AliPay", "alipay", [info]);
};

module.exports = new AliPay();
