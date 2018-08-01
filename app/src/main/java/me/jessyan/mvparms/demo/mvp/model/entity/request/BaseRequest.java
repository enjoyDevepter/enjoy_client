package me.jessyan.mvparms.demo.mvp.model.entity.request;


import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.DeviceUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * 公共请求字段
 */
public class BaseRequest implements Serializable {

    private transient static final long serialVersionUID = 1L;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private String sequence = UUID.randomUUID().toString();
    private String ip = DeviceUtils.getIPAddress(ArmsUtils.getContext());
    private String timestamp = sdf.format(System.currentTimeMillis()).toString();
    private String channel = "cx";
    private String childChannel = "91f";
    private String version = String.valueOf(1.0);
    private String clientVersion = DeviceUtils.getVersionName(ArmsUtils.getContext());
    private String deviceNumber = DeviceUtils.getIMEI(ArmsUtils.getContext());

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChildChannel() {
        return childChannel;
    }

    public void setChildChannel(String childChannel) {
        this.childChannel = childChannel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}
