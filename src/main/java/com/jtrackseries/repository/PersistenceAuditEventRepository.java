package com.jtrackseries.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtrackseries.domain.PersistentAuditEvent;
import com.jtrackseries.web.rest.dto.StatsRecordsByCreateDate;

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
public interface PersistenceAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

    List<PersistentAuditEvent> findByPrincipal(String principal);

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime after);

    List<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
	@Query(value = 
			"SELECT new com.jtrackseries.web.rest.dto.StatsRecordsByCreateDate "
			+ "(FUNCTION('YEAR', o.auditEventDate) , FUNCTION('MONTH', o.auditEventDate) , count(o) as total ) " 
		    + "FROM PersistentAuditEvent o "
		    + "GROUP BY FUNCTION('YEAR', o.auditEventDate) , FUNCTION('MONTH', o.auditEventDate) "
		    + "ORDER BY FUNCTION('YEAR', o.auditEventDate) , FUNCTION('MONTH', o.auditEventDate) "		    
		  )		
	public List<StatsRecordsByCreateDate> findStatsRecordsCreateDate();    
}
