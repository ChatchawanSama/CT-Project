package ku.cs.models;

public abstract class Promotion<T> implements ConditionFilterer<T> {
    private final String shopPromotionName;
    private String promotionCode;
    private String discountType;
    private double discount;
    private String detail;

    public Promotion(String shopPromotionName, String promotionCode, String discountType, double discount, String detail) {
        this.shopPromotionName = shopPromotionName;
        this.promotionCode = promotionCode;
        this.discountType = discountType;
        this.discount = discount;
        this.detail = detail;
    }

    public abstract Object getMinimum();

    public abstract void setMinimum(T t);

    public String getShopPromotionName() {
        return shopPromotionName;
    }

    public abstract String promotionAlert();

    public abstract String getPromotionType();

    public abstract String toCsv();

    public abstract String getMinimumToString();

    public abstract String getDiscountToString();

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }
}
