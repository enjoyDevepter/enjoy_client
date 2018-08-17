package me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean;

public class OrderBy {
    private String field;
    // 是否升序
    private boolean asc;

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "OrderBy{" +
                "field='" + field + '\'' +
                ", asc=" + asc +
                '}';
    }
}
