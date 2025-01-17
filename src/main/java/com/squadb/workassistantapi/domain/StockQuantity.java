package com.squadb.workassistantapi.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockQuantity implements Comparable<StockQuantity> {

    private static final int MIN_VALUE = 0;

    @Getter
    @Column(nullable = false, name = "stock_quantity")
    private int value;

    private StockQuantity(int value) {
        validateNonNegative(value);
        this.value = value;
    }

    private void validateNonNegative(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("재고 수는 0 이상이어야 합니다.");
        }
    }

    public static StockQuantity valueOf(int value) {
        return new StockQuantity(value);
    }

    public StockQuantity minusOne() {
        return StockQuantity.valueOf(this.value - 1);
    }

    public StockQuantity plusOne() {
        return StockQuantity.valueOf(this.value + 1);
    }

    boolean isZero() {
        return this.value == MIN_VALUE;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        StockQuantity that = (StockQuantity) other;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(StockQuantity other) {
        return Integer.compare(this.value, other.value);
    }
}

