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
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import io.reactivex.Observable;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.OrderConfirmGoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.MoneyView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class OrderConfirmGoodsListItemHolder extends BaseHolder<Goods> {
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.spce)
    TextView spceTV;
    @BindView(R.id.price)
    MoneyView priceTV;
    @BindView(R.id.count)
    TextView countTV;
    @BindView(R.id.minus)
    View minusV;
    @BindView(R.id.add)
    View addV;
    @BindView(R.id.type_layout)
    View typeLayoutV;
    @BindView(R.id.spce_count)
    View spce_countV;
    @BindView(R.id.operation)
    View operationV;
    private OrderConfirmGoodsListAdapter.OnChildItemClickLinstener onChildItemClickLinstener;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public OrderConfirmGoodsListItemHolder(View itemView, OrderConfirmGoodsListAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
        minusV.setOnClickListener(this);
        addV.setOnClickListener(this);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void onClick(View view) {
        if (null != onChildItemClickLinstener) {
            switch (view.getId()) {
                case R.id.add:
                    onChildItemClickLinstener.onChildItemClick(view, OrderConfirmGoodsListAdapter.ViewName.ADD, getAdapterPosition());
                    return;
                case R.id.minus:
                    onChildItemClickLinstener.onChildItemClick(view, OrderConfirmGoodsListAdapter.ViewName.MINUS, getAdapterPosition());
                    return;
            }
        }
        super.onClick(view);
    }

    @Override
    public void setData(Goods data, int position) {
        if (data.getSecKillPrice() != 0) {
            priceTV.setMoneyText(String.valueOf(data.getSecKillPrice()));
            spce_countV.setVisibility(View.VISIBLE);
            typeLayoutV.setVisibility(View.GONE);
        } else if (data.getVipPrice() != 0) {
            spce_countV.setVisibility(View.VISIBLE);
            typeLayoutV.setVisibility(View.GONE);
            priceTV.setMoneyText(String.valueOf(data.getVipPrice()));
        } else {
            spce_countV.setVisibility(View.GONE);
            typeLayoutV.setVisibility(View.VISIBLE);
            priceTV.setMoneyText(String.valueOf(data.getSalePrice()));
        }
        Observable.just(data.getName())
                .subscribe(s -> nameTV.setText(String.valueOf(s)));
        Observable.just(data.getNums())
                .subscribe(s -> countTV.setText(String.valueOf(s)));
        Observable.just(data.getGoodsSpecValue().getSpecValueName())
                .subscribe(s -> spceTV.setText("规格：" + String.valueOf(s)));

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(data.getImage())
                        .imageView(imageIV)
                        .build());

    }

    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        //只要传入的 Context 为 Activity, Glide 就会自己做好生命周期的管理, 其实在上面的代码中传入的 Context 就是 Activity
        //所以在 onRelease 方法中不做 clear 也是可以的, 但是在这里想展示一下 clear 的用法
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder()
                .imageViews(imageIV)
                .build());
        this.imageIV = null;
        this.nameTV = null;
        this.priceTV = null;
        this.countTV = null;
        this.minusV = null;
        this.addV = null;
        this.spce_countV = null;
        this.typeLayoutV = null;
    }
}
