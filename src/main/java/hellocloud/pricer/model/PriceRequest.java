package hellocloud.pricer.model;

import java.math.BigDecimal;

public class PriceRequest {
    private String symbol;
    private BigDecimal value;

    private PriceRequest() {
    }

    public PriceRequest(String symbol, BigDecimal value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceRequest that = (PriceRequest) o;

        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceRequest{" +
                "symbol='" + symbol + '\'' +
                ", value=" + value +
                '}';
    }
}
