package com.example.booking.common.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDto<T> {

    private List<T> rows;
    private int page;
    private int take;
    private long itemCount;
    private int pageCount;
    private Boolean hasPreviousPage;
    private Boolean hasNextPage;

    public PageDto(List<T> data, PageMetaDto meta) {
        this.rows = data;
        this.page = meta.getPage();
        this.take = meta.getTake();
        this.itemCount = meta.getItemCount();
        this.pageCount = meta.getPageCount();
        this.hasPreviousPage = meta.getHasPreviousPage();
        this.hasNextPage = meta.getHasNextPage();
    }
    public PageDto(Page<T> data) {
        this.rows = data.getContent();
        this.page = data.getPageable().getPageNumber() + 1 ;
        this.take = data.getPageable().getPageSize() ;
        this.itemCount = data.getTotalElements();
        this.pageCount = data.getTotalPages();
        this.hasPreviousPage = data.hasPrevious();
        this.hasNextPage = data.hasNext();
    }
}
