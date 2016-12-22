# novonity-plugin-alipay
Cordova plugin for alipay.

# Installation

```bash
$ ionic plugin add novonity-plugin-alipay
```

# How to use

Call the function below directly when using ionic1

```javascript
AliPay.alipay(params);
```

or in typescript when using ionic2

```typescript
declare var AliPay: any;
AliPay.alipay(params);
```

# Notice
The parameter "params" above contains all the data that payOrder function in the AlipaySdk needed.
