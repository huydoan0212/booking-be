package com.example.nikebe.domain.user.user.repository;

import com.example.nikebe.domain.user.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    @EntityGraph(attributePaths = {"userRole"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT u FROM UserEntity u JOIN FETCH u.userRole WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);
//
//    @NonNull
//    @EntityGraph(attributePaths = {"userRole", "userStatus"}, type = EntityGraph.EntityGraphType.FETCH)
//    @Query("SELECT u FROM UserEntity u WHERE u.userStatus.code <> 'DEACTIVATE' ")
//    List<UserEntity> findAll();
//
//    @NonNull
//    @EntityGraph(attributePaths = {"userRole", "userStatus"}, type = EntityGraph.EntityGraphType.FETCH)
//    List<UserEntity> findAll(Specification<UserEntity> spec);
//
//    @EntityGraph(attributePaths = {"userRole", "userStatus"}, type = EntityGraph.EntityGraphType.FETCH)
//    @NonNull
//    Page<UserEntity> findAll(Specification<UserEntity> specification, @NonNull Pageable pageable);
//
//    @EntityGraph(attributePaths = {"userRole", "userStatus"}, type = EntityGraph.EntityGraphType.FETCH)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    Optional<UserEntity> findById(UUID id);
//
//    @NonNull
//    @EntityGraph(attributePaths = {"userRole", "userStatus"}, type = EntityGraph.EntityGraphType.FETCH)
//    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.userStatus.code = :code")
//    Optional<UserEntity> findByIdAndUserStatusCode(@Param("id") UUID id, @Param("code") String code);

//    @Query("SELECT ue FROM UserEntity ue where :query")
//    List<UserResponse> findAllBy();
//    @EntityGraph(attributePaths = {"userRole", "userStatus"}, type = EntityGraph.EntityGraphType.LOAD)
//    UserEntity save(UserEntity userEntity);

}
