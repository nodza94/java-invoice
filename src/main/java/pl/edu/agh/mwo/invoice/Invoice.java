package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	//private Collection<Product> products;
	private HashMap <Product, Integer> products;

	public void addProduct(Product product) {
		products.put(product, 1);
	}

	public void addProduct(Product product, Integer quantity) {
		//for (int i = 0; i < ; i++) {
		if (quantity > 0) {
			products.put(product, quantity);
		}
		else {
			throw new IllegalArgumentException("Quantity can't be negative or zero");
		}
	}

	public BigDecimal getSubtotal() {
		BigDecimal sum = BigDecimal.ZERO;

		if (products != null && products.size() > 0) {

			for (Product product : products.keySet()) {

				sum.add(product.getPrice());
			}
			
		}
		return sum;
		
	}

	public BigDecimal getTax() {
		BigDecimal sumTax = BigDecimal.ZERO;
		
		if (products != null && products.size() > 0) {
			for (Product product : products.keySet()) {

				sumTax.add(product.getPrice().multiply(product.getTaxPercent()));
			}
		}
		return sumTax;
	}

	public BigDecimal getTotal() {
		BigDecimal sumTotal = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (Product product : products.keySet()) {

				sumTotal.add(product.getPrice().multiply(product.getTaxPercent()));
			}
			}
		return sumTotal;
	}
}
