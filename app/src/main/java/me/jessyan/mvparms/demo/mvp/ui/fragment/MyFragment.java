package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerMyComponent;
import me.jessyan.mvparms.demo.di.module.MyModule;
import me.jessyan.mvparms.demo.mvp.contract.MyContract;
import me.jessyan.mvparms.demo.mvp.presenter.MyPresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.CashCoinActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.ConsumeCoinActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyDiaryActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyMealActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MyOrderActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.UserIntegralActivity;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.mvparms.demo.mvp.ui.activity.CashCoinActivity.KEY_FOR_CASH_STR;


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

    @BindView(R.id.consume)
    View consume;
    @BindView(R.id.cash)
    View cash;

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
        settingV.setOnClickListener(this);
        msgV.setOnClickListener(this);
        bonusTV.setOnClickListener(this);
        consume.setOnClickListener(this);
        cash.setOnClickListener(this);
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
                break;
            case R.id.msg:
                break;
            case R.id.goods_order:
                Intent intent0 = new Intent(getContext(), MyOrderActivity.class);
                intent0.putExtra("type", 0);
                ArmsUtils.startActivity(intent0);
                break;
            case R.id.kgoods_order:
                Intent intent1 = new Intent(getContext(), MyOrderActivity.class);
                intent1.putExtra("type", 1);
                ArmsUtils.startActivity(intent1);
                break;
            case R.id.hgoods_order:
                Intent intent2 = new Intent(getContext(), MyOrderActivity.class);
                intent2.putExtra("type", 2);
                ArmsUtils.startActivity(intent2);
                break;
            case R.id.friend:
                break;
            case R.id.diary:
                ArmsUtils.startActivity(MyDiaryActivity.class);
                break;
            case R.id.collect:
                break;
            case R.id.follow:
                break;
            case R.id.meal:
                ArmsUtils.startActivity(getActivity(), MyMealActivity.class);
                break;
            case R.id.coupon:
                break;
            case R.id.store:
                break;
            case R.id.bonus:
                Intent intent = new Intent(getContext(), UserIntegralActivity.class);
                intent.putExtra(UserIntegralActivity.KEY_FOR_USER_ALL_SCORE, "" + bonusTV.getText());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.consume:
                Intent consumeIntent = new Intent(getContext(), ConsumeCoinActivity.class);
                consumeIntent.putExtra(ConsumeCoinActivity.KEY_FOR_CONSUME_COIN,memberMoneyTV.getText());
                ArmsUtils.startActivity(consumeIntent);
                break;
            case R.id.cash:
                Intent cashIntent = new Intent(getContext(), CashCoinActivity.class);
                cashIntent.putExtra(KEY_FOR_CASH_STR,moneyTV.getText());
                ArmsUtils.startActivity(cashIntent);
                break;
        }
    }
}
