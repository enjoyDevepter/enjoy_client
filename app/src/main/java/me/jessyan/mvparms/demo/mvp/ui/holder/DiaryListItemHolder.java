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
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
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
public class DiaryListItemHolder extends BaseHolder<Diary> {

    @BindView(R.id.headImage)
    ShapeImageView headImageIV;
    @BindView(R.id.nickName)
    TextView nickNameTV;
    @BindView(R.id.fllow)
    View fllowV;
    @BindView(R.id.publishDate)
    TextView publishDateTV;
    @BindView(R.id.left)
    ImageView leftIV;
    @BindView(R.id.right)
    ImageView rightIV;
    @BindView(R.id.intro)
    TextView introTV;
    @BindView(R.id.browse)
    TextView browseTV;
    @BindView(R.id.comment)
    TextView commentTV;
    @BindView(R.id.isPraise)
    View isPraiseTV;
    @BindView(R.id.praise)
    TextView praiseTV;
    @BindView(R.id.praise_layout)
    View praiseLayoutV;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架


    private DiaryListAdapter.OnChildItemClickLinstener onChildItemClickLinstener;

    public DiaryListItemHolder(View itemView, DiaryListAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
        fllowV.setOnClickListener(this);
        praiseLayoutV.setOnClickListener(this);
        leftIV.setOnClickListener(this);
        rightIV.setOnClickListener(this);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
    }

    @Override
    public void onClick(View view) {
        if (null != onChildItemClickLinstener) {
            switch (view.getId()) {
                case R.id.fllow:
                    onChildItemClickLinstener.onChildItemClick(view, DiaryListAdapter.ViewName.FLLOW, getAdapterPosition());
                    return;
                case R.id.left:
                    onChildItemClickLinstener.onChildItemClick(view, DiaryListAdapter.ViewName.LEFT_IMAGE, getAdapterPosition());
                    return;
                case R.id.right:
                    onChildItemClickLinstener.onChildItemClick(view, DiaryListAdapter.ViewName.RIGHT_IMAGE, getAdapterPosition());
                    return;
                case R.id.praise_layout:
                    onChildItemClickLinstener.onChildItemClick(view, DiaryListAdapter.ViewName.VOTE, getAdapterPosition());
                    return;
            }
        }
        onChildItemClickLinstener.onChildItemClick(view, DiaryListAdapter.ViewName.ITEM, getAdapterPosition());

    }

    @Override
    public void setData(Diary diary, int position) {
        Observable.just(diary.getMember().getNickName())
                .subscribe(s -> nickNameTV.setText(s));
        Observable.just(diary.getMember().getIsFollow())
                .subscribe(s -> fllowV.setSelected("1".equals(s) ? true : false));
        Observable.just(diary.getPublishDate())
                .subscribe(s -> publishDateTV.setText(s));
        Observable.just(diary.getIntro())
                .subscribe(s -> introTV.setText(s));
        Observable.just(diary.getBrowse())
                .subscribe(s -> browseTV.setText(String.valueOf(s)));
        Observable.just(diary.getComment())
                .subscribe(s -> commentTV.setText(String.valueOf(s)));
        Observable.just(diary.getIsPraise())
                .subscribe(s -> isPraiseTV.setSelected("1".equals(s) ? true : false));
        Observable.just(diary.getPraise())
                .subscribe(s -> praiseTV.setText(String.valueOf(s)));

        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(diary.getMember().getHeadImage())
                        .imageView(headImageIV)
                        .build());

        if (diary.getImageList() != null && diary.getImageList().size() > 0) {
            mImageLoader.loadImage(itemView.getContext(),
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.mipmap.place_holder_img)
                            .url(diary.getImageList().get(0))
                            .imageView(leftIV)
                            .build());
        }
        if (diary.getImageList() != null && diary.getImageList().size() > 1) {
            mImageLoader.loadImage(itemView.getContext(),
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.mipmap.place_holder_img)
                            .url(diary.getImageList().get(1))
                            .imageView(rightIV)
                            .build());
        }


    }


    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.headImageIV = null;
        this.nickNameTV = null;
        this.fllowV = null;
        this.publishDateTV = null;
        this.leftIV = null;
        this.rightIV = null;
        this.introTV = null;
        this.browseTV = null;
        this.commentTV = null;
        this.isPraiseTV = null;
        this.praiseTV = null;
        this.praiseLayoutV = null;
    }
}
