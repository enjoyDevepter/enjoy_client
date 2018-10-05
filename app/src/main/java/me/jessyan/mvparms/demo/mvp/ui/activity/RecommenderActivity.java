package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerRecommenderComponent;
import me.jessyan.mvparms.demo.di.module.RecommenderModule;
import me.jessyan.mvparms.demo.mvp.contract.RecommenderContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Member;
import me.jessyan.mvparms.demo.mvp.presenter.RecommenderPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class RecommenderActivity extends BaseActivity<RecommenderPresenter> implements RecommenderContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.image)
    ShapeImageView imageIV;
    @BindView(R.id.nickName)
    TextView nickNameTV;
    @BindView(R.id.level)
    TextView levelTV;
    @BindView(R.id.idCard)
    TextView idCardTV;
    @BindView(R.id.time)
    TextView timeTV;
    @BindView(R.id.type)
    TextView typeTV;

    @Inject
    ImageLoader mImageLoader;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerRecommenderComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .recommenderModule(new RecommenderModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_recommender; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("推荐上属");
        Member member = (Member) getIntent().getSerializableExtra("recommender");
        if (null != member) {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.mipmap.place_holder_user)
                            .url(member.getHeadImage())
                            .isCenterCrop(true)
                            .imageView(imageIV)
                            .build());
            nickNameTV.setText(member.getNickName());
            if (null != member.getGrowthRank()) {
                levelTV.setText(member.getGrowthRank().getGrowthLevelName());
            }
            idCardTV.setText(member.getIdCard());
            timeTV.setText(member.getRegDate());
            typeTV.setText(member.getOriginDesc());
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
        }
    }
}
