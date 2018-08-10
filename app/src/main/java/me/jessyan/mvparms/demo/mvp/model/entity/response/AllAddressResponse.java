package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;

/**
 * Created by guomin on 2018/7/28.
 */

public class AllAddressResponse extends BaseResponse {

    private List<AreaAddress> areaList;

    public List<AreaAddress> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaAddress> areaList) {
        this.areaList = areaList;
    }

    @Override
    public String toString() {
        return "AllAddressResponse{" +
                "areaList=" + areaList +
                '}';
    }
}
