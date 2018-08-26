package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

import java.io.Serializable;

public class MemberAccount implements Serializable {
    private long amount;  // 消费币
    private long bonus;  // 现金币
    private long frozenAmount;  // 冻结消费币
    private long total;  // 总消费币
    private long point;  // 积分
    private long growth;  // 成长值。

    @Override
    public String toString() {
        return "MemberAccount{" +
                "amount=" + amount +
                ", bonus=" + bonus +
                ", frozenAmount=" + frozenAmount +
                ", total=" + total +
                ", point=" + point +
                ", growth=" + growth +
                '}';
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getGrowth() {
        return growth;
    }

    public void setGrowth(long growth) {
        this.growth = growth;
    }
}
