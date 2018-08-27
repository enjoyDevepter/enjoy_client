package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerModifyUserInfoComponent;
import me.jessyan.mvparms.demo.di.module.ModifyUserInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.ModifyUserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommonUserInfo;
import me.jessyan.mvparms.demo.mvp.presenter.ModifyUserInfoPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CommonUserInfoAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ModifyUserInfoActivity extends BaseActivity<ModifyUserInfoPresenter> implements ModifyUserInfoContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.content)
    EditText contentET;
    @BindView(R.id.contentRV)
    RecyclerView contentRV;
    @BindView(R.id.submit)
    View submitV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    CommonUserInfoAdapter mAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerModifyUserInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .modifyUserInfoModule(new ModifyUserInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_modify_user_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        submitV.setOnClickListener(this);
        String type = getIntent().getStringExtra("type");
        titleTV.setText(getIntent().getStringExtra("title"));
        if ("age".equals(type) || "name".equals(type)) {

            if ("age".equals(type)) {
                contentET.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            contentET.setVisibility(View.VISIBLE);
            contentRV.setVisibility(View.INVISIBLE);
        } else {
            contentRV.setVisibility(View.VISIBLE);
            contentET.setVisibility(View.INVISIBLE);
            ArmsUtils.configRecyclerView(contentRV, mLayoutManager);
            contentRV.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.submit:
                String type = getIntent().getStringExtra("type");
                if ("age".equals(type)) {
                    provideCache().put("label", Integer.valueOf(contentET.getText().toString()));
                    provideCache().put("value", Integer.valueOf(contentET.getText().toString()));
                } else if ("name".equals(type)) {
                    provideCache().put("lable", contentET.getText().toString());
                    provideCache().put("value", contentET.getText().toString());
                } else if ("hobby".equals(type)) {
                    List<CommonUserInfo> userInfos = mAdapter.getInfos();
                    String label = (String) provideCache().get("label");
                    String value = (String) provideCache().get("value");
                    for (CommonUserInfo userInfo : userInfos) {
                        if (userInfo.isChoice()) {
                            if (ArmsUtils.isEmpty(label)) {
                                label = userInfo.getLabel();
                            } else {
                                label += "," + userInfo.getLabel();
                            }
                            if (ArmsUtils.isEmpty(value)) {
                                value = userInfo.getValue();
                            } else {
                                value += "," + userInfo.getValue();
                            }
                        }
                    }
                    provideCache().put("label", label);
                    provideCache().put("value", value);
                }
                mPresenter.modifyUserInfo();
                break;
        }
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        List<CommonUserInfo> userInfos = mAdapter.getInfos();
        if ("hobby".equals(getIntent().getStringExtra("type"))) {
            userInfos.get(position).setChoice(!userInfos.get(position).isChoice());
        } else {
            for (int i = 0; i < userInfos.size(); i++) {
                userInfos.get(i).setChoice(i == position ? true : false);
            }
            provideCache().put("label", userInfos.get(position).getLabel());
            provideCache().put("value", userInfos.get(position).getValue());

        }
        mAdapter.notifyDataSetChanged();
    }
}
