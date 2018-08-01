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
package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodSummary;
import me.jessyan.mvparms.demo.mvp.ui.holder.GoodsItemHolder;
import me.jessyan.mvparms.demo.mvp.ui.holder.GoodsStyleOneItemHolder;

/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class HomeGoodsAdapter extends DefaultAdapter<GoodSummary> {
    public HomeGoodsAdapter(List<GoodSummary> goods) {
        super(goods);
    }

    @Override
    public BaseHolder<GoodSummary> getHolder(View v, int viewType) {
        switch (getInfos().size()) {
            case 1:
                return new GoodsStyleOneItemHolder(v);
        }
        return new GoodsItemHolder(v, getInfos().size());
    }

    @Override
    public int getLayoutId(int viewType) {
        switch (getInfos().size()) {
            case 1:
                return R.layout.home_goods_style_one_item;
            case 2:
                return R.layout.home_goods_style_two_item;
        }
        return R.layout.home_goods_style_normal_item;
    }
}
