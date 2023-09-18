package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Money {
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.00",
            DecimalFormatSymbols.getInstance(Locale.US));

    @JsonProperty()
    private Currency currency;

    @JsonProperty()
    private BigDecimal value;

    public Money(Currency currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;

    }

    public Currency currency() {
        return currency;
    }

    public String value() {
        return decimalFormat.format(value);
    }

    public boolean hasValue(String v) {
        return value.toString() == v;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Money other = (Money) obj;

        return this.currency.equals(other.currency) && this.value.equals(other.value);
    }

}
