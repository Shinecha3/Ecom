package Lib;

import java.util.ArrayList;

import Lib.Discount.DefaultPricingStrategy;
import Lib.Discount.DiscountStrategy;

public class PricingService {
    private record StrategyRule(String sku, DiscountStrategy strategy) {}

    private final ArrayList<StrategyRule> strategies = new ArrayList<>();

    private final DiscountStrategy defaulStrategy = new DefaultPricingStrategy();

    /**
     * ลงทะเบียนกลยุทธ์ส่วนสำหรับสินค้า SKU ที่กำหนด
     * หากมีโปรโมชันสำหรับ SKU นี้อยู่แล้ว จะถูกแทนที่ด้วยอันใหม่
     * @param sku รหัสสินค้าที่ต้องการผูกกับโปรโมชัน
     * @param strategy กลยุทธ์ส่วนลดที่จะใช้
     */
    public void addStrategy(String sku, DiscountStrategy strategy){
        StrategyRule ruleToRemove = null;
        for (StrategyRule rule : strategies){
            if (rule.sku().equals(sku)) {
                ruleToRemove = rule;
                break;
            }
        }
        if (ruleToRemove != null){
            strategies.remove(ruleToRemove);
        }
        strategies.add(new StrategyRule(sku, strategy)); 
    }

    public double calculateItemPrice(CartItem item){
        String sku = item.getProduct().getProductId();
        for (StrategyRule rule : strategies){
                if (rule.sku().equals(sku)) {
                    return rule.strategy().calculateItemPrice(item);
                }
        }
        return defaulStrategy.calculateItemPrice(item);            
        
    }

    public DiscountStrategy getStategy(CartItem item){
        String sku = item.getProduct().getProductId();
        for (StrategyRule rule : strategies){
                if (rule.sku().equals(sku)) {
                    return rule.strategy;
                }
        }
        return null;
    }
}
