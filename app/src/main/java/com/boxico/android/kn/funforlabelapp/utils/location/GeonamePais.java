package com.boxico.android.kn.funforlabelapp.utils.location;

/**
 * Query:http://api.geonames.org/countryInfo?username=graviton57&style=FULL&lang=ru
 * JSON Result :{"geonames": [
 {
 "continent": "EU",
 "capital": "Андорра-ла-Велья",
 "languages": "ca",
 "geonameId": 3041565,
 "south": 42.42849259876837,
 "isoAlpha3": "AND",
 "north": 42.65604389629997,
 "fipsCode": "AN",
 "population": "84000",
 "east": 1.7865427778319827,
 "isoNumeric": "020",
 "areaInSqKm": "468.0",
 "countryCode": "AD",
 "west": 1.4071867141112762,
 "countryName": "Андорра",
 "continentName": "Европа",
 "currencyCode": "EUR"
 },
 /**
 * Created by Igor Havrylyuk on 08.03.2017.
 */
public class GeonamePais {

    private String continent;
    private String capital;
    private String languages;
    private int    geonameId;
    private float  south;
    private String isoAlpha3;
    private float north;
    private String fipsCode;
    private int population;
    private float  east;
    private String isoNumeric;
    private String areaInSqKm;
    private String countryCode;
    private float  west;
    private String countryName;
    private String continentName;
    private String currencyCode;

    public GeonamePais() {
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public int getGeonameId() {
        return geonameId;
    }

    public void setGeonameId(int geonameId) {
        this.geonameId = geonameId;
    }

    public float getSouth() {
        return south;
    }

    public void setSouth(float south) {
        this.south = south;
    }

    public String getIsoAlpha3() {
        return isoAlpha3;
    }

    public void setIsoAlpha3(String isoAlpha3) {
        this.isoAlpha3 = isoAlpha3;
    }

    public float getNorth() {
        return north;
    }

    public void setNorth(float north) {
        this.north = north;
    }

    public String getFipsCode() {
        return fipsCode;
    }

    public void setFipsCode(String fipsCode) {
        this.fipsCode = fipsCode;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public float getEast() {
        return east;
    }

    public void setEast(float east) {
        this.east = east;
    }

    public String getIsoNumeric() {
        return isoNumeric;
    }

    public void setIsoNumeric(String isoNumeric) {
        this.isoNumeric = isoNumeric;
    }

    public String getAreaInSqKm() {
        return areaInSqKm;
    }

    public void setAreaInSqKm(String areaInSqKm) {
        this.areaInSqKm = areaInSqKm;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public float getWest() {
        return west;
    }

    public void setWest(float west) {
        this.west = west;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}