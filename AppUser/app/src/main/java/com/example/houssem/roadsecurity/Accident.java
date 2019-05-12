package com.example.houssem.roadsecurity;

import java.sql.Time;

public class Accident {
    private String time,email,cause;
    private String temp,wind,nb_inj,nb_mort,lati,longi,weather;

    public Accident(){

    }

    public Accident(String time, String email, String cause, String temp, String wind, String nb_inj, String nb_mort, String lati, String longi, String weather) {
        this.time = time;
        this.email = email;
        this.cause = cause;
        this.temp = temp;
        this.wind = wind;
        this.nb_inj = nb_inj;
        this.nb_mort = nb_mort;
        this.lati = lati;
        this.longi = longi;
        this.weather = weather;
    }

    public String getTime() {
        return time;
    }

    public String getEmail() {
        return email;
    }

    public String getCause() {
        return cause;
    }

    public String getTemp() {
        return temp;
    }

    public String getWind() {
        return wind;
    }

    public String getNb_inj() {
        return nb_inj;
    }

    public String getNb_mort() {
        return nb_mort;
    }

    public String getLati() {
        return lati;
    }

    public String getLongi() {
        return longi;
    }

    public String getWeather() {
        return weather;
    }
}
