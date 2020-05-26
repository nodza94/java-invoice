package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;
    
    private final BigDecimal excise;

    protected Product(String name, BigDecimal price, BigDecimal tax, BigDecimal excise) {
        if (name == null || name.isEmpty() || price == null || tax == null || excise == null
                || tax.compareTo(new BigDecimal(0)) < 0
                || price.compareTo(new BigDecimal(0)) < 0
                || excise.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
        this.excise = excise;
    }
    //excise is not for all products, so it's crucial to have a constructor for normal product!
    //Alcohol is Other product
    //Fuel -> Road and Transporter Day + law abolishing the tax on liquid fuels -> Tax Free + excise
    
    protected Product(String name, BigDecimal price, BigDecimal tax) {
        this(name, price, tax, BigDecimal.ZERO);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
    
    public BigDecimal getExcise() {
        return excise;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price).add(excise);
    }
    
    //to eliminate duplicate user defined objects as a key
    
    @Override
    public int hashCode() {
        return Objects.hash(name, price, taxPercent, excise);
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        return Objects.equals(name, other.name) && Objects.equals(price, other.price)
                && Objects.equals(taxPercent, other.taxPercent) && Objects.equals(excise, other.excise);
    }
}
