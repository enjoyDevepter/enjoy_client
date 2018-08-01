package me.jessyan.mvparms.demo.mvp.model.entity.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.Ad;
import me.jessyan.mvparms.demo.mvp.model.entity.Module;
import me.jessyan.mvparms.demo.mvp.model.entity.NaviInfo;

/**
 * Created by guomin on 2018/7/25.
 */

public class HomeResponse extends BaseResponse {

    private List<Ad> carouselList;
    private List<Module> moduleList;
    private List<NaviInfo> firstNavList;
    private List<NaviInfo> secondNavList;

    public List<Ad> getCarouselList() {
        return carouselList;
    }

    public void setCarouselList(List<Ad> carouselList) {
        this.carouselList = carouselList;
    }

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public List<NaviInfo> getFirstNavList() {
        return firstNavList;
    }

    public void setFirstNavList(List<NaviInfo> firstNavList) {
        this.firstNavList = firstNavList;
    }

    public List<NaviInfo> getSecondNavList() {
        return secondNavList;
    }

    public void setSecondNavList(List<NaviInfo> secondNavList) {
        this.secondNavList = secondNavList;
    }

    @Override
    public String toString() {
        return "HomeResponse{" +
                "carouselList=" + carouselList +
                ", moduleList=" + moduleList +
                ", firstNavList=" + firstNavList +
                ", secondNavList=" + secondNavList +
                '}';
    }


}
