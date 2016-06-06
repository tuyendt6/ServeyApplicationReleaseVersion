package com.samsung.object;

/**
 * Created by Computer on 4/6/2016.
 */
public class Corregimientos {
    private String CorregimientosID;
    private String DistritoID;
    private String CorregimientosName;

    public Corregimientos(String corregimientosID, String distritoID, String corregimientosName) {
        CorregimientosID = corregimientosID;
        DistritoID = distritoID;
        CorregimientosName = corregimientosName;
    }

    public String getCorregimientosID() {
        return CorregimientosID;
    }

    public String getDistritoID() {
        return DistritoID;
    }

    public String getCorregimientosName() {
        return CorregimientosName;
    }

    public void setCorregimientosID(String corregimientosID) {
        CorregimientosID = corregimientosID;
    }

    @Override
    public String toString() {
        return CorregimientosName;
    }
}
