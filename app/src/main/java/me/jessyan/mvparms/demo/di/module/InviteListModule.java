package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.InviteListContract;
import me.jessyan.mvparms.demo.mvp.model.InviteListModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MyMemberBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMemberAdapter;


@Module
public class InviteListModule {
    private InviteListContract.View view;

    /**
     * 构建InviteListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InviteListModule(InviteListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InviteListContract.View provideInviteListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InviteListContract.Model provideInviteListModel(InviteListModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideStoreAdapter(List<MyMemberBean> list) {
        return new MyMemberAdapter(list);
    }


    @ActivityScope
    @Provides
    List<MyMemberBean> provideOrderBeanList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

}