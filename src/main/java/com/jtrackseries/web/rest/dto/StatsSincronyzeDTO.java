package com.jtrackseries.web.rest.dto;

public class StatsSincronyzeDTO {
	
	public int seriesUpdated = 0;
	public int episodesUpdated = 0;
	public int episodesNewed = 0;
	public int episodesRemoved = 0;
	
	public String toString() {
        return "Stats {" +
                "series updated=" + seriesUpdated +
                ", episodes updated='" + episodesUpdated + "'" +
                ", episodes newed='" + episodesNewed + "'" +
                ", episodes removed='" + episodesRemoved + "'" +
                '}';
	}
	
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
	public int getEpisodesRemoved() {
		return episodesRemoved;
	}
	public void setEpisodesRemoved(int episodesRemoved) {
		this.episodesRemoved = episodesRemoved;
	}
	
	
}
