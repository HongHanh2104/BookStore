package com.example.honghanh.bookstoremanager.data;

import java.io.Serializable;

public class Bookstore implements Serializable {
    private String id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private String overview;
    private String posterPath;
    private String bigPosterPath;

    public Bookstore(String id, String name, String address, String telephone, String email,
                     String overview, String posterPath, String bigPosterPath) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
        this.overview = overview;
        this.bigPosterPath = bigPosterPath;
        this.posterPath = posterPath;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }
}