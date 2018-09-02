package me.jessyan.mvparms.demo.mvp.model.entity.user.response;

import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;

public class UpdateResponse extends BaseResponse {

    private String description;
    private String url;
    private String upgradeVersion;
    private boolean needUpgrade;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpgradeVersion() {
        return upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }

    public boolean isNeedUpgrade() {
        return needUpgrade;
    }

    public void setNeedUpgrade(boolean needUpgrade) {
        this.needUpgrade = needUpgrade;
    }

    @Override
    public String toString() {
        return "UpdateResponse{" +
                "description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", upgradeVersion='" + upgradeVersion + '\'' +
                ", needUpgrade=" + needUpgrade +
                '}';
    }
}
