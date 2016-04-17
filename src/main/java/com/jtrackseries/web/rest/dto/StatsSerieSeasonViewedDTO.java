package com.jtrackseries.web.rest.dto;

import com.jtrackseries.domain.Serie;

public class StatsSerieSeasonViewedDTO {
	
	public Serie serie;
	public String season;
	public Long totalViewed;
	public Long  totalEpisodes;
	
	public StatsSerieSeasonViewedDTO(Serie serie, String season, Long totalViewed, Long totalEpisodes) {
		super();
		this.serie = serie;
		this.season = season;
		this.totalViewed = totalViewed;
		this.totalEpisodes = totalEpisodes;
	}

	public String toString() {
        return "StatsSeriesLastSeason {" +
                "serie=" + serie +
                ", season='" + season + "'" +
                ", viewed='" + totalViewed + "'" +
                ", totalEpisodes='" + totalEpisodes + "'" +
                '}';
	}

	public Serie getSerie() {
		return serie;
	}

	public void setSerieId(Serie serie) {
		this.serie = serie;
	}

	public String getSeasson() {
		return season;
	}

	public void setSeasson(String season) {
		this.season = season;
	}

	public Long getViewed() {
		return totalViewed;
	}

	public void setViewed(Long viewed) {
		this.totalViewed = viewed;
	}

	public Long getTotal() {
		return totalEpisodes;
	}

	public void setTotal(Long total) {
		this.totalEpisodes = total;
	}
	
	
	
	
}
