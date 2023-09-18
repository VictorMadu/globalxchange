package com.victorebubemadu.klasha.globalxchange.sdk;

import com.google.gson.reflect.TypeToken;
import com.victorebubemadu.klasha.globalxchange.controller.Response;
import com.victorebubemadu.klasha.globalxchange.domain.model.AllCountries;
import com.victorebubemadu.klasha.globalxchange.domain.model.TopCities;
import com.victorebubemadu.klasha.globalxchange.domain.model.CountryRegions;
import com.victorebubemadu.klasha.globalxchange.domain.model.ExchangedPairs;

import okhttp3.Request;

public class GlobalXchange {

    private GettablePort port;
    private ResponseManager responseManager = new ResponseManager();

    public GlobalXchange(GettablePort port) {
        this.port = port;
    }

    public Response<TopCities> mostPopluatedCitiesInItalyNewZealandAndGhana(int maxNoOfCities) {
        var url = withBase("/api/v1/countries/specified/most-populated-cities?limit=" + maxNoOfCities);

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        var parseGuide = new TypeToken<Response<TopCities>>() {
        };

        return responseManager.data(request, parseGuide);

    }

    public Response<TopCities> mostPopluatedCitiesInItalyNewZealandAndGhana_FreeParam(
            Object maxNoOfCities) {
        var url = withBase("/api/v1/countries/specified/most-populated-cities");

        if (maxNoOfCities != null)
            url += "?limit=" + maxNoOfCities;

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        var parseGuide = new TypeToken<Response<TopCities>>() {
        };

        return responseManager.data(request, parseGuide);

    }

    public Response<CountryRegions> countryStatesAndCities(String countryName) {
        var url = withBase("/api/v1/countries/regions?country=" + countryName);

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        var parseGuide = new TypeToken<Response<CountryRegions>>() {
        };
        return responseManager.data(request, parseGuide);
    }

    public Response<CountryRegions> countryStatesAndCities_FreeParam(Object countryName) {

        var url = withBase("/api/v1/countries/regions");

        if (countryName != null)
            url += "?country=" + countryName;

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        var parseGuide = new TypeToken<Response<CountryRegions>>() {
        };
        return responseManager.data(request, parseGuide);
    }

    public Response<ExchangedPairs> exchange(String baseCurrency, String quotaCurrency, String baseAmount) {
        var url = withBase("/api/v1/exchange?base_currency=" + baseCurrency
                + "&quota_currency=" + quotaCurrency
                + "&base_amount=" + baseAmount);

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        var parseGuide = new TypeToken<Response<ExchangedPairs>>() {
        };
        return responseManager.data(request, parseGuide);
    }

    public Response<AllCountries> allCountries() {
        var url = withBase("/api/v1/countries");

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        var parseGuide = new TypeToken<Response<AllCountries>>() {
        };
        return responseManager.data(request, parseGuide);

    }

    public static interface GettablePort {
        int get();
    }

    private String withBase(String otherUrl) {
        return "http://localhost:" + port.get() + otherUrl;
    }

}
