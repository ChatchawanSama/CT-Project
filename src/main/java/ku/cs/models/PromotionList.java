package ku.cs.models;

import java.util.ArrayList;
import java.util.List;

public class PromotionList {
    private List<Promotion> promotions;

    public PromotionList() {
        promotions = new ArrayList<>();
    }

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public void removePromotion(Promotion promotion) {
        for (Promotion promo : promotions)
            if (promo.equals(promotion)) {
                promotions.remove(promo);
                return;
            }
    }

    public void changePromotion(Promotion oldPromotion, Promotion newPromotion) {
        for (Promotion promotion : promotions) {
            if (promotion.getPromotionCode().equals(oldPromotion.getPromotionCode())) {
                promotion.setPromotionCode(newPromotion.getPromotionCode());
                promotion.setDiscount(newPromotion.getDiscount());
                promotion.setMinimum(newPromotion.getMinimum());
                promotion.setDiscountType(newPromotion.getDiscountType());
                promotion.setDetail(newPromotion.getDetail());
            }
        }

    }

    public Promotion searchPromotionByPromotionCode(String promotionCode) {
        for (Promotion promotion : promotions) {
            if (promotion.getPromotionCode().equals(promotionCode))
                return promotion;
        }
        return null;
    }

    public List<Promotion> getPromotionListByShop(String shopName) {
        List<Promotion> promotionsByShop = new ArrayList<>();
        for (Promotion promotion : promotions) {
            if (promotion.getShopPromotionName().equals(shopName)) {
                promotionsByShop.add(promotion);
            }
        }
        return promotionsByShop;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    @Override
    public String toString() {
        return "PromotionList{" +
                "promotions=" + promotions +
                '}';
    }

    public String toCsv() {
        String line = "";

        for (Promotion promotion : promotions)
            line += promotion.toCsv() + "\n";

        return line;
    }

}
