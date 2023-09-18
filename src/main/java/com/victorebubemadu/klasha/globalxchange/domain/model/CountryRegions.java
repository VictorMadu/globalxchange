package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryRegions {

    @JsonProperty()
    private String country;

    @JsonProperty()
    private StateRegions[] states;

    public CountryRegions(CountryStates countryState, StateCities[] stateCitiesMapping) {
        var states = new StateRegions[stateCitiesMapping.length];

        for (var i = 0; i < stateCitiesMapping.length; i++) {
            var stateCities = stateCitiesMapping[i];
            states[i] = new StateRegions(stateCities);

            if (!countryState.hasState(stateCities.state())) {
                throw new RuntimeException();
            }
        }

        this.country = countryState.country();

        Arrays.sort(states, (a, b) -> a.name().compareTo(b.name()));
        this.states = states;
    }

    public String name() {
        return country;
    }

    public int numberOfStates() {
        return states.length;
    }

    public boolean hasState(String state) {
        // TODO: Use binary search for performance

        for (StateRegions stateRegions : states) {
            if (stateRegions.is(state))
                return true;
        }

        return false;
    }

    public StateRegions state(String state) {
        for (StateRegions stateRegions : states) {
            if (stateRegions.is(state)) {
                return stateRegions;
            }
        }

        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CountryRegions other = (CountryRegions) obj;

        if (!this.country.equals(other.country)) {
            return false;
        }

        if (this.states.length != other.states.length) {
            return false;
        }

        for (int i = 0; i < this.states.length; i++) {
            if (!this.states[i].equals(other.states[i])) {
                return false;
            }
        }

        return true;
    }

}
