package com.example.booking.common.pagination;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ParameterObject
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageOptionsDto {
    @Parameter(description = "Page number", in = ParameterIn.QUERY, example = "1")
    @Min(value = 1, message = "Page index must be greater than or equal to 1")
    private int page = 1;

    @Parameter(description = "Items per page", in = ParameterIn.QUERY, example = "1")
    @Min(value = 1, message = "Items per page must be greater than or equal to 1")
    private int take = 1 ;

    private Sort.Direction  sortDirection ;

    private String sortBy;

    public Pageable toPageable() {
        if(sortDirection != null && sortBy != null) {
            return PageRequest.of(page -1, take, sortDirection, sortBy);
        }
        return PageRequest.of(page -1, take);
    }



}
