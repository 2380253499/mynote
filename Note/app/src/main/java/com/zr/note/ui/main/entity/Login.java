package com.zr.note.ui.main.entity;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Login  {
/*
log-key[equipmentName: ]########[HUAWEI HN3-U01]
log-key[username: ]########[lidm02]
log-key[source: ]########[agencyApp]
log-key[token: ]########[5f626066c4dc4384b1185bf390bb671c]
log-key[att: ]########[121.43884]
log-key[appSys: ]########[android]
log-key[appModel: ]########[864572010933342]
log-key[password: ]########[Ln1need2016100909]
log-key[lat: ]########[31.221374]
log-key[randomTime: ]########[1475977705]
log-key[version: ]########[24.0]*/
    private String equipmentName;
    private String username;
    private String source;
    private String appSys;
    private String appModel;
    private String password;
    private String randomTime;
    private String version;

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAppSys() {
        return appSys;
    }

    public void setAppSys(String appSys) {
        this.appSys = appSys;
    }

    public String getAppModel() {
        return appModel;
    }

    public void setAppModel(String appModel) {
        this.appModel = appModel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRandomTime() {
        return randomTime;
    }

    public void setRandomTime(String randomTime) {
        this.randomTime = randomTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
