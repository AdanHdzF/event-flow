package com.edacourse.api;

public class TenPercentDiscountPromotion implements PromotionService {

	@Override
	public void applyPromotion(double price) {
		System.out
				.println("[TenPercentDiscountPromotion]: original price: " + price + ", final price: " + (price * 0.9));
	}

}
