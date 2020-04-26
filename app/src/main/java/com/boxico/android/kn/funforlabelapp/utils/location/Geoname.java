package com.boxico.android.kn.funforlabelapp.utils.location;

public class Geoname implements Comparable {
    public double lat;
    public double lng;
    public long geonameId;
    public String countrycode;
    public String name;
    public String fclName;
    public String toponymName;
    public String fcodeName;
    public String wikipedia;
    public String fcl;
    public long population;
    public String fcode;

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        String result = name;
        if(name.contains("Partido de")){
            result = name.replaceAll("Partido de","");
        }else if(name.contains("Departamento de")){
            result = name.replaceAll("Departamento de","");
        }

        return result;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getGeonameId() {
        return geonameId;
    }

    public void setGeonameId(long geonameId) {
        this.geonameId = geonameId;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFclName() {
        return fclName;
    }

    public void setFclName(String fclName) {
        this.fclName = fclName;
    }

    public String getToponymName() {
        return toponymName;
    }

    public void setToponymName(String toponymName) {
        this.toponymName = toponymName;
    }

    public String getFcodeName() {
        return fcodeName;
    }

    public void setFcodeName(String fcodeName) {
        this.fcodeName = fcodeName;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public String getFcl() {
        return fcl;
    }

    public void setFcl(String fcl) {
        this.fcl = fcl;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    @Override
    public int compareTo(Object o) {
        Geoname other = (Geoname) o;
        return name.compareTo(other.getName());
    }
}