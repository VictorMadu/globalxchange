package com.victorebubemadu.klasha.globalxchange.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateRegions {

    @JsonProperty()
    private String state;

    @JsonProperty()
    private String[] cities;

    public StateRegions(StateCities stateCities) {
        this.state = stateCities.state();
        this.cities = stateCities.cities();
    }

    public boolean is(String b) {
        return state.compareToIgnoreCase(b) == 0;
    }

    public boolean hasCity(String city) {
        // TODO: Use binary search for performance
        for (String c : cities) {
            if (c.compareToIgnoreCase(city) == 0)
                return true;
        }

        return false;
    }

    public String name() {
        return state;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StateRegions other = (StateRegions) obj;

        if (!this.state.equals(other.state)) {
            return false;
        }

        if (this.cities.length != other.cities.length) {
            return false;
        }

        for (int i = 0; i < this.cities.length; i++) {
            if (!this.cities[i].equals(other.cities[i])) {
                return false;
            }
        }

        return true;
    }

}
