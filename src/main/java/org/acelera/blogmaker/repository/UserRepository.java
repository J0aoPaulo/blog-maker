package org.acelera.blogmaker.repository;

import io.micrometer.common.lang.NonNull;
import org.acelera.blogmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmail(@NonNull String email);
}
