package Lib;

import java.util.ArrayList;

/**
 * RI (Representation Invariant): 
 * - shoppingCart ต้องไม่เป็น null
 * - ไม่มีสินค้าซ้ำใน shoppingCart 
 * 
 * AF (Abstraction Function): 
 * - Array CartItem ที่มีสินค้าและจำนวน
 * - มีฟังก์ชันก์ เพิ่ม, ลบ, ล้างสินค้า และคำนวณราคารวม
 */
public class ShoppingCart  {
    private ArrayList<CartItem> shoppingCart = new ArrayList<CartItem>();
    private PricingService pricingService;
    private ProductCatalog catalog;
    private double totalPrice;

    /**
     * ตรวจสอบ RI 
     * ถ้าไม่ถูกต้องจะ throw RuntimeException
     */
    private void checkRep(){
        if (shoppingCart == null) {
            throw new RuntimeException("RI violated: product list cannot be null");
        }
        // ตรวจสอบสินค้าซ้ำ
        for (int i = 0; i < shoppingCart.size(); i++){
            for (int j = i + 1; j < shoppingCart.size(); j++){
                if (shoppingCart.get(i).equals(shoppingCart.get(j))) {
                    throw new RuntimeException("RI violated: catalog cannot be duplicate products");
                }
            }
        }
    }
    
    /**
     * สร้าง ShoppingCart ใหม่
     * @param pricingService วิธีคำนวณราคา
     * @param catalog แคตตาล็อกสินค้า
     */
    public ShoppingCart(PricingService pricingService, ProductCatalog catalog){
        this.pricingService = pricingService;
        this.catalog = catalog;
    }

    /**
     * เพิ่มสินค้าเข้าในตะกร้า
     * @param sku รหัสสินค้า
     * @param quantity จำนวนที่ต้องการเพิ่ม
     * @return ไม่มีค่า return
     */
    public void addItem(String sku, int quantity){
        Product product = catalog.findById(sku);
        if (shoppingCart.size() == 0) {
            shoppingCart.add(new CartItem(product, quantity));
        } else {
            for(CartItem a : shoppingCart){
                if(a.getProduct().getProductId() == sku){
                    a.increaseQuantity(quantity);
                    checkRep();
                    return;
                } 
            }
            if ((catalog.findById(sku)== null)) {
                return;
            } else {
                if (quantity >0) {
                    shoppingCart.add(new CartItem(product, quantity)); 
                }
            }
        }
    }

    /**
     * แสดงรายการสินค้าในตะกร้า
     */
    public void printCart(){
        for(CartItem a : shoppingCart){
            System.out.println(a.getProduct().getProductId()+" "+ a.getQuantity()+pricingService.getStategy(a)+"\n");
        }
    }

    /**
     * คืนค่าจำนวนรายการสินค้าในตะกร้า
     * @return จำนวนรายการในตะกร้า
     */
    public int getItemCount(){
        return shoppingCart.size();
    }

    /**
     * คำนวณราคารวมของสินค้าทั้งหมดในตะกร้า
     * @return ราคารวมของตะกร้า
     */
    public double getTotalPrice(){
        totalPrice = 0;
        for(CartItem a : shoppingCart){
            totalPrice = totalPrice+pricingService.calculateItemPrice(a);
        }
        return this.totalPrice;
    }
    
    /**
     * คืนค่าราคารวมล่าสุดที่คำนวณ
     * @return ราคารวมล่าสุด
     */
    public double getTotalPriceNow() {
        return this.totalPrice;
    }

    /**
     * ลบสินค้าออกจากตะกร้าโดยใช้ sku
     * @param sku รหัสสินค้า
     */
    public void removeItem(String sku){
        CartItem itemToRemove = null;
        for(CartItem a : shoppingCart){
            if (a.getProduct().getProductId() == sku) {
                itemToRemove = a;
            }
        }
        shoppingCart.remove(itemToRemove);
    }
    
    /**
     * ลบสินค้าทั้งหมดในตะกร้า
     */
    public void clearCart(){
        shoppingCart.clear();
    }
}