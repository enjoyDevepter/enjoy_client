package me.jessyan.mvparms.demo.mvp.model.entity.hospital.request;

public class LoginUserHospitalInfoRequest {
    private final int cmd = 603;
    private String hospitalId;
    private String token;

    @Override
    public String toString() {
        return "LoginUserHospitalInfoRequest{" +
                "cmd=" + cmd +
                ", hospitalId='" + hospitalId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getCmd() {
        return cmd;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
