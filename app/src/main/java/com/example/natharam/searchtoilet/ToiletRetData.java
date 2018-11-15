package com.example.natharam.searchtoilet;

public class ToiletRetData {
    private String nameOfToilet;
    private String addressLocat;
    private String modOfToilet;
    private String costOfToilet;
    private double lat;
    private   double lng;
    private    String id;
    private  String idChild;

    public ToiletRetData() {



    }


    public ToiletRetData(String nameOfToilet, String addressLocat, String modOfToilet, String costOfToilet, double lat, double lng, String id,String idChild) {
        this.nameOfToilet = nameOfToilet;
        this.addressLocat = addressLocat;
        this.modOfToilet = modOfToilet;
        this.costOfToilet = costOfToilet;
        this.lat = lat;
        this.lng = lng;
        this.id = id;
        this.idChild=idChild;
    }

    public String getIdChild() {
        return idChild;
    }

    public void setIdChild(String idChild) {
        this.idChild = idChild;
    }

    public String getNameOfToilet() {
        return nameOfToilet;
    }

    public void setNameOfToilet(String nameOfToilet) {
        this.nameOfToilet = nameOfToilet;
    }

    public String getAddressLocat(){return addressLocat;}
    public  void setAddressLocat(){this.addressLocat=addressLocat;}

    public String getModOfToilet() {
        return modOfToilet;
    }

    public void setModOfToilet(String modOfToilet) {
        this.modOfToilet = modOfToilet;
    }

    public String getCostOfToilet() {
        return costOfToilet;
    }

    public void setCostOfToilet(String costOfToilet) {
        this.costOfToilet = costOfToilet;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

