package com.gkcontas.patterns.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The flyweight: immutable intrinsic state, shared rather than duplicated.
 *
 * <p>The split the pattern turns on is intrinsic versus extrinsic state. Code, symbol and
 * decimal places belong to the currency itself and are identical for every amount in BRL
 * — that is intrinsic, and shareable. The amount belongs to the transaction and differs
 * every time — extrinsic, and it stays outside.
 *
 * <p>Sharing only works because the object is immutable. A mutable flyweight is a global
 * variable handed to everyone who asked for a currency.
 */
public final class Currency {

    private static final Map<String, Currency> CACHE = new ConcurrentHashMap<>();

    private final String code;
    private final String symbol;
    private final int decimalPlaces;

    private Currency(String code, String symbol, int decimalPlaces) {
        this.code = code;
        this.symbol = symbol;
        this.decimalPlaces = decimalPlaces;
    }

    /**
     * The factory is not optional decoration: a public constructor would let callers make
     * their own instances and the sharing would quietly stop happening.
     */
    public static Currency of(String code) {
        return CACHE.computeIfAbsent(code, key -> switch (key) {
            case "BRL" -> new Currency("BRL", "R$", 2);
            case "USD" -> new Currency("USD", "$", 2);
            case "JPY" -> new Currency("JPY", "¥", 0);
            default -> new Currency(key, key, 2);
        });
    }

    static int cachedInstances() {
        return CACHE.size();
    }

    public String code() {
        return code;
    }

    public String symbol() {
        return symbol;
    }

    public int decimalPlaces() {
        return decimalPlaces;
    }
}
