package com.victorebubemadu.klasha.globalxchange.infrastructure.api;

import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.victorebubemadu.klasha.globalxchange.domain.model.AllCountries;
import com.victorebubemadu.klasha.globalxchange.domain.model.CityPopulation;
import com.victorebubemadu.klasha.globalxchange.domain.model.TopCities;
import com.victorebubemadu.klasha.globalxchange.domain.model.CountryStates;
import com.victorebubemadu.klasha.globalxchange.domain.model.Currency;
import com.victorebubemadu.klasha.globalxchange.domain.model.StateCities;
import com.victorebubemadu.klasha.globalxchange.exception.AppException;
import com.victorebubemadu.klasha.globalxchange.exception.errorcodes.InfraErrorCodes;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Countriesnow {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public TopCities mostPopulatedCitiesIn(String countryName, int limit) {
        var url = "https://countriesnow.space/api/v0.1/countries/population/cities/filter";
        var json = gson.toJson(new MostPopulatedCitiesRequest(countryName, limit));

        var body = RequestBody.create(json, JSON);
        var request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (var response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new AppException(InfraErrorCodes.INVALID_COUNTRY_NAME);
            }

            var resJson = response.body().string();
            var mostPopulatedCitiesResponse = gson.fromJson(resJson, MostPopulatedCitiesResponse.class);

            return mostPopulatedCitiesResponse.cityPopulationRanking();
        } catch (IOException e) {
            throw new AppException(InfraErrorCodes.FAILED_OPERATION);
        }
    }

    public CountryStates statesOf(String countryName) {
        var url = "https://countriesnow.space/api/v0.1/countries/states";
        var json = gson.toJson(new StatesRequest(countryName));

        var body = RequestBody.create(json, JSON);

        var request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (var response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new AppException(InfraErrorCodes.INVALID_COUNTRY_NAME);
            }

            var resJson = response.body().string();
            var statesResponse = gson.fromJson(resJson, StatesResponse.class);

            return statesResponse.countryStates();
        } catch (IOException e) {
            throw new AppException(InfraErrorCodes.FAILED_OPERATION);
        }
    }

    public StateCities citiesOf(String countryName, String stateName) {
        var url = "https://countriesnow.space/api/v0.1/countries/state/cities";
        var json = gson.toJson(new CitiesRequest(countryName, stateName));

        var body = RequestBody.create(json, JSON);

        var request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (var response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new AppException(InfraErrorCodes.INVALID_COUNTRY_OR_STATE_NAME);
            }

            var resJson = response.body().string();
            var citiesResponse = gson.fromJson(resJson, CitiesResponse.class);

            return citiesResponse.stateCities(stateName);
        } catch (IOException e) {
            throw new AppException(InfraErrorCodes.FAILED_OPERATION);
        }
    }

    public Currency currencyOf(String countryName) {
        var url = "https://countriesnow.space/api/v0.1/countries/currency";
        var json = gson.toJson(new CountryInfoRequest(countryName));

        var body = RequestBody.create(json, JSON);

        var request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (var response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new AppException(InfraErrorCodes.INVALID_COUNTRY_NAME);
            }

            var resJson = response.body().string();
            var countryInfoResponse = gson.fromJson(resJson, CountryCurrencyResponse.class);

            return countryInfoResponse.currency();
        } catch (IOException e) {
            throw new AppException(InfraErrorCodes.FAILED_OPERATION);
        }
    }

    public AllCountries allCountries() {
        var url = "https://countriesnow.space/api/v0.1/countries/states";

        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = client.newCall(request).execute()) {
            if (response.code() != 200) {
                throw new AppException(InfraErrorCodes.FAILED_OPERATION);
            }

            var resJson = response.body().string();
            var countriesResponse = gson.fromJson(resJson, AllCountriesResponse.class);

            return countriesResponse.allCountries();
        } catch (IOException e) {
            throw new AppException(InfraErrorCodes.FAILED_OPERATION);
        }
    }

    private static class MostPopulatedCitiesRequest {
        private String order = "dsc";
        private String country;
        private int limit;

        MostPopulatedCitiesRequest(String country, int limit) {
            this.country = country;
            this.limit = limit;
        }
    }

    private static class MostPopulatedCitiesResponse {
        private City[] data;

        private static class PopulationCount {
            private String value;
        }

        private static class City {
            private String city;
            private String country;
            private PopulationCount[] populationCounts;

        }

        TopCities cityPopulationRanking() {
            var cityPopulations = new CityPopulation[data.length];

            for (var i = 0; i < data.length; i++) {
                var countryName = data[i].country;
                var cityName = data[i].city;
                var sizeStr = data[i].populationCounts[0].value;
                var size = Integer.parseInt(sizeStr);

                cityPopulations[i] = new CityPopulation(countryName, cityName,
                        size);
            }

            return new TopCities(cityPopulations);
        }
    }

    private static class AllCountriesResponse {
        private CountryData[] data;

        private static class CountryData {
            private String name;
        }

        public AllCountries allCountries() {
            String[] countries = Arrays.stream(data).map(countryData -> countryData.name).toArray(String[]::new);
            return new AllCountries(countries);
        }

    }

    private static class StatesResponse {
        private CountryData data;

        private static class CountryData {
            private String name;
            private State[] states;

            private String[] states() {
                return Arrays.stream(states).map(State::toString).toArray(String[]::new);
            }
        }

        private static class State {
            private String name;

            public String toString() {
                return name;
            }
        }

        public CountryStates countryStates() {
            var countryName = data.name;
            var states = data.states();

            return new CountryStates(countryName, states);
        }

    }

    private static class CitiesResponse {
        private String[] data;

        public StateCities stateCities(String stateName) {
            return new StateCities(stateName, cities());
        }

        public String[] cities() {
            return data;
        }
    }

    private static class CountryCurrencyResponse {
        private CountryData data;

        public static class CountryData {
            private String name;
            private String currency;
        }

        public Currency currency() {
            return new Currency(data.currency, data.name);
        }
    }

    private static class StatesRequest {
        private String country;

        public StatesRequest(String country) {
            this.country = country;
        }
    }

    private static class CitiesRequest {
        private String country;
        private String state;

        public CitiesRequest(String country, String state) {
            this.country = country;
            this.state = state;
        }
    }

    private static class CountryInfoRequest {
        public String country;

        public CountryInfoRequest(String country) {
            this.country = country;
        }
    }

}
