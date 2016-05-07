package com.jtrackseries.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtrackseries.domain.ManualTracking;

/**
 * Spring Data JPA repository for the ManualTracking entity.
 */
public interface ManualTrackingRepository extends JpaRepository<ManualTracking,Long> {

    @Query("select manualTracking from ManualTracking manualTracking where manualTracking.user.login = ?#{principal.username} order by manualTracking.title asc")
    List<ManualTracking> findByUserIsCurrentUser();

}
