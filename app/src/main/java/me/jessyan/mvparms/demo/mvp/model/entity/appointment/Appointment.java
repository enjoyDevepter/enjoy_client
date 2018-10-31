package me.jessyan.mvparms.demo.mvp.model.entity.appointment;

import me.jessyan.mvparms.demo.mvp.model.entity.Goods;

/**
 * Created by guomin on 2018/8/19.
 */
public class Appointment {

    private String projectId;
    private String reservationDate;
    private String reservationTime;
    private String reservationId;
    private String isExperience;
    private String confirmTime;
    private String deductTime;
    private int surplusNum;
    private String desc;
    private String reservationStatus;
    private String status;
    private String statusDesc;
    private long createDate;
    private String code;
    private String relateStatus;
    private String relateStatusDesc;
    private String phone;
    private String address;
    private Goods goods;
    private Hospital hospital;
    private Member member;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getIsExperience() {
        return isExperience;
    }

    public void setIsExperience(String isExperience) {
        this.isExperience = isExperience;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getDeductTime() {
        return deductTime;
    }

    public void setDeductTime(String deductTime) {
        this.deductTime = deductTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public int getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(int surplusNum) {
        this.surplusNum = surplusNum;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getRelateStatus() {
        return relateStatus;
    }

    public void setRelateStatus(String relateStatus) {
        this.relateStatus = relateStatus;
    }

    public String getRelateStatusDesc() {
        return relateStatusDesc;
    }

    public void setRelateStatusDesc(String relateStatusDesc) {
        this.relateStatusDesc = relateStatusDesc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "projectId='" + projectId + '\'' +
                ", reservationDate='" + reservationDate + '\'' +
                ", reservationTime='" + reservationTime + '\'' +
                ", reservationId='" + reservationId + '\'' +
                ", isExperience='" + isExperience + '\'' +
                ", confirmTime='" + confirmTime + '\'' +
                ", deductTime='" + deductTime + '\'' +
                ", surplusNum=" + surplusNum +
                ", desc='" + desc + '\'' +
                ", reservationStatus='" + reservationStatus + '\'' +
                ", status='" + status + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", createDate=" + createDate +
                ", code='" + code + '\'' +
                ", relateStatus='" + relateStatus + '\'' +
                ", relateStatusDesc='" + relateStatusDesc + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", goods=" + goods +
                ", hospital=" + hospital +
                ", member=" + member +
                '}';
    }

    public class Hospital {
        private String hospitalId;
        private String name;

        public String getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(String hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Hospital{" +
                    "hospitalId='" + hospitalId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public class Member {
        private String memberId;
        private String memberName;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        @Override
        public String toString() {
            return "Member{" +
                    "memberId='" + memberId + '\'' +
                    ", memberName='" + memberName + '\'' +
                    '}';
        }
    }
}
