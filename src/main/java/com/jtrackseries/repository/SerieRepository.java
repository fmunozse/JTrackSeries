package com.jtrackseries.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtrackseries.domain.Serie;

/**
 * Spring Data JPA repository for the Serie entity.
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {

	List<Serie> findAllByTitle(String title);

    @Query("select serie from Serie serie where serie.externalId=?1 and serie.user.login = ?#{principal.username}")
    Optional<Serie> findOneByExternalIdAndUserIsCurrentUser(String externalId);

    @Query("select serie from Serie serie where serie.user.login = ?#{principal.username}")
	Page<Serie> findByUserIsCurrentUser(Pageable pageable);

    @Query("select serie from Serie serie where serie.user.login = ?#{principal.username}")
	List<Serie> findByUserIsCurrentUser();
    
}
