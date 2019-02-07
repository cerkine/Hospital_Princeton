package model;

import java.time.LocalDate;
import java.time.Period;

public class Persona  {
    public enum Genere {
        HOME,DONA
    }

    private String Nom;
    private String Cognoms;
    private String Email;
    private LocalDate DataNaixament;
    private Genere genere;

    public Persona(String Nom, String Cognoms, Genere g, LocalDate dataN) {
        this.Nom=Nom;
        this.Cognoms=Cognoms;
        genere=g;
        DataNaixament=dataN;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getCognoms() {
        return Cognoms;
    }

    public void setCognoms(String cognoms) {
        Cognoms = cognoms;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public LocalDate getDataNaixament() {
        return DataNaixament;
    }

    public void setDataNaixament(LocalDate dataNaixament) {
        DataNaixament = dataNaixament;
    }


    public int getEdat() {
        return Period.between(getDataNaixament(), LocalDate.now()).getYears();
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }
}
