package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/29.
 */

public class MealOrderConfrimRequest extends BaseRequest {
    private int cmd = 513;
    private int money;
    private String token;
    private MealGoods setMealGoods;

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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public MealGoods getSetMealGoods() {
        return setMealGoods;
    }

    public void setSetMealGoods(MealGoods setMealGoods) {
        this.setMealGoods = setMealGoods;
    }

    @Override
    public String toString() {
        return "MealOrderConfrimRequest{" +
                "cmd=" + cmd +
                ", money=" + money +
                ", token='" + token + '\'' +
                ", setMealGoods=" + setMealGoods +
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
