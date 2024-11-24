package com.mastama.pasarbersama.controller;

import com.mastama.pasarbersama.dto.BaseResponse;
import com.mastama.pasarbersama.entity.Categories;
import com.mastama.pasarbersama.entity.PagingInfo;
import com.mastama.pasarbersama.service.CategoriesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoriesService categoriesService;

    @GetMapping
    public BaseResponse getCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        PagingInfo<Categories> categoriesList = categoriesService.getCategories(page, size);

        BaseResponse response = new BaseResponse();
        response.setResponseStatus(true);
        response.setResponseDesc("Success get categories");
        response.setData(categoriesList);

        return response;
    }
}
