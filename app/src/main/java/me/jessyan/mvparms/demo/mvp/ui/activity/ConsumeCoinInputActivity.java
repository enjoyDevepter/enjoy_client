package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.simple.eventbus.Subscriber;

import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerConsumeCoinInputComponent;
import me.jessyan.mvparms.demo.di.module.ConsumeCoinInputModule;
import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinInputContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;
import me.jessyan.mvparms.demo.mvp.presenter.ConsumeCoinInputPresenter;

import me.jessyan.mvparms.demo.R;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ConsumeCoinInputActivity extends BaseActivity<ConsumeCoinInputPresenter> implements ConsumeCoinInputContract.View {

    @BindView(R.id.back)
    View back;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.consume_count)
    TextView consume_count;  // 消费币
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.money)
    TextView money;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @BindView(R.id.contentList)
    RecyclerView contentList;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean isEnd;
    @BindView(R.id.no_date)
    View onDateV;

    @BindView(R.id.commit)
    View commit;

    private MemberAccount account;

    @BindView(R.id.pay_for_zfb)
    View pay_for_zfb;
    @BindView(R.id.pay_for_wx)
    View pay_for_wx;

    enum PayType{
        WX("WEIXIN_APP"),  // 微信
        ZFB("ALIPAY_APP");  // 支付宝

        private String payType;
        PayType(String payType){
            this.payType = payType;
        }
    }

    @BindView(R.id.zfb)
    View zfb;
    @BindView(R.id.wx)
    View wx;
    private PayType currType = PayType.WX;

    private void selectType(PayType type){
        zfb.setSelected(false);
        wx.setSelected(false);

        switch (type){
            case ZFB:
                zfb.setSelected(true);
                break;
            case WX:
                wx.setSelected(true);
                break;
        }
        currType = type;
    }

    @Subscriber(tag = EventBusTags.USER_ACCOUNT_CHANGE)
    public void updateUserAccount(MemberAccount account){
        if(account == null){
            return;
        }
        this.account = account;
        consume_count.setText(String.format("%.2f", account.getAmount() * 1.0 / 100));
    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.nextPage();
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return isEnd;
                }
            };

            mPaginate = Paginate.with(contentList, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerConsumeCoinInputComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .consumeCoinInputModule(new ConsumeCoinInputModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_consume_coin_input; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        updateUserAccount((MemberAccount)cache.get(KEY_KEEP + MyModel.KEY_FOR_USER_ACCOUNT));
        title.setText("消费币");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        selectType(PayType.ZFB);
        pay_for_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(PayType.ZFB);
            }
        });
        pay_for_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectType(PayType.WX);
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = s + "";
                if(TextUtils.isEmpty(s1)){
                    money.setText("0");
                    money_num = 0;
                }else{
                    long i = 0;
                    i = Long.parseLong(s1);
                    money.setText(""+i);
                    money_num = i * 100;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ArmsUtils.configRecyclerView(contentList, mLayoutManager);
        contentList.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestOrderList();
            }
        });
        initPaginate();

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currType == null){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"请选择支付方式");
                    return;
                }
                mPresenter.recharge(money_num,currType.payType);
            }
        });
    }

    private long money_num = 0;

    @Override
    public void showLoading() {

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
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Activity getActivity(){
        return this;
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }
    @Override
    public void showError(boolean hasDate) {
        onDateV.setVisibility(hasDate ? INVISIBLE : VISIBLE);
        contentList.setVisibility(hasDate ? VISIBLE : INVISIBLE);
    }

    @Override
    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(contentList);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mPaginate = null;
    }

}
