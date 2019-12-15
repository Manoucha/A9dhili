package com.android.a9dhili.Models;

import java.io.Serializable;

public class Post implements Serializable {
    private  int id;
    private String contenu;
    private String tags;
    private int etat;
    private String id_user;
    private float tarif;
    private double longitude;
    private double latitude;

    public Post(String contenu, String tags, int etat, String id_user, float tarif, double longitude, double latitude) {
        this.contenu = contenu;
        this.tags = tags;
        this.etat = etat;
        this.id_user = id_user;
        this.tarif = tarif;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Post(int id,String contenu, String tags, int etat, String id_user, float tarif, double longitude, double latitude) {
        this.contenu = contenu;
        this.tags = tags;
        this.etat = etat;
        this.id_user = id_user;
        this.tarif = tarif;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public float getTarif() {
        return tarif;
    }

    public void setTarif(float tarif) {
        this.tarif = tarif;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

