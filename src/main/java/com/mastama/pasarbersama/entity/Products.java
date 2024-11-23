package com.mastama.pasarbersama.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "product_name")
    private String productName;

    @Column(nullable = false, name = "product_image")
    private String productImage;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    private double rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories categoryId;
}
