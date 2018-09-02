package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinInputContract;
import me.jessyan.mvparms.demo.mvp.model.ConsumeCoinInputModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.ChargeBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ConsumeInputAdapter;


@Module
public class ConsumeCoinInputModule {
    private ConsumeCoinInputContract.View view;

    /**
     * 构建ConsumeCoinInputModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ConsumeCoinInputModule(ConsumeCoinInputContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinInputContract.View provideConsumeCoinInputView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ConsumeCoinInputContract.Model provideConsumeCoinInputModel(ConsumeCoinInputModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<ChargeBean> list) {
        return new ConsumeInputAdapter(list);
    }


    @ActivityScope
    @Provides
    List<ChargeBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}