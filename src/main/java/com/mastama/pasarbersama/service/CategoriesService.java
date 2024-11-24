package com.mastama.pasarbersama.service;

import com.mastama.pasarbersama.entity.Categories;
import com.mastama.pasarbersama.entity.PagingInfo;
import com.mastama.pasarbersama.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoryRepository categoryRepository;

    public PagingInfo<Categories> getCategories(int pageNumber, int pageSize) {
        // TODO
        // ketika pageNumber tidak ditemukan malah mendapatkan error 401
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        Page<Categories> categoriesPage = categoryRepository.findAll(pageRequest);

        return PagingInfo.convertFromPage(categoriesPage);
    }
}
