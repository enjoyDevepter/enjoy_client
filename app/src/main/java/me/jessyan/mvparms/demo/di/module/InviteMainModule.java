package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

import me.jessyan.mvparms.demo.mvp.contract.InviteMainContract;
import me.jessyan.mvparms.demo.mvp.model.InviteMainModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MyMemberBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMemberAdapter;


@Module
public class InviteMainModule {
    private InviteMainContract.View view;

    /**
     * 构建InviteMainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public InviteMainModule(InviteMainContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    InviteMainContract.View provideInviteMainView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    InviteMainContract.Model provideInviteMainModel(InviteMainModel model) {
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