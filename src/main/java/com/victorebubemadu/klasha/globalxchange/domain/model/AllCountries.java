package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AllCountries {

    @JsonProperty()
    private String[] countries;

    public AllCountries(String[] countries) {
        this.countries = new String[countries.length];

        System.arraycopy(countries, 0, this.countries, 0, this.countries.length);
        Arrays.sort(this.countries);

    }

    public String[] list() {
        return countries;
    }

    public boolean includes(String country) {
        return Arrays.binarySearch(countries, country, (a, b) -> a.compareToIgnoreCase(b)) >= 0;
    }
}
