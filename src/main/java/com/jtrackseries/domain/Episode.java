package com.jtrackseries.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Episode.
 */
@Entity
@Table(name = "episode")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Episode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
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
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "notes")
    private String notes;
    
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
            '}';
    }
}
