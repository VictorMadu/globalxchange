package com.victorebubemadu.klasha.globalxchange.behaviouraltesting;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.victorebubemadu.klasha.globalxchange.domain.model.AllCountries;
import com.victorebubemadu.klasha.globalxchange.domain.model.CountryRegions;
import com.victorebubemadu.klasha.globalxchange.exception.ErrorDetails;
import com.victorebubemadu.klasha.globalxchange.exception.errorcodes.InfraErrorCodes;
import com.victorebubemadu.klasha.globalxchange.sdk.GlobalXchange;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FetchingStatesAndCitiesTest {

    @LocalServerPort
    private int port;

    private GlobalXchange globalXchange = new GlobalXchange(() -> port);

    @Test
    void test_FetchingCountriesStatesAndTheirCities_HappyPath() {
        AllCountries countries = globalXchange.allCountries().data();

        assertThat(countries.includes("Nigeria"), is(true));
        assertThat(countries.includes("Nigeri"), is(false));
        assertThat("Case insenitive", countries.includes("niGEria"), is(true));

        CountryRegions nigeria = globalXchange.countryStatesAndCities("Nigeria").data();

        assertThat(nigeria.name(), is("Nigeria"));
        assertThat(nigeria.numberOfStates(), is(36)); // including FC
        assertThat(nigeria.hasState("Lagos State"), is(true));
        assertThat(nigeria.hasState("Enugu State"), is(true));
        assertThat(nigeria.hasState("London"), is(false));
        assertThat(nigeria.state("Lagos State").hasCity("Ikeja"), is(true));
        assertThat(nigeria.state("Enugu State").hasCity("Enugu"), is(true));
        assertThat(nigeria.state("Anambra State").hasCity("Anambra"), is(false));

        CountryRegions usa = globalXchange.countryStatesAndCities("United States").data();

        assertThat(usa.name(), is("United States"));
        assertThat(usa.hasState("California"), is(true));
        assertThat(usa.hasState("Californi"), is(false));
        assertThat(usa.state("California").hasCity("San Francisco"), is(true));
        assertThat(usa.state("California").hasCity("San Francisc"), is(false));

        CountryRegions nigeria2 = globalXchange.countryStatesAndCities("nIGeria").data();

        assertThat("Case Insensitive", nigeria, equalTo(nigeria2));
    }

    @Test
    void test_FetchingCountryWithWrongName_SadPath() {
        ErrorDetails invalidCountryName = globalXchange.countryStatesAndCities("Nigeri").error();

        assertThat(invalidCountryName.code(), is("INFRA_ERROR"));
        assertThat(invalidCountryName.message(), is("INVALID_COUNTRY_NAME"));

        ErrorDetails noCountryName = globalXchange.countryStatesAndCities_FreeParam(null).error();

        assertThat(noCountryName.code(), is("VALIDATION_ERROR"));
        assertThat(noCountryName.message(), is("MISSING_REQUIRED_DATA"));
        assertThat("Check if additional information was provided about the error for clarity",
                noCountryName.extraDetails().length(), greaterThanOrEqualTo(1));

    }

}
