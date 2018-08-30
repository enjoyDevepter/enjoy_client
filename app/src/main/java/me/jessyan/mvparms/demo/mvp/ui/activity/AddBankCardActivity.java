package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.WheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerAddBankCardComponent;
import me.jessyan.mvparms.demo.di.module.AddBankCardModule;
import me.jessyan.mvparms.demo.mvp.contract.AddBankCardContract;
import me.jessyan.mvparms.demo.mvp.model.ChooseBankModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankBean;
import me.jessyan.mvparms.demo.mvp.presenter.AddBankCardPresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ChooseBankNameAdapter;


import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AddBankCardActivity extends BaseActivity<AddBankCardPresenter> implements AddBankCardContract.View {

    @BindView(R.id.back)
    View back;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.commit)
    View commit;

    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_bank)
    TextView et_bank;
    @BindView(R.id.choose_view)
    View choose_view;
    @BindView(R.id.num_bank)
    EditText num_bank;

    @BindView(R.id.bank_list)
    WheelView bank_list;

    @BindView(R.id.ok)
    View ok;
    @BindView(R.id.cancel)
    View cancel;
    @BindView(R.id.bank_list_parent)
    View bank_list_parent;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAddBankCardComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addBankCardModule(new AddBankCardModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_add_bank_card; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        title.setText("添加银行卡");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_name.getText().toString();
                if(TextUtils.isEmpty(title)){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"请输入持卡人姓名");
                    return;
                }
                if(currentBankBean == null){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"请选择银行");
                    return;
                }
                String cardNo = num_bank.getText().toString();
                if(TextUtils.isEmpty(cardNo)){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"请输入卡号");
                    return;
                }

                mPresenter.addBankCard(currentBankBean.getName(),cardNo,title,currentBankBean.getBankId());
            }
        });

        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        bank_list.setCyclic(false);
        bank_list.setCurrentItem(0);
        bank_list.setDividerColor(Color.parseColor("#E7E7E7"));
        bank_list.setDividerType(WheelView.DividerType.FILL);
        bank_list.setTextColorCenter(Color.parseColor("#000000"));
        bank_list.setTextColorOut(Color.parseColor("#9A9A9A"));
        bank_list.setTextSize(13);
        bank_list.setGravity(Gravity.CENTER);
        bank_list.setLineSpacingMultiplier(ArmsUtils.dip2px(ArmsUtils.getContext(),31));





        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBankBean = cacheBankBean;
                bank_list_parent.setVisibility(View.GONE);
                et_bank.setText(currentBankBean.getName());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_list_parent.setVisibility(View.GONE);
            }
        });
        choose_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_list_parent.setVisibility(View.VISIBLE);
                et_name.clearFocus();
                num_bank.clearFocus();
                hideImm();
            }
        });

    }

    public void updateBankList(List<BankBean> bankBeans){
        ChooseBankNameAdapter adapter = new ChooseBankNameAdapter(bankBeans);
        bank_list.setAdapter(adapter);
        cacheBankBean = bankBeans.get(0);
        bank_list.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                if(i < 0){
                    return;
                }
                cacheBankBean = bankBeans.get(i);
            }
        });
    }

    private BankBean cacheBankBean;

    private BankBean currentBankBean;

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
