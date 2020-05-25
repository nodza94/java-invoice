package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import pl.edu.agh.mwo.invoice.product.Product;
//import java.util.Random;


public class Invoice {
    private Map<Product, Integer> products = new HashMap<Product, Integer>();
    private static int nextNumber = 0;
    private final int number = ++nextNumber;

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        if (products.containsKey(product)) {
            products.put(product, products.get(product) + quantity);
        } else {
            products.put(product, quantity);
        }
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return number;
    }
    
    public String toString() {
        String invoicePrint = "INVOICE No. " + number + "\n";
        int i = 1;

        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            invoicePrint +=  + i + "  " + product.getName() + " \t " + quantity + " x  " + product.getPriceWithTax() + "\n";
            i++;
        }
        invoicePrint = invoicePrint + "LICZBA POZYCJI: " + products.size();
        return invoicePrint;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }
}

/*
INVOICE No. [XXXX]
1 Product Name  [quantity] x price
2 Product Name  [quantity] x price
.
.
.
N Product Name  X - price
LICZBA POZYCJI: X
*/