package com.victorebubemadu.klasha.globalxchange.behaviouraltesting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.victorebubemadu.klasha.globalxchange.domain.model.ExchangedPairs;
import com.victorebubemadu.klasha.globalxchange.exception.ErrorDetails;
import com.victorebubemadu.klasha.globalxchange.exception.errorcodes.InfraErrorCodes;
import com.victorebubemadu.klasha.globalxchange.sdk.GlobalXchange;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExchangingCurrenciesTest {

    @LocalServerPort
    private int port;

    private GlobalXchange globalXchange = new GlobalXchange(() -> port);

    @Test
    void test_ExchangingCurrencies_HappyPath() {
        ExchangedPairs usd_ngn_0 = globalXchange.exchange("United States", "Nigeria", "0").data();

        assertThat(usd_ngn_0.pairSymbol(), is("USD/NGN"));
        assertThat(usd_ngn_0.baseAmount(), is("0.00"));
        assertThat(usd_ngn_0.quotaAmount(), is("0.00"));

        ExchangedPairs usd_ngn_1 = globalXchange.exchange("United States", "Nigeria", "1").data();

        assertThat(usd_ngn_1.pairSymbol(), is("USD/NGN"));
        assertThat(usd_ngn_1.baseAmount(), is("1.00"));
        assertThat(usd_ngn_1.quotaAmount(), is("460.72"));

        ExchangedPairs usd_ngn_10 = globalXchange.exchange("United States", "Nigeria", "10").data();

        assertThat(usd_ngn_10.pairSymbol(), is("USD/NGN"));
        assertThat(usd_ngn_10.baseAmount(), is("10.00"));
        assertThat(usd_ngn_10.quotaAmount(), is("4,607.20"));

        ExchangedPairs usd_ngn_1000 = globalXchange.exchange("United States", "Nigeria", "1000").data();

        assertThat(usd_ngn_1000.pairSymbol(), is("USD/NGN"));
        assertThat(usd_ngn_1000.baseAmount(), is("1,000.00"));
        assertThat(usd_ngn_1000.quotaAmount(), is("460,720.00"));

        ExchangedPairs usd_ngn_10000 = globalXchange.exchange("United States", "Nigeria", "10000").data();

        assertThat(usd_ngn_10000.pairSymbol(), is("USD/NGN"));
        assertThat(usd_ngn_10000.baseAmount(), is("10,000.00"));
        assertThat(usd_ngn_10000.quotaAmount(), is("4,607,200.00"));

        ExchangedPairs gbp_ugx_1 = globalXchange.exchange("United Kingdom", "Uganda", "1").data();

        assertThat(gbp_ugx_1.pairSymbol(), is("GBP/UGX"));
        assertThat(gbp_ugx_1.baseAmount(), is("1.00"));
        assertThat(gbp_ugx_1.quotaAmount(), is("4,633.48"));

        ExchangedPairs gbp_ugx_1_copy = globalXchange.exchange("United Kingdom", "Uganda", "1").data();
        assertThat(gbp_ugx_1, equalTo(gbp_ugx_1_copy));
    }

    @Test
    void test_BadInputs_SadPath() {
        ErrorDetails baseCurrency = globalXchange.exchange("United State", "Nigeria", "1").error();

        assertThat(baseCurrency.code(), is("INFRA_ERROR"));
        assertThat(baseCurrency.message(), is(InfraErrorCodes.INVALID_COUNTRY_NAME));

        ErrorDetails quotaCurrency = globalXchange.exchange("United States", "Nigeri", "1").error();

        assertThat(quotaCurrency.code(), is("INFRA_ERROR"));
        assertThat(quotaCurrency.message(), is(InfraErrorCodes.INVALID_COUNTRY_NAME));

        ErrorDetails baseAmount = globalXchange.exchange("United States", "Nigeri", "-1").error();

        assertThat(baseAmount.code(), is("VALIDATION_ERROR"));
        assertThat(baseAmount.message(), is("INVALID_BASE_AMOUNT"));
    }

}
