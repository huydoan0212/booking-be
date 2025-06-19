package com.example.booking.common.pagination;

import lombok.Data;

@Data
public class PageMetaDto {
    private int page;

    private int take;

    private long itemCount;

    private int pageCount;

    private Boolean hasPreviousPage;

    private Boolean hasNextPage;

    public PageMetaDto(int page, int take, int itemCount, int pageCount, Boolean hasPreviousPage, Boolean hasNextPage) {
        this.page = page;
        this.take = take;
        this.itemCount = itemCount;
        this.pageCount = pageCount;
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }



    public PageMetaDto(PageOptionsDto pageOptionsDto, long itemCount) {
        this.page = pageOptionsDto.getPage();
        this.take = pageOptionsDto.getTake();
        this.itemCount = itemCount;
        this.pageCount = (int) Math.ceil((double) itemCount / take);
        this.hasPreviousPage = page > 1;
        this.hasNextPage = page < pageCount;
    }

    public PageMetaDto(int page, int take, long itemCount) {
        this.page = page;
        this.take = take;
        this.itemCount = itemCount;
        this.pageCount = (int) Math.ceil((double) itemCount / take);
        this.hasPreviousPage = page > 1;
        this.hasNextPage = page < pageCount;
    }
}
