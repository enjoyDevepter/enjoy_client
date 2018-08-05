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
import me.jessyan.mvparms.demo.mvp.model.entity.request.Store;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class StoresListItemHolder extends BaseHolder<Store> {

    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.distance)
    TextView distanceTV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.check)
    View checkV;

    public StoresListItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Store store, int position) {
        Observable.just(store.getName())
                .subscribe(s -> nameTV.setText(s));
        Observable.just(store.getDistance())
                .subscribe(s -> distanceTV.setText(String.valueOf(s)));
        Observable.just(store.getAddress())
                .subscribe(s -> addressTV.setText(String.valueOf(s)));
        Observable.just(store.isCheck())
                .subscribe(s -> checkV.setSelected(s));
    }

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.nameTV = null;
        this.distanceTV = null;
        this.addressTV = null;
        this.checkV = null;
    }
}
