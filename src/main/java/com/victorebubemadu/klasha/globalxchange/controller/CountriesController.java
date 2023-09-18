package com.victorebubemadu.klasha.globalxchange.controller;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.victorebubemadu.klasha.globalxchange.domain.model.AllCountries;
import com.victorebubemadu.klasha.globalxchange.domain.model.TopCities;
import com.victorebubemadu.klasha.globalxchange.exception.AppException;
import com.victorebubemadu.klasha.globalxchange.domain.model.CountryRegions;
import com.victorebubemadu.klasha.globalxchange.domain.model.ExchangedPairs;
import com.victorebubemadu.klasha.globalxchange.service.CountriesService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping("/api/v1")
public class CountriesController {

    @Autowired
    private CountriesService service;

    @GetMapping(value = "/countries/specified/most-populated-cities")
    public ResponseEntity<Response<TopCities>> mostPopluatedCitiesInItalyNewZealandAndGhana(
            @RequestParam("limit") @NotNull(message = "INVALID_LIMIT") @Digits(integer = 10, fraction = 0, message = "INVALID_LIMIT") @Range(min = 1, max = 4_000_000, message = "INVALID_LIMIT") int limit) {

        var topCities = service.mostPopluatedCitiesInItalyNewZealandAndGhana(limit);

        var message = "Most populated cities in Italy, New Zealand and Ghana";
        var response = new Response<>(message, topCities);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/countries/regions")
    public ResponseEntity<Response<CountryRegions>> countryStatesAndCities(
            @RequestParam("country") @NotNull(message = "INVALID_COUNTRY") @NotBlank(message = "INVALID_COUNTRY") String country) {

        var countryStatesAndCities = service.countryStatesAndCities(country);

        var message = country + " with list of cities in each of its states";
        var response = new Response<>(message, countryStatesAndCities);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/exchange")
    public ResponseEntity<Response<ExchangedPairs>> exchange(
            @RequestParam("base_currency") @NotNull(message = "INVALID_LIMIT") @NotBlank(message = "INVALID_BASE_CURRENCY") String baseCurrency,
            @RequestParam("quota_currency") @NotNull(message = "INVALID_LIMIT") @NotBlank(message = "INVALID_QUOTA_CURRENCY") String quotaCurrency,
            @RequestParam("base_amount") @NotNull(message = "INVALID_LIMIT") @Digits(fraction = 2, integer = 10, message = "INVALID_BASE_AMOUNT") BigDecimal baseAmount) {

        var exchangedPairs = service.exchange(baseCurrency, quotaCurrency, baseAmount);

        var message = exchangedPairs.pairSymbol() + " details";
        var response = new Response<>(message, exchangedPairs);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ADDITIONAL
    @GetMapping("/countries")
    public ResponseEntity<Response<AllCountries>> allCountries() {

        var allCountries = service.allCountries();

        var message = "All Countries";
        var response = new Response<>(message, allCountries);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
