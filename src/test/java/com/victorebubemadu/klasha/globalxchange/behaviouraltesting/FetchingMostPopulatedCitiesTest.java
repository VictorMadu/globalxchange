package com.victorebubemadu.klasha.globalxchange.behaviouraltesting;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.victorebubemadu.klasha.globalxchange.domain.model.CityPopulation;
import com.victorebubemadu.klasha.globalxchange.domain.model.TopCities;
import com.victorebubemadu.klasha.globalxchange.exception.ErrorDetails;
import com.victorebubemadu.klasha.globalxchange.sdk.GlobalXchange;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FetchingMostPopulatedCitiesTest {
    @LocalServerPort
    private int port;

    private GlobalXchange globalXchange = new GlobalXchange(() -> port);

    @Test
    void test_FetchingMostPopulatedCitiesWithLimitFive_HappyPath() {

        TopCities cities = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana(5).data();
        CityPopulation[] arranged = cities.inDescendingOrder();

        assertThat(arranged.length, is(5));

        for (int i = 1; i < arranged.length; i++) {
            var previous = arranged[i - 1];
            var current = arranged[i];

            assertThat(previous.isGreaterOrEqualTo(current), is(true));
        }
    }

    @Test
    void test_FetchingMostPopulatedCitiesWithLimitOne_HappyPath() throws Exception {
        TopCities cities = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana(1).data();
        CityPopulation[] arranged = cities.inDescendingOrder();

        assertThat(arranged.length, is(1));
    }

    @Test
    void test_FetchingMostPopulatedCitiesWithExceedAvailableCities_HappyPath() {

        TopCities cities_1 = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana(1_000_000).data();
        TopCities cities_2 = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana(2_000_000).data();

        assertThat(cities_1, equalTo(cities_2));
    }

    @Test
    void test_FetchingMostPopulatedCitiesWithLimitZero_SadPath() throws Exception {
        ErrorDetails maxNoOfCities = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana(0).error();

        assertThat(maxNoOfCities.code(), is("VALIDATION_ERROR"));
        assertThat(maxNoOfCities.message(), is("INVALID_LIMIT"));

    }

    @Test
    void test_FetchingMostPopulatedCitiesWithLimitMinusOne_SadPath() throws Exception {

        ErrorDetails maxNoOfCities = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana(-1).error();

        assertThat(maxNoOfCities.code(), is("VALIDATION_ERROR"));
        assertThat(maxNoOfCities.message(), is("INVALID_LIMIT"));

    }

    @Test
    void test_FetchingMostPopulatedCitiesWithCharLikeLimit_SadPath() throws Exception {

        ErrorDetails invalidLimitParam = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana_FreeParam("a")
                .error();

        assertThat(invalidLimitParam.code(), is("VALIDATION_ERROR"));
        assertThat(invalidLimitParam.message(), is("INCOMPATIBLE_TYPE_ERROR"));

        assertThat("Check if additional information was provided about the error for clarity",
                invalidLimitParam.extraDetails().length(), greaterThanOrEqualTo(1));

        ErrorDetails noLimitParam = globalXchange.mostPopluatedCitiesInItalyNewZealandAndGhana_FreeParam(null).error();

        assertThat(noLimitParam.code(), is("VALIDATION_ERROR"));
        assertThat(noLimitParam.message(), is("MISSING_REQUIRED_DATA"));

        assertThat("Check if additional information was provided about the error for clarity",
                noLimitParam.extraDetails().length(), greaterThanOrEqualTo(1));

    }

}