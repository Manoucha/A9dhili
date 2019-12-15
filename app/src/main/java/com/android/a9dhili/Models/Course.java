package com.android.a9dhili.Models;

public class Course {

    private String idCourseur ;
    private String idClient ;
    private int etat ;
    private int idPost ;

    public Course(String idCourseur, String idClient, int etat, int idPost) {
        this.idCourseur = idCourseur;
        this.idClient = idClient;
        this.etat = etat;
        this.idPost = idPost;
    }

    public String getIdCourseur() {
        return idCourseur;
    }

    public void setIdCourseur(String idCourseur) {
        this.idCourseur = idCourseur;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }
}
