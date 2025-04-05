package org.acelera.blogmaker.repository;

import org.acelera.blogmaker.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    boolean existsByDescription(String description);
}
