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
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.CartBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CartGoodsListAdapter;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class CartListItemHolder extends BaseHolder<CartBean.CartItem> {

    @BindView(R.id.top)
    View topV;
    @BindView(R.id.buttom)
    View buttomV;
    @BindView(R.id.promotion_one)
    TextView promotionOneTV;
    @BindView(R.id.promotion_two)
    TextView promotionTwoTV;
    @BindView(R.id.goods)
    RecyclerView goodsRV;
    @Inject
    CartGoodsListAdapter mAdapter;
    @Inject
    RecyclerView.LayoutManager layoutManager;
    @Inject
    List<CartBean.GoodsBean> goods;

    public CartListItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(CartBean.CartItem cartItem, int position) {

        CartBean.Promotion promotion = cartItem.getPromotion();
        Observable.just(promotion.getTip()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (ArmsUtils.isEmpty(s)) {
                    topV.setVisibility(View.GONE);
                } else {
                    topV.setVisibility(View.VISIBLE);
                    promotionOneTV.setText(s);
                }
            }
        });
        Observable.just(promotion.getTitle()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (ArmsUtils.isEmpty(s)) {
                    buttomV.setVisibility(View.GONE);
                } else {
                    topV.setVisibility(View.VISIBLE);
                    promotionOneTV.setText(s);
                }
            }
        });
        goods.addAll(cartItem.getGoodsList());
        ArmsUtils.configRecyclerView(goodsRV, layoutManager);
        goodsRV.setAdapter(mAdapter);
    }


    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.promotionOneTV = null;
        this.promotionTwoTV = null;
        this.goodsRV = null;
    }
}
