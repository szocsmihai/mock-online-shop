package ro.iteahome.shop.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    public static LinkedHashMap<Product, Integer> content = new LinkedHashMap<>();

    public static float getTotalCost() {
        float cost = 0;
        for (Map.Entry<Product, Integer> pair : content.entrySet()) {
            cost += pair.getKey().getPrice() * pair.getValue();
        }
        return cost;
    }
}