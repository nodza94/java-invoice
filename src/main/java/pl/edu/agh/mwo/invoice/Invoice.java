package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Collection<Product> products;

	public void addProduct(Product product) {
		products.add(product);
	}

	public void addProduct(Product product, Integer quantity) {
		for (int i = 0; i < quantity; i++) {
			products.add(product);
		}
	}

	public BigDecimal getSubtotal() {
		BigDecimal sum = BigDecimal.ZERO;

		if (products != null && products.size() > 0) {

			for (Product product : products) {

				sum.add(product.getPrice());
			}
			
		}
		return sum;
	}

	public BigDecimal getTax() {
		BigDecimal sumTax = BigDecimal.ZERO;
		
		if (products != null && products.size() > 0) {
			
		}
		return sumTax;
	}

	public BigDecimal getTotal() {
		BigDecimal sumTotal = BigDecimal.ZERO;
		
		return sumTotal;
	}
}
