package com.arrnel.tests.util;

import com.arrnel.tests.model.dto.*;
import com.arrnel.tests.model.enums.Currency;
import net.datafaker.Faker;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class DataGenerator {

    private static final Long CURRENT_SECONDS = 1000 * Long.parseLong(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"))
    );
    private static final AtomicLong userIdCounter = new AtomicLong(CURRENT_SECONDS);
    private static final Faker FAKE = new Faker();

    public static Long generateLong() {
        return Math.abs(ThreadLocalRandom.current().nextLong());
    }

    public static Long generateUserId() {
        return userIdCounter.incrementAndGet();
    }

    public static Currency randomCurrency(@Nullable Currency excludeCurrency) {
        var currencies = Currency.values();
        var delta = excludeCurrency == null ? 1 : 2;
        var index = new Random().nextInt(currencies.length - delta);
        return Arrays.stream(currencies)
                .filter(currency -> !currency.equals(Currency.UNKNOWN))
                .filter(currency -> !currency.equals(excludeCurrency))
                .toList()
                .get(index);
    }

    public static Currency randomCurrency() {
        return randomCurrency(null);
    }

    public static BigDecimal randomBigDecimal(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) >= 0) {
            throw new IllegalArgumentException("Max value should be greater than min");
        }
        var random = ThreadLocalRandom.current();
        BigDecimal range = max.subtract(min);
        BigDecimal randomInRange = range.multiply(BigDecimal.valueOf(random.nextDouble()));
        BigDecimal result = min.add(randomInRange);
        return result.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal randomBigDecimal() {
        return randomBigDecimal(BigDecimal.ZERO, new BigDecimal("10000.00"));
    }

    public static String randomText() {
        var isEmptyChance = 5;
        var randomValue = ThreadLocalRandom.current().nextInt(100);
        return randomValue < isEmptyChance
                ? ""
                : FAKE.lorem().sentence();
    }

    public static CreateBankAccountRequestDTO generateCreateBankAccountRequest() {
        return new CreateBankAccountRequestDTO(generateUserId());
    }

    public static CreateCurrencyWalletRequestDTO generateCurrencyWalletRequest() {
        return new CreateCurrencyWalletRequestDTO(generateUserId(), randomCurrency());
    }

    public static CreateCurrencyWalletRequestDTO generateCurrencyWalletRequest(Long bankAccountId) {
        return new CreateCurrencyWalletRequestDTO(bankAccountId, randomCurrency());
    }

    public static CreateDepositRequestDTO generateDepositRequest() {
        return new CreateDepositRequestDTO(new Random().nextLong(), randomBigDecimal());
    }

    public static CreateDepositRequestDTO generateDepositRequest(Long currencyWalletId) {
        return new CreateDepositRequestDTO(currencyWalletId, randomBigDecimal());
    }

    public static CreateTransferRequestDTO generateTransferRequest(Long sourceCurrencyWalletId, Long destinationCurrencyWalletId) {
        return new CreateTransferRequestDTO(
                sourceCurrencyWalletId,
                destinationCurrencyWalletId,
                randomBigDecimal(),
                randomText()
        );
    }

    public static CreateWithdrawalRequestDTO generateWithdrawalRequest(Long currencyWalletId) {
        return new CreateWithdrawalRequestDTO(currencyWalletId, randomBigDecimal());
    }

    public static CreateRefundRequestDTO generateRefundRequest(Long transferId) {
        return new CreateRefundRequestDTO(transferId, randomBigDecimal(), randomText());
    }

}
