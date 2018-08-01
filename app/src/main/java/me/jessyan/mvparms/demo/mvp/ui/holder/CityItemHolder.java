package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yu.bundles.extended.recyclerview.ExtendedHolder;
import com.yu.bundles.extended.recyclerview.ExtendedNode;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewHelper;

import java.util.ArrayList;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;

public class CityItemHolder extends ExtendedHolder<Area> implements View.OnLongClickListener, View.OnTouchListener {
    private TextView textView;
    private OnChoiceListener onChoiceListener;

    public CityItemHolder(ExtendedRecyclerViewHelper helper, View itemView, OnChoiceListener onChoiceListener) {
        super(helper, itemView);
        textView = itemView.findViewById(R.id.textView);
        itemView.setOnLongClickListener(this);
        itemView.setOnTouchListener(this);
        this.onChoiceListener = onChoiceListener;
    }

    @Override
    public void setData(ExtendedNode<Area> node) {
        textView.setText(node.data.getName());
    }

    @Override
    public boolean onLongClick(View v) {
        return true;
    }

    @Override
    protected View getExtendedClickView() {
        return itemView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                ArrayList<ExtendedNode> sons = helper.getNode(getLayoutPosition()).getSons();
                if (null == sons || (sons != null && sons.size() <= 0)) {
                    if (null != onChoiceListener) {
                        onChoiceListener.onChoice((Area) helper.getNode(getLayoutPosition()).data);
                    }
                }
                break;
        }
        return false;
    }

    public interface OnChoiceListener {
        void onChoice(Area area);
    }
}
