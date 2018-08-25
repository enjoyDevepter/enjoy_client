package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MyMealDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.MyMealDetailsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealDetailListAdapter;


@Module
public class MyMealDetailsModule {
    private MyMealDetailsContract.View view;

    /**
     * 构建MyMealDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MyMealDetailsModule(MyMealDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MyMealDetailsContract.View provideMyMealDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MyMealDetailsContract.Model provideMyMealDetailsModel(MyMealDetailsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Appointment> provideUserList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    MyMealDetailListAdapter provideUserAdapter(List<Appointment> list) {
        return new MyMealDetailListAdapter(list);
    }

}