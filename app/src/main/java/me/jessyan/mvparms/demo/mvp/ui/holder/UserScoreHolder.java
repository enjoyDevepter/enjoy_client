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

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.score.ScorePointBean;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class UserScoreHolder extends BaseHolder<ScorePointBean> {

    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.score_num)
    TextView score_num;
    @BindView(R.id.fuhao)
    TextView fuhao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public UserScoreHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ScorePointBean scorePointBean, int position) {
        type.setText(scorePointBean.getType());
        time.setText(simpleDateFormat.format(new Date(scorePointBean.getCreateDate())));
        long inMoney = scorePointBean.getInMoney();
        if(inMoney == 0){
            score_num.setText(""+ scorePointBean.getOutMoney());
            fuhao.setText("-");
        }else{
            score_num.setText(""+ scorePointBean.getInMoney());
            fuhao.setText("+");
        }
    }


    /**
     * 在 Activity 的 onDestroy 中使用 {@link DefaultAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link BaseHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    @Override
    protected void onRelease() {
        this.type = null;
        this.time = null;
        this.score_num = null;
        this.fuhao = null;
    }
}
