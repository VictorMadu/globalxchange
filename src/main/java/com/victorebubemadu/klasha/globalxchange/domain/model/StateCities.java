package com.victorebubemadu.klasha.globalxchange.domain.model;

public class StateCities {

    private String stateName;
    private String[] cities;

    public StateCities(String stateName, String[] cities) {
        this.stateName = stateName;
        this.cities = cities;
    }

    public String state() {
        return stateName;
    }

    public String[] cities() {
        return cities;
    }
}
