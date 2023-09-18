package com.victorebubemadu.klasha.globalxchange.domain.model;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import static com.victorebubemadu.klasha.globalxchange.ResourceFilePath.load;
import static com.victorebubemadu.klasha.globalxchange.ResourceFilePath.filePath;
import com.victorebubemadu.klasha.globalxchange.ResourceFilePath;

public class ExchangeRate {
    private static final Loader loader = new Loader();
    private static Map<String, BigDecimal> exchangeRates;

    static {
        exchangeRates = loader.loadExchangeRates();
    }

    public BigDecimal rate(Currency base, Currency quota) {
        String key = key(base.toString(), quota.toString());
        BigDecimal rate = exchangeRates.get(key);

        if (rate == null) {
            throw new RuntimeException(); // Tell service that it does not exist
        }

        return rate;
    }

    private String key(String baseCurrency, String quotaCurrency) {
        return loader.key(baseCurrency, quotaCurrency);
    }

    private static class Loader {
        private Map<String, BigDecimal> loadExchangeRates() {
            var exchangeRate = new HashMap<String, BigDecimal>();

            try {
                String path = filePath(ResourceFilePath.Csv.EXCHANGE_RATE);
                Reader reader = new FileReader(path);

                CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                        .setHeader("sourceCurrency", "targetCurrency", "rate")
                        .setSkipHeaderRecord(true)
                        .build();

                Iterable<CSVRecord> records = csvFormat.parse(reader);

                for (CSVRecord record : records) {
                    String sourceCurrency = record.get("sourceCurrency");
                    String targetCurrency = record.get("targetCurrency");
                    String rateString = record.get("rate");

                    String key = key(sourceCurrency, targetCurrency);
                    BigDecimal rate = parseRate(rateString);

                    exchangeRate.put(key, rate);
                }

                return exchangeRate;
            } catch (IOException e) {
                // TODO: Notify developers about the issue (Critical)
                throw new RuntimeException("Failed to load exchange rates file", e);
            }
        }

        private BigDecimal parseRate(String rateString) {
            try {
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                Number number = format.parse(rateString.replaceAll(",", ""));
                return BigDecimal.valueOf(number.doubleValue());
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid rate format: " + rateString, e);
            }
        }

        private String key(String base, String quota) {
            return base + quota;
        }
    }

}
