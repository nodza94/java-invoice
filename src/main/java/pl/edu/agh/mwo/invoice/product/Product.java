package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
	private final String name;

	private final BigDecimal price;

	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax) throws IllegalArgumentException {
		
		this.name = name;
		this.price = price;
		this.taxPercent = tax;
		
		if (name == null || name == "" || name == " ") {
			throw new IllegalArgumentException("Product name can't be blank");
		}
		
	}

	public String getName() {
		
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getTaxPercent() {
		return taxPercent;
	}

	public BigDecimal getPriceWithTax() {
		//BigDecimal newPrice = price*taxPercent;
		return null;
	}
}
