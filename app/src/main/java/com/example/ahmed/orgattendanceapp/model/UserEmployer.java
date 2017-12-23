package com.example.ahmed.orgattendanceapp.model;

/**
 * Created by ahmed on 22/12/17.
 */

public class UserEmployer {

    String userEmail, userPass, companyPlace, compLat, compLon;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getCompanyPlace() {
        return companyPlace;
    }

    public void setCompanyPlace(String companyPlace) {
        this.companyPlace = companyPlace;
    }

    public String getCompLat() {
        return compLat;
    }

    public void setCompLat(String compLat) {
        this.compLat = compLat;
    }

    public String getCompLon() {
        return compLon;
    }

    public void setCompLon(String compLon) {
        this.compLon = compLon;
    }

    public UserEmployer(String userEmail, String userPass, String companyPlace, String compLat, String compLon) {
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.companyPlace = companyPlace;
        this.compLat = compLat;

        this.compLon = compLon;
    }

    public UserEmployer() {

    }
}
