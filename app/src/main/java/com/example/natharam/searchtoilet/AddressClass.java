package com.example.natharam.searchtoilet;


public class AddressClass {


    String nameOfPlace;
    String nameOfCity;

    double latti;
    double lngti;
    public AddressClass() {
    }

    public AddressClass(String nameOfPlace, String nameOfCity, double latti, double lngti) {
        this.nameOfPlace = nameOfPlace;
        this.nameOfCity = nameOfCity;

        this.latti = latti;
        this.lngti = lngti;
    }

    public double getLatti() {
        return latti;
    }

    public void setLatti(double latti) {
        this.latti = latti;
    }

    public double getLngti() {
        return lngti;
    }

    public void setLngti(double lngti) {
        this.lngti = lngti;
    }





    public String getNameOfPlace() {
        return nameOfPlace;
    }

    public void setNameOfPlace(String nameOfPlace) {
        this.nameOfPlace = nameOfPlace;
    }

    public String getNameOfCity() {
        return nameOfCity;
    }

    public void setNameOfCity(String nameOfCity) {
        this.nameOfCity = nameOfCity;
    }
}
