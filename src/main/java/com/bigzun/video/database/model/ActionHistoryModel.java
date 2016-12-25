package com.bigzun.video.database.model;

import java.io.Serializable;

/**
 * Created by Admin on 8/19/2016.
 */
public class ActionHistoryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id = -1;
    private long userId = -1;
    private String deviceId = "";
    private long itemId = -1;
    private String itemType = "";
    private String itemInfo = "";
    private String actionType = "";
    private String actionContent = "";
    private long datetime = System.currentTimeMillis();

    public ActionHistoryModel() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionContent() {
        return actionContent;
    }

    public void setActionContent(String actionContent) {
        this.actionContent = actionContent;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
