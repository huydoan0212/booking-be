package com.example.nikebe.common.template;

import com.example.nikebe.common.constant.Constant;
import com.example.nikebe.common.pagination.PageDto;
import com.example.nikebe.common.pagination.PageOptionsDto;
import com.turkraft.springfilter.converter.FilterSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface CRUDController<E, C, U, R> {

    @GetMapping("/{id}")
    R getById(@PathVariable("id") UUID id);

    @GetMapping("/")
    List<R> getAll();

    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") UUID id);

    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @PostMapping("/")
    R save(@RequestBody C dto);

    @SecurityRequirement(name = Constant.AUTH_GUARD)
    @PutMapping("/{id}")
    R update(@PathVariable("id") UUID id, @RequestBody U dto);

    @Operation(parameters = @Parameter(name = "filter", in = ParameterIn.QUERY,
            schema = @Schema(type = "string"), example = "name ~ '*'"))
    @GetMapping("/search")
    PageDto<R> search(@Parameter(hidden = true) FilterSpecification<E> spec, @ParameterObject PageOptionsDto dto);

}
