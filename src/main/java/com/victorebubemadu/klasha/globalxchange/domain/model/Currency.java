package com.victorebubemadu.klasha.globalxchange.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {

    @JsonProperty()
    private String name;

    @JsonProperty()
    private String country;

    public Currency(String name, String country) {
        this.name = name;
        this.country = country;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Currency other = (Currency) obj;

        return this.name.equals(other.name) && this.country.equals(other.country);

    }
}
