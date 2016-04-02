package com.jtrackseries.web.rest.dto;

public class StatsSincronyzeDTO {
	
	public int seriesUpdated = 0;
	public int episodesUpdated = 0;
	public int episodesNewed = 0;
	
	public int getSeriesUpdated() {
		return seriesUpdated;
	}
	public void setSeriesUpdated(int seriesUpdated) {
		this.seriesUpdated = seriesUpdated;
	}
	public int getEpisodesUpdated() {
		return episodesUpdated;
	}
	public void setEpisodesUpdated(int episodesUpdated) {
		this.episodesUpdated = episodesUpdated;
	}
	public int getEpisodesNewed() {
		return episodesNewed;
	}
	public void setEpisodesNewed(int episodesNewed) {
		this.episodesNewed = episodesNewed;
	}
	
	
}
