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
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.ui.holder.AppointmentListHolder;

/**
 * ================================================
 * 展示 {@link DefaultAdapter} 的用法
 * <p>
 * Created by JessYan on 09/04/2016 12:57
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class AppointmentListAdapter extends DefaultAdapter<Appointment> {
    private OnChildItemClickLinstener onChildItemClickLinstener;

    public AppointmentListAdapter(List<Appointment> appointmentList) {
        super(appointmentList);
    }

    @Override
    public BaseHolder<Appointment> getHolder(View v, int viewType) {
        return new AppointmentListHolder(v, new OnChildItemClickLinstener() {
            @Override
            public void onChildItemClick(View v, ViewName viewname, int position) {
                if (onChildItemClickLinstener != null) {
                    onChildItemClickLinstener.onChildItemClick(v, viewname, position);
                }
            }
        });
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.appointment_list_yimei_item;
    }

    public void setOnChildItemClickLinstener(OnChildItemClickLinstener onChildItemClickLinstener) {
        this.onChildItemClickLinstener = onChildItemClickLinstener;
    }

    public enum ViewName {
        LEFT, ITEM, RIGHT
    }

    public interface OnChildItemClickLinstener {
        void onChildItemClick(View v, AppointmentListAdapter.ViewName viewname, int position);
    }
}
