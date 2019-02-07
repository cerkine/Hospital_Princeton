package model;

import java.time.LocalDate;

public class Pacient extends Persona {
    private float Pes;
    private int Alçada;
    private String Telefon, DNI;

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String dni) {
        DNI = dni;
    }

    public Pacient(String dni, String Nom, String Cognoms, LocalDate dataN, Genere g, String telf, float p, int a) {
        super(Nom, Cognoms, g, dataN);
        DNI = dni;
        Pes = p;
        Alçada = a;
        Telefon = telf;

    }

    public float getPes() {
        return Pes;
    }

    public void setPes(float pes) {
        Pes = pes;
    }

    public int getAlçada() {
        return Alçada;
    }

    public void setAlçada(int alçada) {
        Alçada = alçada;
    }


}