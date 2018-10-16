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
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AppointmentListAdapter;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppointmentListHolder extends BaseHolder<Appointment> {
    @BindView(R.id.no)
    TextView noTV;
    @BindView(R.id.left)
    TextView leftTV;
    @BindView(R.id.right)
    TextView rightTV;
    @BindView(R.id.status)
    TextView statusTV;
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.time_type)
    View tiemV;
    @BindView(R.id.time)
    TextView timeTV;
    @BindView(R.id.confirmTime)
    TextView confirmTimeTV;
    @BindView(R.id.confirmTime_tag)
    View confirmTimeV;
    @BindView(R.id.deductTime)
    TextView deductTimeTV;
    @BindView(R.id.deductTime_tag)
    View deductTimeV;


    private AppointmentListAdapter.OnChildItemClickLinstener onChildItemClickLinstener;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public AppointmentListHolder(View itemView, AppointmentListAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
        leftTV.setOnClickListener(this);
        rightTV.setOnClickListener(this);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void onClick(View view) {
        if (null != onChildItemClickLinstener) {
            switch (view.getId()) {
                case R.id.left:
                    onChildItemClickLinstener.onChildItemClick(view, AppointmentListAdapter.ViewName.LEFT, getAdapterPosition());
                    return;
                case R.id.right:
                    onChildItemClickLinstener.onChildItemClick(view, AppointmentListAdapter.ViewName.RIGHT, getAdapterPosition());
                    return;
            }
        }
        onChildItemClickLinstener.onChildItemClick(view, AppointmentListAdapter.ViewName.ITEM, getAdapterPosition());
    }

    @Override
    public void setData(Appointment appointment, int position) {
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(appointment.getGoods().getImage())
                        .imageView(imageIV)
                        .isCenterCrop(true)
                        .build());
        noTV.setText(appointment.getProjectId());
        if ("1".equals(appointment.getStatus())) {
            rightTV.setVisibility(View.VISIBLE);
            rightTV.setText("预约");
            timeTV.setVisibility(View.GONE);
            leftTV.setVisibility(View.GONE);
        } else if ("2".equals(appointment.getStatus())) {
            leftTV.setVisibility(View.VISIBLE);
            leftTV.setText("取消");
            rightTV.setVisibility(View.VISIBLE);
            rightTV.setText("改约");
        } else { // 3.4.5
            leftTV.setVisibility(View.GONE);
            rightTV.setVisibility(View.GONE);
            timeTV.setVisibility(View.GONE);
        }
        if (ArmsUtils.isEmpty(appointment.getReservationDate()) || ArmsUtils.isEmpty(appointment.getReservationTime())) {
            timeTV.setVisibility(View.INVISIBLE);
            tiemV.setVisibility(View.INVISIBLE);
        } else {
            tiemV.setVisibility(View.VISIBLE);
            timeTV.setVisibility(View.VISIBLE);
            timeTV.setText(appointment.getReservationDate() + "\n" + appointment.getReservationTime());
        }
        statusTV.setText(appointment.getStatusDesc());
        nameTV.setText(appointment.getGoods().getName());

        if (!ArmsUtils.isEmpty(appointment.getConfirmTime())) {
            confirmTimeTV.setVisibility(View.VISIBLE);
            confirmTimeTV.setText(appointment.getConfirmTime());
            confirmTimeV.setVisibility(View.VISIBLE);
        } else {
            confirmTimeV.setVisibility(View.GONE);
            confirmTimeTV.setVisibility(View.GONE);
        }
        if (!ArmsUtils.isEmpty(appointment.getDeductTime())) {
            deductTimeTV.setVisibility(View.VISIBLE);
            deductTimeTV.setText(appointment.getDeductTime());
            deductTimeV.setVisibility(View.VISIBLE);
        } else {
            deductTimeTV.setVisibility(View.GONE);
            deductTimeV.setVisibility(View.GONE);
        }

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
        this.noTV = null;
        this.leftTV = null;
        this.rightTV = null;
        this.statusTV = null;
        this.imageIV = null;
        this.nameTV = null;
        this.timeTV = null;
        this.tiemV = null;
        this.deductTimeV = null;
        this.deductTimeTV = null;
        this.confirmTimeV = null;
        this.confirmTimeTV = null;

    }
}
