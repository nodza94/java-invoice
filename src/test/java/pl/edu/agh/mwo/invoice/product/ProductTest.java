package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.Product;

public class ProductTest {
    @Test
    public void testProductNameIsCorrect() {
        Product product = new OtherProduct("buty", new BigDecimal("100.0"));
        Assert.assertEquals("buty", product.getName());
    }

    @Test
    public void testProductPriceAndTaxWithDefaultTax() {
        Product product = new OtherProduct("Ogorki", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.23"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testProductPriceAndTaxWithDairyProduct() {
        Product product = new DairyProduct("Szarlotka", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.08"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testPriceWithTax() {
        Product product = new DairyProduct("Oscypek", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("108"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullName() {
        new OtherProduct(null, new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithEmptyName() {
        new TaxFreeProduct("", new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullPrice() {
        new DairyProduct("Banany", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNegativePrice() {
        new TaxFreeProduct("Mandarynki", new BigDecimal("-1.00"));
    }
    
    @Test
    public void testProductSameNameDifferentPriceAreNotTheSameProduct() {
    	Product product1 = new OtherProduct("Fotel", new BigDecimal("150"));
    	Product product2 = new OtherProduct("Fotel", new BigDecimal("500"));
    	Assert.assertNotEquals(product1, product2);
    }
    
    @Test
    public void testProductSameNameDifferentTaxAreNotTheSameProduct() {
    	Product product1 = new OtherProduct("Fotel", new BigDecimal("150"));
    	Product product2 = new TaxFreeProduct("Fotel", new BigDecimal("500"));
    	Assert.assertNotEquals(product1, product2);
    }
    
    @Test
    public void testProductSameNameAndPrizeDifferentTaxAreNotTheSameProduct() {
    	Product product1 = new OtherProduct("Fotel", new BigDecimal("500"));
    	Product product2 = new TaxFreeProduct("Fotel", new BigDecimal("500"));
    	Assert.assertNotEquals(product1, product2);
    }
    
    @Test
    public void testProductSameNameDifferentExciseAreNotTheSameProduct() {
    	Product product1 = new OtherProduct("Gin", new BigDecimal("150"));
    	Product product2 = new AlcoholProduct("Gin", new BigDecimal("500"));
    	Assert.assertNotEquals(product1, product2);
    }
    
    @Test
    public void testProductSameNameAndPrizeDifferentExciseAreNotTheSameProduct() {
    	Product product1 = new OtherProduct("Gin", new BigDecimal("150"));
    	Product product2 = new AlcoholProduct("Gin", new BigDecimal("150"));
    	Assert.assertNotEquals(product1, product2);
    }
    
    @Test
    public void testProductPriceTaxAndFullCostAlcoholProduct() {
        Product product = new AlcoholProduct("Soplica Morelowa", new BigDecimal("100"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.23"), Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(new BigDecimal("128.56"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }
    
    @Test
    public void testProductPriceTaxAndFullCostFuelProduct() {
        Product product = new FuelProduct("Fuel Canister", new BigDecimal("50"));
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(new BigDecimal("55.56"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }
    @Test
    public void testProductExciseForTaxFreeProduct() {
        Product product = new TaxFreeProduct("Gazeta Krakowska", new BigDecimal("10"));
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getExcise()));
    }
    @Test
    public void testProductExciseForOtherProduct() {
    	Product product = new OtherProduct("Zielona Herbata", new BigDecimal("10"));
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getExcise()));
    }
}
