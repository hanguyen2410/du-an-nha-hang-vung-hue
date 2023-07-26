package com.cg.repository;

import com.cg.domain.dto.user.UserInfoDTO;
import com.cg.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT NEW com.cg.domain.dto.user.UserInfoDTO (" +
                "us.username, " +
                "sf.fullName, " +
                "us.role " +
            ") " +
            "FROM User AS us " +
            "JOIN Staff AS sf " +
            "ON sf.user = us " +
            "AND us.username = :username "
    )
    UserInfoDTO getInfoByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Boolean existsUserByUsernameAndIdNot(String username, String id);

    Boolean existsByUsername(String username);

    User getByUsername(String username);

    @Modifying
    @Query("UPDATE User AS us SET us.deleted = true WHERE us.id = :userId")
    void softDelete(@Param("userId") String userId);
}
