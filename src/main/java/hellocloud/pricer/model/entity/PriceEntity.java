package hellocloud.pricer.model.entity;

import hellocloud.pricer.model.Price;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "T_PRICE")
public class PriceEntity {
    @Id
    private String symbol;

    private BigDecimal value;

    private LocalDateTime updated;

    public PriceEntity() {
    }

    public PriceEntity(Price price) {
        this.symbol = price.getSymbol();
        this.value = price.getValue();
        this.updated = price.getUpdated();
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getValue() {
        return value;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public Price toPrice() {
        return new Price(symbol, value, updated);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceEntity that = (PriceEntity) o;
        return Objects.equals(symbol, that.symbol) &&
                Objects.equals(value, that.value) &&
                Objects.equals(updated, that.updated);
    }

    @Override
    public int hashCode() {

        return Objects.hash(symbol, value, updated);
    }
}
