package com.victorebubemadu.klasha.globalxchange.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public class CityPopulation {
    @JsonProperty()
    @Expose()
    private String country;

    @JsonProperty()
    @Expose()
    private String city;

    @JsonProperty()
    @Expose()
    private int size;

    public CityPopulation(
            String countryName,
            String cityName,
            int size) {
        this.country = countryName;
        this.city = cityName;
        this.size = size;
    }

    public int size() {
        return this.size;
    }

    public boolean isGreaterOrEqualTo(CityPopulation other) {
        return this.size() >= other.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CityPopulation other = (CityPopulation) obj;

        return this.country.equals(other.country) && this.city.equals(other.city) && this.size == other.size;
    }

}
