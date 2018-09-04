package cn.ehanmy.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (WeiXinPay.getInstance() != null) {
            WeiXinPay.getInstance().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (WeiXinPay.getInstance() != null) {
            WeiXinPay.getInstance().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //4、支付结果回调 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (WeiXinPay.getInstance() != null) {
                if (baseResp.errStr != null) {
                    Log.e("weiXinPay", "errStr=" + baseResp.errStr);
                }
                WeiXinPay.getInstance().onResp(baseResp.errCode, baseResp.errStr);
                finish();
            }
        }
    }


}
