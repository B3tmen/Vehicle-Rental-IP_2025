package org.unibl.etf.promotionsapp.model.beans;

import lombok.Data;
import org.unibl.etf.promotionsapp.model.dto.Promotion;
import org.unibl.etf.promotionsapp.service.PromotionService;

import java.io.Serializable;
import java.util.List;

// We'll use lombok instead of manually writing getters/setters...
@Data
public class PromotionBean implements Serializable {
    private List<Promotion> promotions;
    private PromotionService promotionService;

    public PromotionBean(String token) {
        this.promotionService = new PromotionService(token);
        this.promotions = promotionService.getAll();
    }

}
