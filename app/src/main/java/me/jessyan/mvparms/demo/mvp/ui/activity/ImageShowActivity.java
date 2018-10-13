package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerImageShowComponent;
import me.jessyan.mvparms.demo.di.module.ImageShowModule;
import me.jessyan.mvparms.demo.mvp.contract.ImageShowContract;
import me.jessyan.mvparms.demo.mvp.presenter.ImageShowPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ImageShowActivity extends BaseActivity<ImageShowPresenter> implements ImageShowContract.View {

    @BindView(R.id.pager)
    ViewPager viewPager;
    @Inject
    ImageLoader mImageLoader;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerImageShowComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .imageShowModule(new ImageShowModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_image_show; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String[] urls = getIntent().getStringArrayExtra("images");
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return urls.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(ImageShowActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                mImageLoader.loadImage(view.getContext(),
                        ImageConfigImpl
                                .builder()
                                .placeholder(R.drawable.place_holder_img)
                                .url(urls[position])
                                .imageView(view)
                                .build());
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        viewPager.setCurrentItem(getIntent().getIntExtra("index", 0));
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

}
