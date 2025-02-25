package com.example.nikebe.domain.user.user.entity;

import com.example.nikebe.common.UserStatus;
import com.example.nikebe.domain.role.entity.RoleEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id, username, name, dob, idNumber, gender, phone, startDate, createdAt, updatedAt, userRole, userStatus")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "username", unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "id_number", unique = true)
    private String idNumber;

    @Column(name = "gender")
    private short gender;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID createdBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updatedAt;

    @LastModifiedBy
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_by")
    private UUID updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private RoleEntity userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getUserRoleToString() {
        return userRole.getRole();
    }

    public UserEntity(UUID id) {
        this.id = id;
    }


}
