package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.ChooseBankContract;
import me.jessyan.mvparms.demo.mvp.model.ChooseBankModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ChooseBankAdapter;


@Module
public class ChooseBankModule {
    private ChooseBankContract.View view;

    /**
     * 构建ChooseBankModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public ChooseBankModule(ChooseBankContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    ChooseBankContract.View provideChooseBankView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    ChooseBankContract.Model provideChooseBankModel(ChooseBankModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<BankCardBean> list) {
        return new ChooseBankAdapter(list);
    }


    @ActivityScope
    @Provides
    List<BankCardBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}