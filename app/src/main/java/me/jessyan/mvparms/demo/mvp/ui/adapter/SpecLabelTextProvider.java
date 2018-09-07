package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.widget.TextView;

import me.jessyan.mvparms.demo.mvp.model.entity.GoodsSpecValue;
import me.jessyan.mvparms.demo.mvp.ui.widget.LabelsView;

/**
 * Created by guomin on 2018/9/8.
 */
public class SpecLabelTextProvider implements LabelsView.LabelTextProvider<GoodsSpecValue> {
    @Override
    public CharSequence getLabelText(TextView label, int position, GoodsSpecValue data) {
        return data.getSpecValueName();
    }
}
