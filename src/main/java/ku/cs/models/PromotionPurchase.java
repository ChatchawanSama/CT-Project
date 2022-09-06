package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class PromotionPurchase extends Promotion<Double> {
    double minimumPurchase;


    public PromotionPurchase(String shopPromotionName, String promotionCode, String discountType, double discount, String detail, double minimumPurchase) {
        super(shopPromotionName, promotionCode, discountType, discount, detail);
        this.minimumPurchase = minimumPurchase;
    }

    public String getMinimumToString() {
        return "" + minimumPurchase + " Bath.";
    }

    public String getDiscountToString() {
        return "" + getDiscount() + (getDiscountType().equals("Value") ? " Baht." : " %");
    }

    public List<String> getPromotionPurchaseAllDiscountType() {
        List<String> discountTypeList = new ArrayList<>();
        discountTypeList.add("Value");
        discountTypeList.add("Percent");
        return discountTypeList;
    }

    @Override
    public Double getMinimum() {
        return minimumPurchase;
    }

    public void setMinimum(double minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    @Override
    public void setMinimum(Double min) {
        minimumPurchase = min;
    }


    @Override
    public String promotionAlert() {
        return "Must purchase at least " + getMinimum() + " Baht.";
    }

    @Override
    public String getPromotionType() {
        return "Promotion purchase";
    }


    @Override
    public String toCsv() {
        return "PromotionPurchase ," + getShopPromotionName()
                + ", " + getPromotionCode() + ", " + getDiscountType() + ", " + getDiscount() + ", " + "\"" + getDetail() + "\"" + ", " + getMinimum();
    }

    @Override
    public String toString() {
        return getPromotionCode() + ", discount : " + getDiscount() + (getDiscountType().equals("Value") ? " Baht." : " %");
    }

    @Override
    public boolean match(Double purchase) {
        return purchase >= getMinimum();
    }
}
