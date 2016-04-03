package com.jtrackseries.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Episode.
 */
@Entity
@Table(name = "episode",
       indexes = {@Index(name = "INDX_EPISODE_SERIE",  columnList="serie_id", unique = false),
                  @Index(name = "INDX_EPISODE_DATEPUBLISH", columnList="date_publish", unique = false)
       			 }
		)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Episode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 2048)
    @Column(name = "title", length = 2048, nullable = false)
    private String title;
    
    @NotNull
    @Column(name = "season", nullable = false)
    private String season;
    
    @NotNull
    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;
    
    @Column(name = "date_publish")
    private LocalDate datePublish;
    
    @NotNull
    @Column(name = "viewed", nullable = false)
    private Boolean viewed;
    
    @Column(name = "external_id")
    private String externalId;
    
    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;
    
    @Size(max = 2048)
    @Column(name = "notes", length = 2048)
    private String notes;
    
    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;
    
    @ManyToOne
    @JoinColumn(name = "serie_id")
    private Serie serie;

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

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }
    
    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public LocalDate getDatePublish() {
        return datePublish;
    }
    
    public void setDatePublish(LocalDate datePublish) {
        this.datePublish = datePublish;
    }

    public Boolean getViewed() {
        return viewed;
    }
    
    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public String getExternalId() {
        return externalId;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Episode episode = (Episode) o;
        if(episode.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, episode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Episode{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", season='" + season + "'" +
            ", episodeNumber='" + episodeNumber + "'" +
            ", datePublish='" + datePublish + "'" +
            ", viewed='" + viewed + "'" +
            ", externalId='" + externalId + "'" +
            ", description='" + description + "'" +
            ", notes='" + notes + "'" +
            ", lastUpdated='"+lastUpdated+"'" + 
            '}';
    }

	public ZonedDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(ZonedDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
