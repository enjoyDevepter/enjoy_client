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

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.Observable;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class HGoodsListItemHolder extends BaseHolder<HGoods> {

    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.doctor)
    TextView doctorT;
    @BindView(R.id.deposit)
    TextView depositTV;
    @BindView(R.id.tailMoney)
    TextView tailMoneyTV;
    @BindView(R.id.sale)
    TextView saleTV;
    @BindView(R.id.pricet_tag)
    TextView priceTagTV;
    @BindColor(R.color.red)
    int redColor;
    @BindColor(R.color.order_item_color)
    int norColor;
    @BindView(R.id.price_layout)
    View depositV;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public HGoodsListItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(HGoods goods, int position) {
        Observable.just(goods.getName())
                .subscribe(s -> nameTV.setText(s));
        Observable.just(goods.getGoodsSpecValue().getSpecValueName())
                .subscribe(s -> doctorT.setText(s));
        Observable.just(goods.getSales())
                .subscribe(s -> saleTV.setText(String.valueOf(s)));
        if (goods.getDeposit() == 0 && goods.getTailMoney() == 0 && ArmsUtils.isEmpty(goods.getAdvanceDepositId())) {
            depositV.setVisibility(View.INVISIBLE);
            priceTagTV.setTextColor(redColor);
//            tailMoneyTV.setTextSize(ArmsUtils.getDimens(tailMoneyTV.getContext(), R.dimen.price_red_text_size));
            tailMoneyTV.setTextColor(redColor);
            Observable.just(goods.getSalePrice())
                    .subscribe(s -> tailMoneyTV.setText(String.valueOf(s)));
        } else {
            depositV.setVisibility(View.VISIBLE);
//            tailMoneyTV.setTextSize(ArmsUtils.getDimens(tailMoneyTV.getContext(), R.dimen.price_nor_text_size));
            tailMoneyTV.setTextColor(norColor);
            priceTagTV.setTextColor(norColor);
            Observable.just(goods.getDeposit())
                    .subscribe(s -> depositTV.setText(String.valueOf(s)));
            Observable.just(goods.getTailMoney())
                    .subscribe(s -> tailMoneyTV.setText(String.valueOf(s)));
        }

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_img)
                        .url(goods.getImage())
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
        this.depositTV = null;
        this.tailMoneyTV = null;
        this.saleTV = null;
        this.doctorT = null;
        this.priceTagTV = null;
        this.depositV = null;
        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}
