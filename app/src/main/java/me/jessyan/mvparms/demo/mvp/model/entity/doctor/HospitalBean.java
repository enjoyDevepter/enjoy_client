package me.jessyan.mvparms.demo.mvp.model.entity.doctor;

public class HospitalBean {
    private String hospitalId;
    private String name;

    @Override
    public String toString() {
        return "HospitalBean{" +
                "hospitalId='" + hospitalId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}
