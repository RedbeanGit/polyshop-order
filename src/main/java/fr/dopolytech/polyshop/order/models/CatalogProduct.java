package fr.dopolytech.polyshop.order.models;

public class CatalogProduct {
    public String id;
	public String name;
	public String description;
	public double price;

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}
}
