package me.jessyan.mvparms.demo.mvp.model.entity.user.bean;

/**
 * Created by guomin on 2018/8/27.
 */

public class CommonUserInfo {
    private String label;
    private String value;
    private boolean choice;

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "CommonUserInfo{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", choice=" + choice +
                '}';
    }
}
