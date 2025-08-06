package Lib.Discount;

import Lib.CartItem;

public class DefaultPricingStrategy implements DiscountStrategy {
    @Override
    public double calculateItemPrice(CartItem item) {
        return item.getProduct().getPrice() * item.getQuantity();
    }
}
