package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorSkill;
import me.jessyan.mvparms.demo.mvp.ui.widget.RatingBar;

public class DoctorListHolder extends BaseHolder<DoctorBean> {

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.hosptial)
    TextView hosptial;

    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.skill)
    TagFlowLayout skill;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;

    public DoctorListHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(DoctorBean data, int position) {
        Observable.just(data.getName())
                .subscribe(s -> name.setText(String.valueOf(s)));
        if (null != data.getHospitalList() && data.getHospitalList().size() > 0) {
            Observable.just(data.getHospitalList().get(0).getName())
                    .subscribe(s -> hosptial.setText(String.valueOf(s)));
        }
        Observable.just(data.getStar())
                .subscribe(s -> rating.setStar(s));

        List<DoctorSkill> doctorSkillList = data.getDoctorSkillList();
        List<String> skillList = new ArrayList<>(doctorSkillList.size());
        for (int i = 0; i < doctorSkillList.size(); i++) {
            skillList.add(doctorSkillList.get(i).getProjectName());
        }
        skill.setAdapter(new SkillAdapter(skillList));

        //itemView 的 Context 就是 Activity, Glide 会自动处理并和该 Activity 的生命周期绑定
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(data.getHeadImage())
                        .imageView(image)
                        .build());
    }

    @Override
    protected void onRelease() {
        image = null;
        name = null;
        hosptial = null;
        rating = null;
        skill = null;
    }

    private class SkillAdapter extends TagAdapter<String> {

        public SkillAdapter(List<String> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, String s) {
            TextView tv = (TextView) LayoutInflater.from(ArmsUtils.getContext()).inflate(R.layout.search_hot_item, null, false);
            tv.setText(s);
            return tv;
        }
    }
}
