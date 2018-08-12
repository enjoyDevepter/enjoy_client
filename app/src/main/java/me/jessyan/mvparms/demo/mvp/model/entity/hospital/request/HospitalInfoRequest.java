package me.jessyan.mvparms.demo.mvp.model.entity.hospital.request;

public class HospitalInfoRequest {
    private final int cmd = 602;
    private String hospitalId;

    @Override
    public String toString() {
        return "HospitalInfoRequest{" +
                "cmd=" + cmd +
                ", hospitalId='" + hospitalId + '\'' +
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
}
