package com.o2cinemas.beans;

import java.util.ArrayList;

/**
 * Created by user on 5/24/2016.
 */
//"length": "2:17 Hrs",
//        "download": "112154"

public class Detail_Bean {

    private String msg;
    private String status;
    private String id;
    private String title;
    private String description;
    private String category;
    private String ssname;
    private String download;
    private String length;
    private String foldername;
    private String type;
    private ArrayList<String> partsList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getPartsList() {
        return partsList;
    }

    public void setPartsList(ArrayList<String> partsList) {
        this.partsList = partsList;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getSsname() {
        return ssname;
    }

    public void setSsname(String ssname) {
        this.ssname = ssname;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
