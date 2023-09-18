package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

public class TopCities {

    @JsonProperty()
    @Expose()
    private CityPopulation[] cities;

    public TopCities(CityPopulation[] cities) {
        var clonedCities = cities.clone();

        validateSortedArray(clonedCities);
        this.cities = clonedCities;
    }

    public TopCities mergeWith(TopCities... otherCityPopulationRankings) {
        var mergedCities = cities;

        for (var otherCityPopulationRanking : otherCityPopulationRankings) {
            var otherCities = otherCityPopulationRanking.cities;
            mergedCities = mergeSortedArrays(mergedCities, otherCities);
        }

        return new TopCities(mergedCities);
    }

    public TopCities pickFirst(int n) {
        var firstNCities = Arrays.copyOfRange(cities, 0, Math.min(n, cities.length));
        return new TopCities(firstNCities);
    }

    public CityPopulation[] inDescendingOrder() {
        return cities.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TopCities other = (TopCities) obj;

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

    private void validateSortedArray(CityPopulation[] sortedCities) {
        for (var i = 1; i < sortedCities.length; i++) {
            if (sortedCities[i - 1].size() < sortedCities[i].size()) {
                throw new IllegalArgumentException("Input array must be sorted in descending order by size.");
            }
        }
    }

    private CityPopulation[] mergeSortedArrays(CityPopulation[] arr1, CityPopulation[] arr2) {
        var totalCities = arr1.length + arr2.length;
        var result = new CityPopulation[totalCities];
        int i = 0, j = 0, k = 0;

        while (i < arr1.length && j < arr2.length) {
            if (arr1[i].size() >= arr2[j].size()) {
                result[k++] = arr1[i++];
            } else {
                result[k++] = arr2[j++];
            }
        }

        while (i < arr1.length) {
            result[k++] = arr1[i++];
        }

        while (j < arr2.length) {
            result[k++] = arr2[j++];
        }

        return result;
    }

}
