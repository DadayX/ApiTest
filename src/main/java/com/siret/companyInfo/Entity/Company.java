package com.siret.companyInfo.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Company {
    private String siret;
    private String nic;
    private String full_address;
    private String creation_date;
    private String Full_name;



    public Company(String nic, String full_address, String creation_date, String full_name) {
        this.nic = nic;
        this.full_address = full_address;
        this.creation_date = creation_date;
        this.Full_name = full_name;
    }
    public Company(){
        this.nic = null;
        this.full_address = null;
        this.creation_date = null;
        this.Full_name = null;
    }
    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getFull_name() {
        return Full_name;
    }

    public void setFull_name(String full_name) {
        Full_name = full_name;
    }
}
