package com.victorebubemadu.klasha.globalxchange.service;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.victorebubemadu.klasha.globalxchange.domain.model.AllCountries;
import com.victorebubemadu.klasha.globalxchange.domain.model.TopCities;
import com.victorebubemadu.klasha.globalxchange.domain.model.CountryRegions;
import com.victorebubemadu.klasha.globalxchange.domain.model.CountryStates;
import com.victorebubemadu.klasha.globalxchange.domain.model.Currency;
import com.victorebubemadu.klasha.globalxchange.domain.model.ExchangedPairs;
import com.victorebubemadu.klasha.globalxchange.domain.model.StateCities;
import com.victorebubemadu.klasha.globalxchange.infrastructure.api.Countriesnow;

@Service
public class CountriesService {
    final private Countriesnow countriesnow = new Countriesnow();

    public TopCities mostPopluatedCitiesInItalyNewZealandAndGhana(int limit) {
        int limitPerCountry = (int) Math.ceil(limit / 3f);

        TopCities inItaly = countriesnow.mostPopulatedCitiesIn("Italy", limitPerCountry);
        TopCities inNewZealand = countriesnow.mostPopulatedCitiesIn("New Zealand", limitPerCountry);
        TopCities inGhana = countriesnow.mostPopulatedCitiesIn("Ghana", limitPerCountry);

        return inItaly.mergeWith(inNewZealand, inGhana).pickFirst(limit);
    }

    public CountryRegions countryStatesAndCities(String country) {
        CountryStates countryStates = countriesnow.statesOf(country);
        StateCities[] stateCitiesMapping = Arrays.stream(countryStates.states())
                .parallel()
                .map(state -> countriesnow.citiesOf(countryStates.country(), state))
                .toArray(StateCities[]::new);

        return new CountryRegions(countryStates, stateCitiesMapping);
    }

    public ExchangedPairs exchange(String baseCountry, String quotaCountry, BigDecimal baseAmount) {
        Currency base = countriesnow.currencyOf(baseCountry);
        Currency quota = countriesnow.currencyOf(quotaCountry);

        ExchangedPairs exchanged = new ExchangedPairs(base, quota, baseAmount);

        return exchanged;
    }

    // ADDITIONAL
    public AllCountries allCountries() {
        return countriesnow.allCountries();
    }
}
