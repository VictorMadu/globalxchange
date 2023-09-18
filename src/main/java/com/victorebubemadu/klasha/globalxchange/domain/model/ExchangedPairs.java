package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangedPairs {

    private static final ExchangeRate exchangeRate = new ExchangeRate();

    @JsonProperty("base")
    private Money base;

    @JsonProperty("quota")
    private Money quota;

    public ExchangedPairs(Currency baseCurrency, Currency quotaCurrency, BigDecimal baseAmount) {
        var rate = exchangeRate.rate(baseCurrency, quotaCurrency);

        this.base = new Money(baseCurrency, baseAmount);
        this.quota = new Money(quotaCurrency, baseAmount.multiply(rate));
    }

    @JsonProperty("pair")
    public String pairSymbol() {
        return base.currency() + "/" + quota.currency();
    }

    public String baseAmount() {
        return base.value();
    }

    public String quotaAmount() {
        return quota.value();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ExchangedPairs other = (ExchangedPairs) obj;

        return this.base.equals(other.base) && this.quota.equals(other.quota);

    }

}
