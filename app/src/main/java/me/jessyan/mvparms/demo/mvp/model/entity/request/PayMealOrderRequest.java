package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/29.
 */

public class PayMealOrderRequest extends BaseRequest {
    private int cmd = 514;
    private String token;
    private MealGoods setMealGoods;
    private long money;
    private long price;
    private long totalPrice;
    private long payMoney;
    private String remark;
    private String memberAddressId;

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

    public MealGoods getSetMealGoods() {
        return setMealGoods;
    }

    public void setSetMealGoods(MealGoods setMealGoods) {
        this.setMealGoods = setMealGoods;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemberAddressId() {
        return memberAddressId;
    }

    public void setMemberAddressId(String memberAddressId) {
        this.memberAddressId = memberAddressId;
    }

    @Override
    public String toString() {
        return "PayMealOrderRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", setMealGoods=" + setMealGoods +
                ", money=" + money +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", payMoney=" + payMoney +
                ", remark='" + remark + '\'' +
                ", memberAddressId='" + memberAddressId + '\'' +
                '}';
    }

    public static class MealGoods {
        private double totalPrice;
        private String setMealId;
        private double salePrice;
        private int nums;

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getSetMealId() {
            return setMealId;
        }

        public void setSetMealId(String setMealId) {
            this.setMealId = setMealId;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        @Override
        public String toString() {
            return "MealGoods{" +
                    "totalPrice=" + totalPrice +
                    ", setMealId='" + setMealId + '\'' +
                    ", salePrice=" + salePrice +
                    ", nums=" + nums +
                    '}';
        }
    }
}
