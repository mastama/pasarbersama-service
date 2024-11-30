package com.mastama.pasarbersama.controller;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.entity.PagingInfo;
import com.mastama.pasarbersama.entity.Products;
import com.mastama.pasarbersama.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping("/products/all")
    BaseResponse getAllProducts() {
        BaseResponse baseResponse = new BaseResponse();
        List<Products> productList = productsService.getAllProducts();
        baseResponse.setResponseStatus(true);
        baseResponse.setResponseDesc("Success");
        baseResponse.setData(productList);

        return baseResponse;
    }

    @GetMapping("/product/all")
    BaseResponse getAllProduct() {
        BaseResponse baseResponse = new BaseResponse();
        List<Products> productList = productsService.getAllProduct();
        baseResponse.setResponseStatus(true);
        baseResponse.setResponseDesc("Success");
        baseResponse.setData(productList);

        return baseResponse;
    }

    @GetMapping
    BaseResponse getProductsByPageAndSize(
            @RequestParam(defaultValue = "1") int page, //start from page-1
            @RequestParam(defaultValue = "5") int size
            ) {
        BaseResponse baseResponse = new BaseResponse();
        List<Products> productList = productsService.getProductsByPageAndSize(page, size);
        baseResponse.setResponseStatus(true);
        baseResponse.setResponseDesc("Success");
        baseResponse.setData(productList);

        return baseResponse;
    }

    @GetMapping("/products")
    BaseResponse getProductsName( @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "5") Integer size,
                                  @RequestParam(required = false) String q,
                                  @RequestParam(required = false) Integer categoryId,
                                  @RequestParam(required = false) String sort
    ) {

        String key = (q == null ? "NQ" : "Q") + (categoryId == null ? "NC" : "C");

        PagingInfo<Products> productList = switch (key) {
            case "QNC" -> productsService.getProductsByNameOrDesc(page, size, q, sort);
            case "NQC" -> productsService.getProductsByC(page, size, categoryId, sort);
            case "QC" -> productsService.getProductsByQOrDAndC(page, size, q, categoryId, sort);
            default -> productsService.getProducts(page, size, sort);
        };

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseStatus(true);
        baseResponse.setResponseDesc("Success");
        baseResponse.setData(productList);

        return baseResponse;
    }
}
