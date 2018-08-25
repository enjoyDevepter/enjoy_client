package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/24.
 */

public class MallPageChange {

    private int nnaviIndex;
    private int bottomIndex;

    public MallPageChange(int nnaviIndex, int bottomIndex) {
        this.nnaviIndex = nnaviIndex;
        this.bottomIndex = bottomIndex;
    }

    public int getNnaviIndex() {
        return nnaviIndex;
    }

    public void setNnaviIndex(int nnaviIndex) {
        this.nnaviIndex = nnaviIndex;
    }

    public int getBottomIndex() {
        return bottomIndex;
    }

    public void setBottomIndex(int bottomIndex) {
        this.bottomIndex = bottomIndex;
    }
}
