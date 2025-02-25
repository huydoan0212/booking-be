package com.example.nikebe.domain.role;

import com.example.nikebe.domain.role.dto.RoleResponseDto;
import com.example.nikebe.domain.role.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

//    @SecurityRequirement(name = Constant.AUTH_GUARD)
//    @PostMapping(path = "/")
//    private RoleEntity createRole(@RequestBody CreateRoleDto role) {
//        return roleService.createRole(role);
//    }

    @GetMapping("/{id}")
    public RoleResponseDto getRoleById(@PathVariable("id") UUID id) {
        return roleService.getRoleById(id);
    }

}
