package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.DiaryNavi;

/**
 * Created by guomin on 2018/7/28.
 */

public class DiaryNaviListResponse extends BaseResponse {
    private List<DiaryNavi> navList;

    public List<DiaryNavi> getNavList() {
        return navList;
    }

    public void setNavList(List<DiaryNavi> navList) {
        this.navList = navList;
    }

    @Override
    public String toString() {
        return "DiaryNaviListResponse{" +
                "navList=" + navList +
                '}';
    }
}
