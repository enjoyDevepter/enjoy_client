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
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import io.reactivex.Observable;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowDoctorAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class MyFollowDocterItemHolder extends BaseHolder<DoctorBean> {

    @BindView(R.id.headImage)
    ShapeImageView headImageIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.fllow)
    View fllowV;
    @BindView(R.id.hospital)
    TextView hospitalTV;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架


    private MyFollowDoctorAdapter.OnChildItemClickLinstener onChildItemClickLinstener;

    public MyFollowDocterItemHolder(View itemView, MyFollowDoctorAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
        fllowV.setOnClickListener(this);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
    }

    @Override
    public void onClick(View view) {
        if (null != onChildItemClickLinstener) {
            switch (view.getId()) {
                case R.id.fllow:
                    onChildItemClickLinstener.onChildItemClick(view, MyFollowDoctorAdapter.ViewName.FLLOW, getAdapterPosition());
                    return;
            }
        }
        onChildItemClickLinstener.onChildItemClick(view, MyFollowDoctorAdapter.ViewName.ITEM, getAdapterPosition());

    }

    @Override
    public void setData(DoctorBean doctorBean, int position) {
        Observable.just(doctorBean.getName())
                .subscribe(s -> nameTV.setText(s));
        fllowV.setSelected(true);
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(doctorBean.getHeadImage())
                        .imageView(headImageIV)
                        .build());
    }


    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.headImageIV = null;
        this.nameTV = null;
        this.fllowV = null;
        this.hospitalTV = null;
    }
}
