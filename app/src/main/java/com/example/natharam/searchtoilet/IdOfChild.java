package com.example.natharam.searchtoilet;

public class IdOfChild {

    String idChilds;

    public IdOfChild(String idChilds, String markerId) {
        this.idChilds = idChilds;
        this.markerId = markerId;
    }

    public IdOfChild() {
    }

    String markerId;
    public String getIdChilds() {
        return idChilds;
    }

    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

}
