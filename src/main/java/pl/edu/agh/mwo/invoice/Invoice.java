package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	// private Collection<Product> products;
	private Map<Product, Integer> products = new HashMap<>();
	
	public void addProduct(Product product) {
		this.products.put(product, 1);
	}

	public void addProduct(Product product, Integer quantity) {
		// for (int i = 0; i < ; i++) {
		if (quantity > 0) {
			this.products.put(product, quantity);
		} else {
			throw new IllegalArgumentException("Quantity can't be negative or zero");
		}
	}

	public BigDecimal getSubtotal() {
		BigDecimal sum = BigDecimal.ZERO;

		if (products != null && products.size() > 0) {

			for (Product product : this.products.keySet()) {
				Integer quantity = this.products.get(product);
				sum = sum.add(product.getPrice().multiply(new BigDecimal(quantity)));
			}
		}
		return sum;

	}

	public BigDecimal getTax() {
		
		return this.getTotal().subtract(this.getSubtotal());
	}

	public BigDecimal getTotal() {
		BigDecimal sumTotal = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (Product product : products.keySet()) {
				Integer quantity = this.products.get(product);
				sumTotal = sumTotal.add(product.getPriceWithTax().multiply(new BigDecimal(quantity)));
			}
		}
		return sumTotal;
	}
}
