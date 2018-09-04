package cn.ehanmy.pay;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.ehanmy.alipayapi.Alipay;
import cn.ehanmy.wxapi.WeiXinPay;

public class PayManager {

    private static PayManager mPay;
    private Activity activity;

    private PayManager(Activity activity) {
        this.activity = activity;
    }

    public static PayManager getInstace(Activity activity) {
        if (mPay == null) {
            synchronized (PayManager.class) {
                if (mPay == null) {
                    mPay = new PayManager(activity);
                }
            }
        }
        return mPay;
    }

    public void toPay(PayMode payMode, String payParameters, PayListener listener) {
        if (payMode.name().equalsIgnoreCase(PayMode.WXPAY.name())) {
            toWxPay(payParameters, listener);
        } else if (payMode.name().equalsIgnoreCase(PayMode.ALIPAY.name())) {
            toAliPay(activity, payParameters, listener);
        }
    }

    public void toWxPay(String payParameters, PayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            if (TextUtils.isEmpty(param.optString("appId")) || TextUtils.isEmpty(param.optString("partnerId"))
                    || TextUtils.isEmpty(param.optString("prepayId")) || TextUtils.isEmpty(param.optString("nonceStr"))
                    || TextUtils.isEmpty(param.optString("timeStamp")) || TextUtils.isEmpty(param.optString("sign"))) {
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }
            WeiXinPay.getInstance().startWXPay(param.optString("appId"),
                    param.optString("partnerId"), param.optString("prepayId"),
                    param.optString("nonceStr"), param.optString("timeStamp"),
                    param.optString("sign"), listener);

        } else {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public void toWxPay(String appId, String partnerId, String prepayId,
                        String nonceStr, String timeStamp, String sign, PayListener listener) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(partnerId)
                || TextUtils.isEmpty(prepayId) || TextUtils.isEmpty(nonceStr)
                || TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(sign)) {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
            return;
        }
        WeiXinPay.getInstance().startWXPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, listener);
    }

    public void toAliPay(Activity activity, String payParameters, PayListener listener) {
        if (payParameters != null) {
            if (listener != null) {
                Alipay.getInstance().startAliPay(activity, payParameters, listener);
            }
        } else {
            if (listener != null) {
                listener.onPayError(Alipay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }

    public enum PayMode {
        WXPAY, ALIPAY
    }

    public interface PayListener {
        //支付成功
        void onPaySuccess(Map<String, String> rawResult);

        //支付失败
        void onPayError(int error_code, String message);

        //支付取消
        void onPayCancel();
    }
}

