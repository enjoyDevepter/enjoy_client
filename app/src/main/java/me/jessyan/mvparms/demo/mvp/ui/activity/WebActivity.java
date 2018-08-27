package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerWebComponent;
import me.jessyan.mvparms.demo.di.module.WebModule;
import me.jessyan.mvparms.demo.mvp.contract.WebContract;
import me.jessyan.mvparms.demo.mvp.presenter.WebPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class WebActivity extends BaseActivity<WebPresenter> implements WebContract.View {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    public static final String KEY_FOR_WEB_TITLE = "KEY_FOR_WEB_TITLE";
    public static final String KEY_FOR_WEB_URL = "KEY_FOR_WEB_URL";

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerWebComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .webModule(new WebModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_web; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        Intent intent = getIntent();
        title.setText(intent.getStringExtra(KEY_FOR_WEB_TITLE));
        webView.loadUrl(intent.getStringExtra(KEY_FOR_WEB_URL));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
