/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import butterknife.BindView;
import io.reactivex.Observable;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressEditListAdapter;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AddressEditListItemHolder extends BaseHolder<Address> {

    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.phone)
    TextView phoneTV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.edit)
    View editV;
    @BindView(R.id.delete)
    View deleteV;
    @BindView(R.id.def)
    View delV;
    @BindView(R.id.check)
    View checkV;

    private AddressEditListAdapter.OnChildItemClickLinstener onChildItemClickLinstener;

    public AddressEditListItemHolder(View itemView, AddressEditListAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        editV.setOnClickListener(this);
        deleteV.setOnClickListener(this);
        delV.setOnClickListener(this);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
    }

    @Override
    public void onClick(View view) {
        if (null != onChildItemClickLinstener) {
            switch (view.getId()) {
                case R.id.edit:
                    onChildItemClickLinstener.onChildItemClick(view, AddressEditListAdapter.ViewName.EDIT, getAdapterPosition());
                    return;
                case R.id.delete:
                    onChildItemClickLinstener.onChildItemClick(view, AddressEditListAdapter.ViewName.DELETE, getAdapterPosition());
                    return;
                case R.id.def:
                    onChildItemClickLinstener.onChildItemClick(view, AddressEditListAdapter.ViewName.CHECK, getAdapterPosition());
                    return;
            }
        }
        super.onClick(view);
    }

    @Override
    public void setData(Address address, int position) {
        Observable.just(address.getIsDefaultIn())
                .subscribe(s -> checkV.setSelected("1".equals(s) ? true : false));
        Observable.just(address.getReceiverName())
                .subscribe(s -> nameTV.setText(s));
        Observable.just(address.getPhone())
                .subscribe(s -> phoneTV.setText(String.valueOf(s)));
        Observable.just(address.getAddress())
                .subscribe(s -> addressTV.setText("地址：" + address.getProvinceName() + address.getCityName() + address.getCountyName() + " " + String.valueOf(s)));
    }


    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.nameTV = null;
        this.phoneTV = null;
        this.addressTV = null;
    }
}
