package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;

import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

public class HospitalGoodsListHolder extends BaseHolder<Goods> {


    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;

    public HospitalGoodsListHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(Goods data, int position) {

    }

    @Override
    protected void onRelease() {

    }

}
