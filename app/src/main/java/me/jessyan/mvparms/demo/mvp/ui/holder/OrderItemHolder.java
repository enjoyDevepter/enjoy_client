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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.Observable;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.order.Order;
import me.jessyan.mvparms.demo.mvp.model.entity.order.OrderGoods;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyOrderAdapter;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class OrderItemHolder extends BaseHolder<Order> {
    @BindView(R.id.time)
    TextView timeTV;
    @BindView(R.id.status)
    TextView statusTV;
    @BindView(R.id.pay)
    View payV;
    @BindView(R.id.logistics)
    View logisticsV;
    @BindView(R.id.take_over)
    View takeOverV;
    @BindView(R.id.more_layout)
    View moreV;
    @BindView(R.id.images)
    LinearLayout imageLL;
    @BindView(R.id.more_price)
    TextView morePriceTV;
    @BindView(R.id.more_count)
    TextView moreCountTV;
    @BindView(R.id.single_layout)
    View singleV;
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.price_single)
    TextView priceTV;
    @BindView(R.id.count)
    TextView countTV;
    @BindView(R.id.payPrice_layout)
    View payPriceV;
    @BindView(R.id.payPrice)
    TextView payPriceTV;
    @BindView(R.id.single_price_info)
    TextView single_price_infoTV;
    @BindView(R.id.single_price_tag)
    TextView single_price_tagTV;
    @BindColor(R.color.order_item_color)
    int textColor;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

    private MyOrderAdapter.OnChildItemClickLinstener onChildItemClickLinstener;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public OrderItemHolder(View itemView, MyOrderAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
        payV.setOnClickListener(this);
        logisticsV.setOnClickListener(this);
        takeOverV.setOnClickListener(this);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void onClick(View view) {
        if (null != onChildItemClickLinstener) {
            switch (view.getId()) {
                case R.id.pay:
                    onChildItemClickLinstener.onChildItemClick(view, MyOrderAdapter.ViewName.PAY, getAdapterPosition());
                    return;
                case R.id.logistics:
                    onChildItemClickLinstener.onChildItemClick(view, MyOrderAdapter.ViewName.LOGISTICS, getAdapterPosition());
                    return;
                case R.id.take_over:
                    onChildItemClickLinstener.onChildItemClick(view, MyOrderAdapter.ViewName.TAKE_OVER, getAdapterPosition());
                    break;
            }
        }
        onChildItemClickLinstener.onChildItemClick(view, MyOrderAdapter.ViewName.ITEM, getAdapterPosition());
    }

    @Override
    public void setData(Order order, int position) {
        Observable.just(order.getOrderTime())
                .subscribe(s -> timeTV.setText("时间：" + sdf.format(s)));
        RecyclerView.LayoutParams itemLayoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();

        if ("6".equals(order.getOrderType())) { // 医美套餐
            countTV.setVisibility(View.INVISIBLE);
            payPriceV.setVisibility(View.INVISIBLE);
        } else if ("7".equals(order.getOrderType())) { // 医美定金预售
            countTV.setVisibility(View.GONE);
            payPriceV.setVisibility(View.VISIBLE);
            priceTV.setTextColor(textColor);
            single_price_tagTV.setTextColor(textColor);
            single_price_infoTV.setTextColor(textColor);
            payPriceTV.setText(String.valueOf(order.getGoodsList().get(0).getSalePrice()));
        } else {
            single_price_infoTV.setVisibility(View.GONE);
        }

        if (order.getGoodsList().size() > 1) {
            itemLayoutParams.height = ArmsUtils.getDimens(itemView.getContext(), R.dimen.order_item_height);
            singleV.setVisibility(View.GONE);
            moreV.setVisibility(View.VISIBLE);
            List<OrderGoods> goodsList = order.getGoodsList();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ArmsUtils.getDimens(itemView.getContext(), R.dimen.order_image_height), ArmsUtils.getDimens(itemView.getContext(), R.dimen.order_image_height));
            layoutParams.rightMargin = ArmsUtils.getDimens(itemView.getContext(), R.dimen.plus_width);
            for (OrderGoods goods : goodsList) {
                ImageView imageView = new ImageView(itemView.getContext());
                imageView.setLayoutParams(layoutParams);
                mImageLoader.loadImage(itemView.getContext(),
                        ImageConfigImpl
                                .builder()
                                .url(goods.getImage())
                                .imageView(imageView)
                                .build());
                imageLL.addView(imageView, layoutParams);
            }
            morePriceTV.setText(ArmsUtils.formatLong(order.getTotalPrice()));
            moreCountTV.setText("共" + order.getNums() + "件商品，应付款：");
        } else {
            moreV.setVisibility(View.GONE);
            singleV.setVisibility(View.VISIBLE);
            itemLayoutParams.height = ArmsUtils.getDimens(itemView.getContext(), R.dimen.order_single_item_height);
            String name = "", image = "";
            double salePrice = 0;
            if ("6".equals(order.getOrderType())) {
                image = order.getSetMealGoodsList().get(0).getImage();
                name = order.getSetMealGoodsList().get(0).getName();
                salePrice = order.getSetMealGoodsList().get(0).getSalePrice();
            } else {
                OrderGoods goods = order.getGoodsList().get(0);
                name = goods.getName();
                image = goods.getImage();
                salePrice = goods.getSalePrice();
            }
            //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
            mImageLoader.loadImage(itemView.getContext(),
                    ImageConfigImpl
                            .builder()
                            .url(image)
                            .imageView(imageIV)
                            .build());

            nameTV.setText(name);
            priceTV.setText(String.valueOf(salePrice));
            countTV.setText("数量：x" + String.valueOf(order.getNums()));

        }

        if (order.getOrderStatus().equals("1")) {
            statusTV.setText("待付款");
            logisticsV.setVisibility(View.GONE);
            takeOverV.setVisibility(View.GONE);
        } else if (order.getOrderStatus().equals("3")) {
            statusTV.setText("待发货");
            takeOverV.setVisibility(View.GONE);
            payV.setVisibility(View.GONE);
        } else if (order.getOrderStatus().equals("4")) {
            statusTV.setText("待收货");
            payV.setVisibility(View.GONE);
        } else if (order.getOrderStatus().equals("5")) {
            statusTV.setText("已完成");
            payV.setVisibility(View.GONE);
            logisticsV.setVisibility(View.GONE);
            takeOverV.setVisibility(View.GONE);
        }

        itemView.setLayoutParams(itemLayoutParams);
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
        this.timeTV = null;
        this.statusTV = null;
        this.payV = null;
        this.logisticsV = null;
        this.takeOverV = null;
        this.moreV = null;
        this.imageLL = null;
        this.morePriceTV = null;
        this.moreCountTV = null;
        this.singleV = null;
        this.imageIV = null;
        this.nameTV = null;
        this.priceTV = null;
        this.countTV = null;
    }
}
