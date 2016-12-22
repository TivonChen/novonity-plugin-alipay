package com.novonity.plugin.alipay;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import android.widget.Toast;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

public class AliPay extends CordovaPlugin {

	public String resultStatus;
    public String result;
    public String memo;
    public static final int SDK_PAY_FLAG = 1;

    public static CallbackContext currentCallbackContext;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				payResult((String) msg.obj);
				System.out.println("resultStatus:" + resultStatus);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(cordova.getActivity(), "支付成功",
							Toast.LENGTH_SHORT).show();
							//调用js方法通知前端支付成功，刷新页面
							currentCallbackContext.success();
				}else {
                    if(TextUtils.equals(resultStatus, "8000")) {
                        currentCallbackContext.error("8000");
                        Toast.makeText(cordova.getActivity(), "支付结果确认中", Toast.LENGTH_LONG).show();
                    }else if(TextUtils.equals(resultStatus, "6001")){
                        currentCallbackContext.error("6001");
                        Toast.makeText(cordova.getActivity(), "支付取消", Toast.LENGTH_LONG).show();
                    }else{
                        currentCallbackContext.error("-1");
                        Toast.makeText(cordova.getActivity(), "支付失败", Toast.LENGTH_LONG).show();
                    }
                }
				break;
			}
			default:
				break;
			}
		};
	};

	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if(action.equals("alipay")) {
			payRun(args.getString(0));
            currentCallbackContext = callbackContext;
            return true;
		}
		return false;
	}

	private void payRun(final String payInfo){
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(cordova.getActivity());
                    String result = alipay.pay(payInfo);
                    Message message = new Message();
                    message.what = SDK_PAY_FLAG;
                    message.obj = result;
                    mHandler.sendMessage(message);
                }
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }

	public void payResult(String rawResult) {

            if (TextUtils.isEmpty(rawResult))
                return;

            String[] resultParams = rawResult.split(";");
            for (String resultParam : resultParams) {
                if (resultParam.startsWith("resultStatus")) {
                    resultStatus = gatValue(resultParam, "resultStatus");
                }
                if (resultParam.startsWith("result")) {
                    result = gatValue(resultParam, "result");
                }
                if (resultParam.startsWith("memo")) {
                    memo = gatValue(resultParam, "memo");
                }
            }
        }

        private String gatValue(String content, String key) {
                String prefix = key + "={";
                return content.substring(content.indexOf(prefix) + prefix.length(),
                        content.lastIndexOf("}"));
            }
}
