package me.jessyan.mvparms.demo.mvp.model.entity;

/**
 * Created by guomin on 2018/8/14.
 */

public class Promotion {
    private String promotionId;
    private String rule;
    private String title;
    private String type;
    private String typeDesc;
    private boolean check;

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "promotionId='" + promotionId + '\'' +
                ", rule='" + rule + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", typeDesc='" + typeDesc + '\'' +
                ", check=" + check +
                '}';
    }
}
