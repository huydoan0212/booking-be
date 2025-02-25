package com.example.nikebe.domain.role.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class RoleEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "role_reference")
    private String roleReference;

    @ColumnDefault("NULL::character varying")
    @Column(name = "description")
    private String description;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OffsetDateTime createdAt;

    @ColumnDefault("now()")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public RoleEntity(UUID id) {
        this.id = id;
    }
}