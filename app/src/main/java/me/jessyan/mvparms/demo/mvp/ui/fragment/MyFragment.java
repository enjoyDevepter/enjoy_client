package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerMyComponent;
import me.jessyan.mvparms.demo.di.module.MyModule;
import me.jessyan.mvparms.demo.mvp.contract.MyContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;
import me.jessyan.mvparms.demo.mvp.presenter.MyPresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.CashCoinActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.ConsumeCoinActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.CouponActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.FansActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.GrowthActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.InviteMainActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MessageActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyFarvirateActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyFollowActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyMealActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyOrderActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyStoreActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SettingActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserInfoActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserIntegralActivity;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyFragment extends BaseFragment<MyPresenter> implements MyContract.View, View.OnClickListener {

    @BindView(R.id.setting)
    View settingV;
    @BindView(R.id.msg)
    View msgV;
    @BindView(R.id.nickName)
    TextView nickNameTV;
    @BindView(R.id.money)
    TextView moneyTV;
    @BindView(R.id.member_money)
    TextView memberMoneyTV;
    @BindView(R.id.bonus)
    TextView bonusTV;
    @BindView(R.id.bouns_parent)
    View bouns_parent;
    @BindView(R.id.goods_order)
    View gOrderV;
    @BindView(R.id.hgoods_order)
    View hOrderV;
    @BindView(R.id.kgoods_order)
    View kOrderV;
    @BindView(R.id.friend)
    View friendV;
    @BindView(R.id.diary)
    View diaryV;
    @BindView(R.id.collect)
    View collectV;
    @BindView(R.id.follow)
    View followV;
    @BindView(R.id.meal)
    View mealV;
    @BindView(R.id.coupon)
    View couponV;
    @BindView(R.id.store)
    View storeV;
    @BindView(R.id.recommender)
    View recommenderV;
    @BindView(R.id.consume)
    View consume;
    @BindView(R.id.cash)
    View cash;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.level_icon)
    ImageView level_icon;
    @BindView(R.id.fans)
    View fans;
    @BindView(R.id.follow_view)
    View follow_view;
    @BindView(R.id.chenghao)
    TextView chenghao;
    @BindView(R.id.growth)
    TextView growthTV;
    @BindView(R.id.qr)
    View qrV;
    @BindView(R.id.qr_layout)
    View qrlayoutV;
    @BindView(R.id.qr_image)
    ImageView qrImageIV;


    @Inject
    ImageLoader mImageLoader;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myModule(new MyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        gOrderV.setOnClickListener(this);
        kOrderV.setOnClickListener(this);
        hOrderV.setOnClickListener(this);
        friendV.setOnClickListener(this);
        diaryV.setOnClickListener(this);
        collectV.setOnClickListener(this);
        followV.setOnClickListener(this);
        mealV.setOnClickListener(this);
        couponV.setOnClickListener(this);
        storeV.setOnClickListener(this);
        follow_view.setOnClickListener(this);
        settingV.setOnClickListener(this);
        msgV.setOnClickListener(this);
        bouns_parent.setOnClickListener(this);
        recommenderV.setOnClickListener(this);
        consume.setOnClickListener(this);
        cash.setOnClickListener(this);
        growthTV.setOnClickListener(this);
        image.setOnClickListener(this);
        fans.setOnClickListener(this);
        qrV.setOnClickListener(this);
        qrlayoutV.setOnClickListener(this);
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

    }

    @Subscriber(tag = EventBusTags.ONREFRESH_CONTENT)
    public void refreshContent(int index) {
        if (index == 4) {
            mPresenter.getUserInfo();
        }
    }

    @Subscriber(tag = EventBusTags.USER_BASE_INFO_CHANGE)
    private void updateUserInfo(int cmd) {
        mPresenter.getUserInfo();
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                ArmsUtils.startActivity(SettingActivity.class);
                break;
            case R.id.msg:
                ArmsUtils.startActivity(MessageActivity.class);
                break;
            case R.id.goods_order:
                Intent intent0 = new Intent(getContext(), MyOrderActivity.class);
                intent0.putExtra("type", 2);
                ArmsUtils.startActivity(intent0);
                break;
            case R.id.kgoods_order:
                Intent intent1 = new Intent(getContext(), MyOrderActivity.class);
                intent1.putExtra("type", 1);
                ArmsUtils.startActivity(intent1);
                break;
            case R.id.hgoods_order:
                Intent intent2 = new Intent(getContext(), MyOrderActivity.class);
                intent2.putExtra("type", 0);
                ArmsUtils.startActivity(intent2);
                break;
            case R.id.friend:
                ArmsUtils.startActivity(InviteMainActivity.class);
                break;
            case R.id.diary:
                mPresenter.getMyDiaryInfo();
                break;
            case R.id.collect:
                ArmsUtils.startActivity(MyFarvirateActivity.class);
                break;
            case R.id.follow:
            case R.id.follow_view:
                ArmsUtils.startActivity(MyFollowActivity.class);
                break;
            case R.id.meal:
                ArmsUtils.startActivity(MyMealActivity.class);
                break;
            case R.id.coupon:
                ArmsUtils.startActivity(CouponActivity.class);
                break;
            case R.id.store:
                ArmsUtils.startActivity(MyStoreActivity.class);
                break;
            case R.id.recommender:
//                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
//                Member member = (Member) cache.get(KEY_KEEP + MyModel.KEY_FOR_USER_INFO);
//                Intent recommenderIntent = new Intent(getContext(), RecommenderActivity.class);
//                recommenderIntent.putExtra("recommender", member.getRecomMember());
//                ArmsUtils.startActivity(recommenderIntent);
                break;
            case R.id.bouns_parent:
                Intent bonusIntent = new Intent(getContext(), UserIntegralActivity.class);
                ArmsUtils.startActivity(bonusIntent);
                break;
            case R.id.consume:
                Intent consumeIntent = new Intent(getContext(), ConsumeCoinActivity.class);
                ArmsUtils.startActivity(consumeIntent);
                break;
            case R.id.cash:
                Intent cashIntent = new Intent(getContext(), CashCoinActivity.class);
                ArmsUtils.startActivity(cashIntent);
                break;
            case R.id.growth:
                ArmsUtils.startActivity(GrowthActivity.class);
                break;
            case R.id.image:
                Cache<String, Object> gCache = ArmsUtils.obtainAppComponentFromContext(getContext()).extras();
                Object o = gCache.get(KEY_KEEP + "token");
                if (o == null) {
                    ArmsUtils.startActivity(LoginActivity.class);
                } else {
                    ArmsUtils.startActivity(UserInfoActivity.class);
                }
                break;
            case R.id.fans:
                ArmsUtils.startActivity(FansActivity.class);
                break;
            case R.id.qr:
                qrlayoutV.setVisibility(View.VISIBLE);
                break;
            case R.id.qr_layout:
                qrlayoutV.setVisibility(View.GONE);
                break;
        }
    }

    @Subscriber(tag = EventBusTags.USER_ACCOUNT_CHANGE)
    public void updateUserAccount(MemberAccount account) {
        moneyTV.setText(String.format("%.2f", account.getBonus() * 1.0 / 100));
        memberMoneyTV.setText(String.format("%.2f", account.getTotal() * 1.0 / 100));
        bonusTV.setText(account.getPoint() + "");
        growthTV.setText("成长值 " + account.getGrowth());
    }

    @Subscriber(tag = EventBusTags.USER_INFO_CHANGE)
    public void updateUserInfo(Member member) {
        mImageLoader.loadImage(getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(member.getHeadImage())
                        .imageView(image)
                        .build());
        nickNameTV.setText(member.getNickName());
        int levelIcon = 0;
        switch (member.getRank().getPointLevelId()) {
            case "1":
                levelIcon = R.mipmap.user_level_1;
                break;
            case "2":
                levelIcon = R.mipmap.user_level_2;
                break;
            case "3":
                levelIcon = R.mipmap.user_level_3;
                break;
            case "4":
                levelIcon = R.mipmap.user_level_4;
                break;
            case "5":
                levelIcon = R.mipmap.user_level_5;
                break;
            case "6":
                break;
        }
        level_icon.setBackground(getResources().getDrawable(levelIcon));

//        recommenderV.setVisibility(null == member.getRecomMember() ? View.INVISIBLE : View.VISIBLE);
        if (null != member.getDistributionRank()) {
            chenghao.setText(member.getDistributionRank().getDistributionLevelName());
        }

        mImageLoader.loadImage(getContext(),
                ImageConfigImpl
                        .builder()
                        .isCenterCrop(true)
                        .url(member.getQrCodeUrl())
                        .imageView(qrImageIV)
                        .build());
    }
}
