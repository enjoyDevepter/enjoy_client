package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.ArrayList;

import me.jessyan.mvparms.demo.mvp.model.entity.Area;

/**
 * Created by guomin on 2018/7/25.
 */

public class CityResponse extends BaseResponse {

    private ArrayList<Area> areaList;

    public ArrayList<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(ArrayList<Area> areaList) {
        this.areaList = areaList;
    }

}
