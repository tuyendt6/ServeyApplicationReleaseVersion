package com.samsung.object;

import com.google.gson.annotations.SerializedName;
import com.samsung.table.tblEncuestaDisenos;

/**
 * Created by SamSunger on 5/16/2015.
 */
public class ServeyOject {

    private  String PK_ID ;
    private  String NOMBRE ;
    private  String DESCRIPCION ;
    private  String ACTIVO  ;
    private  String TIMEDONE ;



    public ServeyOject(String PK_ID, String NOMBRE, String DESCRIPCION, String ACTIVO, String TIMEDONE) {
        this.PK_ID = PK_ID;
        this.NOMBRE = NOMBRE;
        this.DESCRIPCION = DESCRIPCION;
        this.ACTIVO = ACTIVO;
        this.TIMEDONE = TIMEDONE;

    }

    public String getPK_ID() {
        return PK_ID;
    }

    public void setPK_ID(String PK_ID) {
        this.PK_ID = PK_ID;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public void setDESCRIPCION(String DESCRIPCION) {
        this.DESCRIPCION = DESCRIPCION;
    }

    public String getACTIVO() {
        return ACTIVO;
    }

    public void setACTIVO(String ACTIVO) {
        this.ACTIVO = ACTIVO;
    }

    public String getTIMEDONE() {
        return TIMEDONE;
    }

    public void setTIMEDONE(String TIMEDONE) {
        this.TIMEDONE = TIMEDONE;
    }


}
