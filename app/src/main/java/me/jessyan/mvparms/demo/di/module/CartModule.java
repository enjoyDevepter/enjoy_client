package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.CartContract;
import me.jessyan.mvparms.demo.mvp.model.CartModel;
import me.jessyan.mvparms.demo.mvp.model.entity.CartBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CartListAdapter;


@Module
public class CartModule {
    private CartContract.View view;

    /**
     * 构建CartModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public CartModule(CartContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    CartContract.View provideCartView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    CartContract.Model provideCartModel(CartModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<CartBean.CartItem> provideCartList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideCartAdapter(List<CartBean.CartItem> list) {
        return new CartListAdapter(list);
    }
}

