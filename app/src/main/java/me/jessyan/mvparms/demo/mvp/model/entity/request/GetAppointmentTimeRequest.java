package me.jessyan.mvparms.demo.mvp.model.entity.request;

/**
 * Created by guomin on 2018/7/25.
 */

public class GetAppointmentTimeRequest extends BaseRequest {
    private int cmd = 2005;
    private String token;
    private String projectId;

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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "GetAppointmentTimeRequest{" +
                "cmd=" + cmd +
                ", token='" + token + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}