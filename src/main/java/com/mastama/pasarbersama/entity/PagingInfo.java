package com.mastama.pasarbersama.entity;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagingInfo<T> {
    private int currentPage;
    private int nextPage;
    private int previousPage;
    private int totalPage;
    private long totalElements;
    private List<T> content;

    public static <T> PagingInfo<T> convertFromPage(Page<T> page) {
        PagingInfo<T> pagingInfo = new PagingInfo<>();

        pagingInfo.currentPage = page.getNumber() + 1;
        pagingInfo.totalPage = page.getTotalPages();
        pagingInfo.totalElements = page.getTotalElements();
        pagingInfo.content = page.getContent();

        if (page.hasNext()) {
            pagingInfo.nextPage = (page.getNumber() + 1) + 1;
        }

        if (page.hasPrevious()) {
            if (pagingInfo.currentPage > pagingInfo.totalPage) {
                pagingInfo.previousPage = pagingInfo.totalPage;
            } else {
                pagingInfo.previousPage = pagingInfo.currentPage - 1;
            }
        }

        return pagingInfo;
    }
}
