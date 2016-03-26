package com.jtrackseries.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Serie.
 */
@Entity
@Table(name = "serie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Serie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 2048)
    @Column(name = "title", length = 2048, nullable = false)
    private String title;
    
    @Size(max = 2048)
    @Column(name = "description", length = 2048)
    private String description;
    
    @Column(name = "external_link")
    private String externalLink;
    
    @Column(name = "external_id")
    private String externalId;
    
    @Column(name = "imdb_id")
    private String imdbId;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "first_aired")
    private LocalDate firstAired;
    
    @Size(max = 2048)
    @Column(name = "notes", length = 2048)
    private String notes;
    
    @OneToMany(mappedBy = "serie", cascade = CascadeType.REMOVE, orphanRemoval = true )
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Episode> episodes = new HashSet<>();

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

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalLink() {
        return externalLink;
    }
    
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getExternalId() {
        return externalId;
    }
    
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getImdbId() {
        return imdbId;
    }
    
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getFirstAired() {
        return firstAired;
    }
    
    public void setFirstAired(LocalDate firstAired) {
        this.firstAired = firstAired;
    }

    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Set<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Serie serie = (Serie) o;
        if(serie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, serie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Serie{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", externalLink='" + externalLink + "'" +
            ", externalId='" + externalId + "'" +
            ", imdbId='" + imdbId + "'" +
            ", status='" + status + "'" +
            ", firstAired='" + firstAired + "'" +
            ", notes='" + notes + "'" +
            '}';
    }
}
