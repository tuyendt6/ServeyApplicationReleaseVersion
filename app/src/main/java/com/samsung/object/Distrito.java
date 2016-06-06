package com.samsung.object;

/**
 * Created by SamSunger on 5/16/2015.
 */
public class Distrito {
    private String DistritoID;
    private String ProvincialID;
    private String DistritoName;

    public Distrito(String distritoID, String provincialID, String distritoName) {
        DistritoID = distritoID;
        ProvincialID = provincialID;
        DistritoName = distritoName;
    }

    public String getDistritoID() {
        return DistritoID;
    }

    public void setDistritoID(String distritoID) {
        DistritoID = distritoID;
    }

    public String getProvincialID() {
        return ProvincialID;
    }

    public void setProvincialID(String provincialID) {
        ProvincialID = provincialID;
    }

    public String getDistritoName() {
        return DistritoName;
    }

    public void setDistritoName(String distritoName) {
        DistritoName = distritoName;
    }

    @Override
    public String toString() {
        return DistritoName;
    }
}
