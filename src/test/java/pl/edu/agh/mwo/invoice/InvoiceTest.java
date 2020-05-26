package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.AlcoholProduct;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.FuelProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }
    
    //Invoice Number tests
    @Test
    public void testInvoiceHasNumber() {
		int number = invoice.getNumber();
		Assert.assertTrue(number > 0);
	}
    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
		int number = invoice.getNumber();
		int number2 = new Invoice().getNumber();
		Assert.assertNotEquals(number, number2);
    }
    @Test
    public void testTwoInvoicesHaveConsequentNumbers() {
		int number = invoice.getNumber();
		int number2 = new Invoice().getNumber();
		Assert.assertEquals(number, number2 -1);
    }
    @Test
    public void testInvoiceNumberIsNotChanging() {
    	Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }
    
    //Invoice print tests
    @Test
    public void testInvoiceIsString() {
    	String testInvoice = invoice.toString();
    	Assert.assertTrue(!testInvoice.isEmpty() || testInvoice != null);
    }
    
    @Test
    public void testInvoiceStartsWithNumber() {
    	String testInvoice = invoice.toString();
    	Assert.assertTrue(testInvoice.startsWith("INVOICE No. " + Integer.toString(invoice.getNumber())));
    }
    

    @Test
    public void testInvoiceEndsWithSum() {
    	invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")));
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 3);
        
    	String testInvoice = invoice.toString();
 
    	Assert.assertEquals(("LICZBA POZYCJI: " + invoice.getProducts().size()), 
    			testInvoice.substring(testInvoice.lastIndexOf("\n") + 1));
    	}
    

    @Test
    public void testInvoiceEveryProductListed() {
    	invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")));
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 3);
    	
    	String testInvoice = invoice.toString();
    	
    	String productList = testInvoice.substring(testInvoice.indexOf("\n") + 1, testInvoice.lastIndexOf("\n") + 1);
    	
    	Assert.assertEquals(productList.split("\r\n|\r|\n").length, invoice.getProducts().size());
    }
    
    @Test
    public void testInvoiceShowCorrectData() {
    	invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
    	invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")));
    	invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 3);
    	
    	String testInvoice = invoice.toString();
    	int i = 1;
    	String productList = testInvoice.substring(testInvoice.indexOf("\n") + 1, testInvoice.lastIndexOf("\n") + 1);
    	
    	Map<Product, Integer> products = invoice.getProducts();
    	String createdProductList = "";
    	
    	for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			createdProductList += i + "  " + product.getName() + " \t " + quantity + " x  " + product.getPriceWithTax() + "\n";
			i++;
		}
    	
    	Assert.assertEquals(createdProductList, productList);
    }
    
    //Invoice duplicate products
    @Test
    public void testInvoiceProductsCountAfterAddingDuplicate() {
    	invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
    	invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")));
    	int pinezkaVal1 = 30;
    	invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), pinezkaVal1);
    	int pinezkaVal2 = 20;
    	invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), pinezkaVal2);
    	
    	Assert.assertEquals(3, invoice.getProducts().size());
    }
    @Test
    public void testInvoiceProductCountIncreasesAfterAddingDuplicate() {
    	int pinezkaVal1 = 30;
    	int pinezkaVal2 = 20;
    	Integer amount = pinezkaVal1 + pinezkaVal2;
    	Product productTest = new OtherProduct("Pinezka", new BigDecimal("0.01"));
    	
    	invoice.addProduct(productTest, pinezkaVal1);
    	invoice.addProduct(productTest, pinezkaVal2);
    	
    	Assert.assertEquals(amount, invoice.getProducts().get(productTest));
    }
    
    @Test
    public void testInvoiceAddProductCountChanges() {
    	Product productTest = new DairyProduct("Chedar", new BigDecimal("10"));
    	Integer amount = 10;
    	for (int i = 0; i < amount; i++) {
    		invoice.addProduct(productTest);
    	}
    	Assert.assertEquals(amount, invoice.getProducts().get(productTest));
    }
    
    @Test
    public void testInvoiceAddProductTwiceWithDifferentPrice() {
        Product productTest1 =   new DairyProduct("Mleko", new BigDecimal("4"));
        Product productTest2 =   new DairyProduct("Mleko", new BigDecimal("5"));
        
        invoice.addProduct(productTest1);
        invoice.addProduct(productTest2);
     
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest2)); 
        
    }
    
    @Test
    public void testInvoiceAddDuplicatedProductTwiceWithDifferentPrice() {
        Product productTest1 =   new DairyProduct("Mleko", new BigDecimal("4"));
        Product productTest2 =   new DairyProduct("Mleko", new BigDecimal("5"));
        Integer val1 = 30;
        Integer val2 = 20;
        
        invoice.addProduct(productTest1, val1);
        invoice.addProduct(productTest2, val2);
     
        Assert.assertEquals(val1, invoice.getProducts().get(productTest1));      
        Assert.assertEquals(val2, invoice.getProducts().get(productTest2)); 
        
    }
    
    @Test
    public void testInvoiceAddProductTwiceWithDifferentTax() {
        Product productTest1 =   new OtherProduct("Mleko", new BigDecimal("4"));
        Product productTest2 =   new DairyProduct("Mleko", new BigDecimal("5"));
        
        invoice.addProduct(productTest1);
        invoice.addProduct(productTest2);
     
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest2)); 
        
    }
    
    @Test
    public void testInvoiceAddDuplicatedProductTwiceWithDifferentTax() {
        Product productTest1 =   new OtherProduct("Mleko", new BigDecimal("4"));
        Product productTest2 =   new DairyProduct("Mleko", new BigDecimal("5"));
        Integer val1 = 30;
        Integer val2 = 20;
        
        invoice.addProduct(productTest1, val1);
        invoice.addProduct(productTest2, val2);
     
        Assert.assertEquals(val1, invoice.getProducts().get(productTest1));      
        Assert.assertEquals(val2, invoice.getProducts().get(productTest2)); 
        
    }
    //Invoice test alcohol
    @Test
    public void testInvoiceAddProductWithSameNameWithExcise() {
        Product productTest1 =   new AlcoholProduct("Krupnik", new BigDecimal("25"));
        Product productTest2 =   new FuelProduct("Krupnik", new BigDecimal("100"));
        
        invoice.addProduct(productTest1);
        invoice.addProduct(productTest2);
     
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest2)); 
        
    }
    
    @Test
    public void testInvoiceAddDuplicatedProductWithSameNameWithExcise() {
    	Product productTest1 =   new AlcoholProduct("Krupnik", new BigDecimal("25"));
        Product productTest2 =   new FuelProduct("Krupnik", new BigDecimal("100"));
        Integer val1 = 3;
        Integer val2 = 2;
        
        invoice.addProduct(productTest1, val1);
        invoice.addProduct(productTest2, val2);
     
        Assert.assertEquals(val1, invoice.getProducts().get(productTest1));      
        Assert.assertEquals(val2, invoice.getProducts().get(productTest2));        
    }
    @Test
    public void testInvoiceAddProductWithandWithoutExcise() {
        Product productTest1 =   new AlcoholProduct("Chardonnay", new BigDecimal("25"));
        Product productTest2 =   new OtherProduct("Chardonnay", new BigDecimal("25"));
        
        invoice.addProduct(productTest1);
        invoice.addProduct(productTest2);
     
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(productTest2)); 
        
    }
    
    @Test
    public void testInvoiceAddDuplicatedProductWithandWithoutExcise() {
    	Product productTest1 =   new AlcoholProduct("Grzaniec Galicyjski", new BigDecimal("20"));
        Product productTest2 =   new OtherProduct("Grzaniec Galicyjski", new BigDecimal("20"));
        Integer val1 = 3;
        Integer val2 = 2;
        
        invoice.addProduct(productTest1, val1);
        invoice.addProduct(productTest2, val2);
     
        Assert.assertEquals(val1, invoice.getProducts().get(productTest1));      
        Assert.assertEquals(val2, invoice.getProducts().get(productTest2));        
    }
}
