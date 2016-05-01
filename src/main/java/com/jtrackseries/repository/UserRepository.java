package com.jtrackseries.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jtrackseries.domain.User;
import com.jtrackseries.web.rest.dto.StatsRecordsByCreateDate;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByLogin(String login);

    Optional<User> findOneById(Long userId);

    @Override
    void delete(User t);

    @Query(value = 
			"SELECT new com.jtrackseries.web.rest.dto.StatsRecordsByCreateDate "
			+ "(FUNCTION('YEAR', us.createdDate) , FUNCTION('MONTH', us.createdDate) , count(us) as total ) " 
		    + "FROM User us "
		    + "GROUP BY FUNCTION('YEAR', us.createdDate) , FUNCTION('MONTH', us.createdDate) "
		    + "ORDER BY FUNCTION('YEAR', us.createdDate) , FUNCTION('MONTH', us.createdDate) "		    
		  )		
	public List<StatsRecordsByCreateDate> findStatsRecordsCreateDate();
}
