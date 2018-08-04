package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.AddressListContract;
import me.jessyan.mvparms.demo.mvp.model.AddressListModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressEditListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressListAdapter;


@Module
public class AddressListModule {
    private AddressListContract.View view;

    /**
     * 构建AddressListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public AddressListModule(AddressListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    AddressListContract.View provideAddressListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    AddressListContract.Model provideAddressListModel(AddressListModel model) {
        return model;
    }


    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Address> provideAddressList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    AddressListAdapter provideAddressAdapter(List<Address> list) {
        return new AddressListAdapter(list);
    }

    @ActivityScope
    @Provides
    AddressEditListAdapter provideAddressEditAdapter(List<Address> list) {
        return new AddressEditListAdapter(list);
    }

}