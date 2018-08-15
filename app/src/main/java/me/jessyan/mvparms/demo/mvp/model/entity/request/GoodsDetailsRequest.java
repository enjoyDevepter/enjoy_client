package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/29.
 */

public class GoodsDetailsRequest extends BaseRequest {
    private int cmd;
    private String name;
    private String province;
    private String city;
    private String county;
    private String goodsId;
    private String merchId;
    private String specValueId;
    private String advanceDepositId;
    private String token;


    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(String specValueId) {
        this.specValueId = specValueId;
    }

    public String getAdvanceDepositId() {
        return advanceDepositId;
    }

    public void setAdvanceDepositId(String advanceDepositId) {
        this.advanceDepositId = advanceDepositId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "GoodsDetailsRequest{" +
                "cmd=" + cmd +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", merchId='" + merchId + '\'' +
                ", specValueId='" + specValueId + '\'' +
                ", advanceDepositId='" + advanceDepositId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
