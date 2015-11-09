package com.jtrackseries.repository;

import com.jtrackseries.domain.Episode;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Episode entity.
 */
public interface EpisodeRepository extends JpaRepository<Episode,Long> {

}
