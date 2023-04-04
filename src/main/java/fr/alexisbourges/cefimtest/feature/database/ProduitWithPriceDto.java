package fr.alexisbourges.cefimtest.feature.database;

import jakarta.persistence.Tuple;

import java.math.BigDecimal;
import java.util.Objects;

public class ProduitWithPriceDto extends ProduitDto{
    private BigDecimal unitPrice;
    public ProduitWithPriceDto(Integer id, String name, String description, BigDecimal unitPrice) {
        super(id, name, description);
        this.unitPrice = unitPrice;
    }

    public ProduitWithPriceDto(Tuple tuple) {
        super(tuple);
        this.unitPrice = BigDecimal.valueOf((Double) tuple.get(3));
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProduitWithPriceDto that = (ProduitWithPriceDto) o;
        return unitPrice.equals(that.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unitPrice);
    }
}
