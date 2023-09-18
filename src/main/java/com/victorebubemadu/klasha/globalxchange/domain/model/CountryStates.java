package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.util.Arrays;

public class CountryStates {
    private String countryName;
    private String[] states;

    public CountryStates(String countryName, String[] states) {
        states = Arrays.copyOf(states, states.length);
        Arrays.sort(states);

        this.countryName = countryName;
        this.states = states;
    }

    public boolean hasState(String state) {
        int index = Arrays.binarySearch(this.states, state);
        return index >= 0;
    }

    public String[] states() {
        return states;
    }

    public String country() {
        return countryName;
    }
}
