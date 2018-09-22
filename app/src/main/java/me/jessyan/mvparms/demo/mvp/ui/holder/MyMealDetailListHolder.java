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
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealDetailListAdapter;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class MyMealDetailListHolder extends BaseHolder<Appointment> {
    @BindView(R.id.time)
    TextView timeTV;
    @BindView(R.id.status)
    TextView statusTV;
    @BindView(R.id.left)
    TextView leftTV;
    @BindView(R.id.right)
    TextView rightTV;
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.price)
    MoneyView priceTV;
    @BindView(R.id.project)
    TextView projectTV;
    @BindView(R.id.date)
    TextView dateTV;
    @BindView(R.id.confirmTime)
    TextView confirmTimeTV;
    @BindView(R.id.deductTime)
    TextView deductTimeTV;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private MyMealDetailListAdapter.OnChildItemClickLinstener onChildItemClickLinstener;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public MyMealDetailListHolder(View itemView, MyMealDetailListAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
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
                    onChildItemClickLinstener.onChildItemClick(view, MyMealDetailListAdapter.ViewName.LEFT, getAdapterPosition());
                    return;
                case R.id.right:
                    onChildItemClickLinstener.onChildItemClick(view, MyMealDetailListAdapter.ViewName.RIGHT, getAdapterPosition());
                    return;
            }
        }
        onChildItemClickLinstener.onChildItemClick(view, MyMealDetailListAdapter.ViewName.ITEM, getAdapterPosition());
    }

    @Override
    public void setData(Appointment appointment, int position) {
        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        Goods mealGoods = appointment.getGoods();
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(mealGoods.getImage())
                        .imageView(imageIV)
                        .build());
        timeTV.setText(sdf.format(appointment.getCreateDate()));
        statusTV.setText(appointment.getStatusDesc());
        nameTV.setText(mealGoods.getName());
        priceTV.setMoneyText(String.valueOf(mealGoods.getSalePrice()));
        if ("0".equals(appointment.getIsExperience())) {
            projectTV.setVisibility(View.VISIBLE);
            projectTV.setText("项目号：" + appointment.getProjectId());
        } else {
            projectTV.setVisibility(View.INVISIBLE);
        }
        if (!ArmsUtils.isEmpty(appointment.getReservationDate())) {
            priceTV.setVisibility(View.GONE);
            dateTV.setVisibility(View.VISIBLE);
            dateTV.setText(Html.fromHtml("<font color='#666666'>预约时间：</font><font color='#333333'>" + appointment.getReservationDate() + " " + appointment.getReservationTime() + "</font>"));
        } else {
            dateTV.setVisibility(View.GONE);
            priceTV.setVisibility(View.VISIBLE);
        }

        if (!ArmsUtils.isEmpty(appointment.getConfirmTime())) {
            confirmTimeTV.setVisibility(View.VISIBLE);
            confirmTimeTV.setText(Html.fromHtml("<font color='#666666'>确认时间：</font><font color='#333333'>" + appointment.getConfirmTime() + "</font>"));
        } else {
            confirmTimeTV.setVisibility(View.GONE);
        }
        if (!ArmsUtils.isEmpty(appointment.getDeductTime())) {
            deductTimeTV.setVisibility(View.VISIBLE);
            deductTimeTV.setText(Html.fromHtml("<font color='#666666'>划扣时间：</font><font color='#333333'>" + appointment.getDeductTime() + "</font>"));
        } else {
            deductTimeTV.setVisibility(View.GONE);
        }

        if (appointment.getStatus().equals("1")) {
            statusTV.setText("可预约");
            if (!"1".equals(appointment.getIsExperience())) {
                leftTV.setVisibility(View.VISIBLE);
                leftTV.setText("复制");
            } else {
                leftTV.setVisibility(View.GONE);
            }
            rightTV.setVisibility(View.VISIBLE);
            rightTV.setText("预约");
        } else if (appointment.getStatus().equals("2")) {
            statusTV.setText("预约中");
            leftTV.setVisibility(View.VISIBLE);
            leftTV.setText("取消");
            rightTV.setVisibility(View.VISIBLE);
            rightTV.setText("改约");
        } else if (appointment.getStatus().equals("3")) {
            statusTV.setText("已预约");
            leftTV.setVisibility(View.GONE);
            rightTV.setVisibility(View.VISIBLE);
            rightTV.setText("取消");
        } else if (appointment.getStatus().equals("4")) {
            statusTV.setText("已服务");
            leftTV.setVisibility(View.VISIBLE);
            leftTV.setText("申请奖励");
            rightTV.setVisibility(View.VISIBLE);
            rightTV.setText("写日记");
        } else if (appointment.getStatus().equals("5")) {
            statusTV.setText("已转赠");
            leftTV.setVisibility(View.GONE);
            rightTV.setVisibility(View.GONE);
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
        this.timeTV = null;
        this.statusTV = null;
        this.leftTV = null;
        this.rightTV = null;
        this.imageIV = null;
        this.nameTV = null;
        this.priceTV = null;
        this.projectTV = null;
        this.dateTV = null;
        this.deductTimeTV = null;
        this.confirmTimeTV = null;
    }
}
