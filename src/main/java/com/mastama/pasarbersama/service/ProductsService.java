package com.mastama.pasarbersama.service;

import com.mastama.pasarbersama.entity.PagingInfo;
import com.mastama.pasarbersama.entity.Products;
import com.mastama.pasarbersama.entity.ProductsSort;
import com.mastama.pasarbersama.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductsService {
    private final ProductsRepository productsRepository;

    public List<Products> getAllProducts() {
        PageRequest pageRequest = PageRequest.of(3, 10);
        Page<Products> productsPage = productsRepository.findAll(pageRequest);

        return productsPage.toList();
    }

    public List<Products> getAllProduct() {
        List<Products> products = productsRepository.findAll();
        return products;
    }

    public List<Products> getProductsByPageAndSize(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size); //start from page 1
        Page<Products> productsPage = productsRepository.findAll(pageRequest);

        return productsPage.toList();
    }

//    public List<Products> getProductsName(Integer page, Integer size, String q) {
//        PageRequest pageRequest = PageRequest.of(page-1, size);
//        Page<Products> products = productsRepository.findByProductNameContainingIgnoreCase(q, pageRequest);
//
//        return products.toList();
//    }

    public PagingInfo<Products> getProductsByQAndC(int page, int size, String q, Integer categoryId) {
        PageRequest pageRequest = PageRequest.of(page-1, size);
        Page<Products> products = productsRepository.filterByNameAndCategory(q, categoryId, pageRequest);

        return PagingInfo.convertFromPage(products);
    }

    public PagingInfo<Products> getProducts(int page, int size, String sort) {
        PageRequest pageRequest = PageRequest.of(page-1, size); //start from page 1

        if (sort != null) {
            pageRequest = pageRequest.withSort(mapStringToSort(sort));
        }

        Page<Products> products = productsRepository.findAll(pageRequest);

        return PagingInfo.convertFromPage(products);
    }

    public PagingInfo<Products> getProductsByNameOrDesc(int page, int size, String q, String sort) {
        PageRequest pageRequest = PageRequest.of(page-1, size);

        if (sort != null) {
            pageRequest = pageRequest.withSort(mapStringToSort(sort));
        }

        Page<Products> products = productsRepository.filterByNameOrDescription(q, pageRequest);

        return PagingInfo.convertFromPage(products);
    }

    public PagingInfo<Products> getProductsByQOrDAndC(int page, int size, String q, Integer categoryId, String sort) {
        PageRequest pageRequest = PageRequest.of(page-1, size);


        if (sort != null) {
            pageRequest = pageRequest.withSort(mapStringToSort(sort));
        }
        Page<Products> products = productsRepository.filterByNameOrDescriptionAndCategory(q, categoryId, pageRequest);

        return PagingInfo.convertFromPage(products);
    }

    public PagingInfo<Products> getProductsByC(int page, int size, Integer categoryId, String sort) {
        PageRequest pageRequest = PageRequest.of(page-1, size);

        if (sort != null) {
            pageRequest = pageRequest.withSort(mapStringToSort(sort));
        }

        Page<Products> products = productsRepository.filterByCategory(categoryId, pageRequest);

        return PagingInfo.convertFromPage(products);
    }

    private Sort mapStringToSort(String value) {
        ProductsSort productsSort = ProductsSort.fromValue(value);
        return switch (productsSort) {
            case PRICE_ASCENDING -> Sort.by("price").ascending();
            case PRICE_DESCENDING -> Sort.by("price").descending();
        };
    }
}
