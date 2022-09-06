package ku.cs.models;

public class PromotionQuantity extends Promotion<Integer> {
    int minimumQuantity;

    public PromotionQuantity(String shopPromotionName, String promotionCode, String discountType, double discount, String detail, int minimumQuantity) {
        super(shopPromotionName, promotionCode, discountType, discount, detail);
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getMinimum() {
        return minimumQuantity;
    }

    @Override
    public void setMinimum(Integer min) {
        minimumQuantity = min;
    }

    public String getMinimumToString() {
        return "" + minimumQuantity + (minimumQuantity == 1 ? " piece." : " pieces.");
    }

    public String getDiscountToString() {
        return "" + getDiscount() + " %";
    }

    @Override
    public String promotionAlert() {
        return "Must purchase at least " + getMinimum() + (getMinimum() == 1 ? " piece." : " pieces.");
    }

    @Override
    public String getPromotionType() {
        return "Promotion quantity";
    }

    @Override
    public String toCsv() {
        return "PromotionQuantity ," + getShopPromotionName()
                + ", " + getPromotionCode() + ", " + getDiscountType() + ", " + getDiscount() + ", " + "\"" + getDetail() + "\"" + ", " + getMinimum();
    }

    @Override
    public String toString() {
        return getPromotionCode() + ", discount : " + getDiscount() + " %";
    }


    @Override
    public boolean match(Integer quantity) {
        return quantity >= getMinimum();
    }
}
