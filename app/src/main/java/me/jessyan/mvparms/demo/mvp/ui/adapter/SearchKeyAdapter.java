package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by guomin on 2018/7/27.
 */

public class SearchKeyAdapter extends TagAdapter {

    public SearchKeyAdapter(List<String> datas) {
        super(datas);
    }

    @Override
    public View getView(FlowLayout parent, int position, Object o) {
        return null;
    }
}
