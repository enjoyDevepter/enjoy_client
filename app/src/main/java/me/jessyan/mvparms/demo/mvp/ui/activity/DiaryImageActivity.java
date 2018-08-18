package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDiaryImageComponent;
import me.jessyan.mvparms.demo.di.module.DiaryImageModule;
import me.jessyan.mvparms.demo.mvp.contract.DiaryImageContract;
import me.jessyan.mvparms.demo.mvp.presenter.DiaryImagePresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryImageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DiaryImageActivity extends BaseActivity<DiaryImagePresenter> implements DiaryImageContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.images)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    DiaryImageAdapter mAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerDiaryImageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .diaryImageModule(new DiaryImageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_diary_image; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("日记图片");
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.plus_width), ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.plus_width)));
        mAdapter.setOnItemClickListener(this);
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
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
    }
}
