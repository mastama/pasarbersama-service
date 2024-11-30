package com.mastama.pasarbersama.entity;

import lombok.Getter;

@Getter
public enum ProductsSort {

    PRICE_ASCENDING("priceAscending"),
    PRICE_DESCENDING("priceDescending"),;


    private final String value;

    ProductsSort(String value) {
        this.value = value;
    }

    public static ProductsSort fromValue(String value) {
        for (ProductsSort productsSort : ProductsSort.values()) {
            if (productsSort.value.equalsIgnoreCase(value)) {
                return productsSort;
            }
        }
        throw new IllegalArgumentException("Invalid product sort: {}" + value + " , avaliable asc or desc");
    }
}
