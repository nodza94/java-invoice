package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelProduct extends TaxFreeProduct {
    public FuelProduct(String name, BigDecimal price) {
        super(name, price, new BigDecimal("5.56"));
    }
}
