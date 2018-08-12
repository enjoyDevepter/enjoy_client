package me.jessyan.mvparms.demo.mvp.model.entity.hospital.response;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalInfoBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class HospitalInfoResponse extends BaseResponse {
    private HospitalInfoBean hospital;
    private List<HospitalEnvBean> hospitalEnvList;
}
