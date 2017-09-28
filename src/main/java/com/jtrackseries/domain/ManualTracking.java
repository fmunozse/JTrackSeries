package com.jtrackseries.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ManualTracking.
 */
@Entity
@Table(name = "manual_tracking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ManualTracking extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 1000)
    @Column(name = "title", length = 1000, nullable = false)
    private String title;
    
    @Column(name = "season")
    private String season;
    
    @Column(name = "total_episodes")
    private Integer totalEpisodes;
    
    @Column(name = "last_viewed")
    private Integer lastViewed;
    
    @Column(name = "date_remainder")
    private LocalDate dateRemainder;
    
	@Size(max = 1000)
    @Column(name = "external_link", length = 1000)
    private String externalLink;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSeason() {
        return season;
    }
    
    public void setSeason(String season) {
        this.season = season;
    }

    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }
    
    public void setTotalEpisodes(Integer totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public Integer getLastViewed() {
        return lastViewed;
    }
    
    public void setLastViewed(Integer lastViewed) {
        this.lastViewed = lastViewed;
    }

    public LocalDate getDateRemainder() {
        return dateRemainder;
    }
    
    public void setDateRemainder(LocalDate dateRemainder) {
        this.dateRemainder = dateRemainder;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ManualTracking manualTracking = (ManualTracking) o;
        if(manualTracking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, manualTracking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ManualTracking{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", season='" + season + "'" +
            ", totalEpisodes='" + totalEpisodes + "'" +
            ", lastViewed='" + lastViewed + "'" +
            ", dateRemainder='" + dateRemainder + "'" +
            ", externalLink='" + externalLink +"'" +
            '}';
    }
}
