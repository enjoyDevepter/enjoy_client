package me.jessyan.mvparms.demo.mvp.model.entity.user.request;

import me.jessyan.mvparms.demo.mvp.model.entity.request.BaseRequest;

/**
 * Created by guomin on 2018/7/25.
 */

public class FollowRequest extends BaseRequest {
    private int cmd;
    private String token;
    private String memberId;
    private String hospitalId;
    private String doctorId;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "FollowRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", memberId='" + memberId + '\'' +
                ", hospitalId='" + hospitalId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                '}';
    }
}
