package me.jessyan.mvparms.demo.mvp.model.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by guomin on 2018/7/25.
 */

public class OrderConfirmInfoRequest extends BaseRequest {

    private int cmd = 503;
    private String province;
    private String city;
    private String county;
    private String deliveryMethod;
    private long money;
    private String couponId;
    private String token;
    private List<OrderGoods> goodsList;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
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

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<OrderGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoods> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return "OrderConfirmInfoRequest{" +
                "cmd=" + cmd +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", county='" + county + '\'' +
                ", deliveryMethod='" + deliveryMethod + '\'' +
                ", money=" + money +
                ", couponId='" + couponId + '\'' +
                ", token='" + token + '\'' +
                ", goodsList=" + goodsList +
                '}';
    }

    public static class OrderGoods implements Parcelable {
        public static final Creator<OrderGoods> CREATOR = new Creator<OrderGoods>() {
            @Override
            public OrderGoods createFromParcel(Parcel in) {
                return new OrderGoods(in);
            }

            @Override
            public OrderGoods[] newArray(int size) {
                return new OrderGoods[size];
            }
        };
        private String goodsId;
        private String merchId;
        private int nums;
        private String promotionId;
        private double salePrice;

        public OrderGoods() {
        }

        public OrderGoods(Parcel in) {
            goodsId = in.readString();
            merchId = in.readString();
            nums = in.readInt();
            promotionId = in.readString();
            salePrice = in.readDouble();
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

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public String getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(String promotionId) {
            this.promotionId = promotionId;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        @Override
        public String toString() {
            return "OrderGoods{" +
                    "goodsId='" + goodsId + '\'' +
                    ", merchId='" + merchId + '\'' +
                    ", nums=" + nums +
                    ", promotionId='" + promotionId + '\'' +
                    ", salePrice=" + salePrice +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(goodsId);
            dest.writeString(merchId);
            dest.writeInt(nums);
            dest.writeString(promotionId);
            dest.writeDouble(salePrice);
        }
    }
}