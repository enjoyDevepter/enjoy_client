package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.guoqi.actionsheet.ActionSheet;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerUserInfoComponent;
import me.jessyan.mvparms.demo.di.module.UserInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.UserInfoContract;
import me.jessyan.mvparms.demo.mvp.presenter.UserInfoPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.View, View.OnClickListener, ActionSheet.OnActionSheetSelected {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.image_layout)
    View imageV;
    @BindView(R.id.image)
    ShapeImageView imageTV;
    @BindView(R.id.name_layout)
    View nameV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.male_layout)
    View maleV;
    @BindView(R.id.male)
    TextView maleTV;
    @BindView(R.id.age_layout)
    View ageV;
    @BindView(R.id.age)
    TextView ageTV;
    @BindView(R.id.area_layout)
    View areaV;
    @BindView(R.id.area)
    TextView areaTV;
    @BindView(R.id.constellation_layout)
    View constellationV;
    @BindView(R.id.constellation)
    TextView constellationTV;
    @BindView(R.id.hobby_layout)
    View hobbyV;
    @BindView(R.id.hobby)
    TextView hobbyTV;
    @BindView(R.id.occupation_layout)
    View occupationV;
    @BindView(R.id.occupation)
    TextView occupationTV;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerUserInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .userInfoModule(new UserInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_user_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("个人信息");
        backV.setOnClickListener(this);
        imageV.setOnClickListener(this);
        nameV.setOnClickListener(this);
        maleV.setOnClickListener(this);
        ageV.setOnClickListener(this);
        areaV.setOnClickListener(this);
        constellationV.setOnClickListener(this);
        hobbyV.setOnClickListener(this);
        occupationV.setOnClickListener(this);
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
            case R.id.image_layout:
                ActionSheet.showSheet(this, this, null);
                break;
            case R.id.name_layout:
                break;
            case R.id.male_layout:
                break;
            case R.id.age_layout:
                break;
            case R.id.area_layout:
                break;
            case R.id.constellation_layout:
                break;
            case R.id.hobby_layout:
                break;
            case R.id.occupation_layout:
                break;
        }
    }

    @Override
    public void onClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.CHOOSE_PICTURE:
                //相册
                break;
            case ActionSheet.TAKE_PICTURE:
                //拍照
                break;
            case ActionSheet.CANCEL:
                //取消
                break;
        }
    }
}
