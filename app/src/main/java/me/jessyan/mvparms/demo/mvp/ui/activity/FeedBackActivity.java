package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerFeedBackComponent;
import me.jessyan.mvparms.demo.di.module.FeedBackModule;
import me.jessyan.mvparms.demo.mvp.contract.FeedBackContract;
import me.jessyan.mvparms.demo.mvp.presenter.FeedBackPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements FeedBackContract.View {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.text_count)
    TextView text_count;

    @BindView(R.id.commit)
    View commit;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFeedBackComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .feedBackModule(new FeedBackModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_feed_back; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        title.setText("意见反馈");
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_count.setText("" + s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_name.getText() + "";
                if (TextUtils.isEmpty(s)) {
                    ArmsUtils.makeText(ArmsUtils.getContext(), "请输入内容后提交");
                    return;
                }
                mPresenter.feedback(s);
            }
        });
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

    public void showOk() {
        ArmsUtils.makeText(this, "提交成功");
        killMyself();
    }
}
