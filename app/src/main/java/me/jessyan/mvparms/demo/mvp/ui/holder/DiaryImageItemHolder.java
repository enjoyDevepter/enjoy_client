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

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class DiaryImageItemHolder extends BaseHolder<DiaryAlbum> {

    @BindView(R.id.image)
    ImageView imageIV;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public DiaryImageItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
        int screenWidth = ArmsUtils.getScreenWidth(ArmsUtils.getContext());
//        if (size == 2) {
//            itemView.setLayoutParams(new RecyclerView.LayoutParams((screenWidth - ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_style_margin_left) * 3 - ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_margin_left) * 2) / 2, ViewGroup.LayoutParams.WRAP_CONTENT));
//        } else {
//            itemView.setLayoutParams(new RecyclerView.LayoutParams((screenWidth - ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_style_margin_left) * 4 - ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.home_module_margin_left) * 2) / 3, ViewGroup.LayoutParams.WRAP_CONTENT));
//        }
    }

    @Override
    public void setData(DiaryAlbum album, int position) {
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(album.getImage())
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
        this.mAppComponent = null;
        this.mImageLoader = null;
    }
}
