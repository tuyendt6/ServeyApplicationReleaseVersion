package com.samsung.object;

/**
 * Created by SamSunger on 5/14/2015.
 */
public class Dealer {

    private String PKID;
    private String DealerName;
    private String Distric;
    private String City;
    private String Adress;
    private String Latitud;
    private String LongGitude;
    private String Status;
    private String NameZone;
    private String PhoneNumber;

    public Dealer(String PKID, String dealerName, String distric, String city, String adress, String latitud, String longGitude, String status, String nameZone, String phoneNumber) {
        if (dealerName == null) {
            dealerName = "";
        }
        if (distric == null) {
            distric = "";
        }
        if (city == null) {
            city = "";
        }
        if (adress == null) {
            adress = "";
        }
        if (latitud == null) {
            latitud = "";
        }
        if (longGitude == null) {
            longGitude = "";
        }
        if (status == null) {
            status = "";
        }
        if (nameZone == null) {
            nameZone = "";
        }
        if (phoneNumber == null) {
            phoneNumber = "";
        }


        DealerName = dealerName;
        Distric = distric;
        City = city;
        Adress = adress;
        Latitud = latitud;
        LongGitude = longGitude;
        Status = status;
        NameZone = nameZone;
        PhoneNumber = phoneNumber;
        this.PKID=PKID;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getDistric() {
        return Distric;
    }

    public void setDistric(String distric) {
        Distric = distric;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongGitude() {
        return LongGitude;
    }

    public void setLongGitude(String longGitude) {
        LongGitude = longGitude;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getNameZone() {
        return NameZone;
    }

    public void setNameZone(String nameZone) {
        NameZone = nameZone;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPKID() {
        return PKID;
    }

    public void setPKID(String PKID) {
        this.PKID = PKID;
    }

    @Override
    public String toString() {
        return PKID+" : "+DealerName+" : "+Latitud +" : " +LongGitude;
    }
}
