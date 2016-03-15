package com.jtrackseries.web.rest.dto;

public class ScratchSeriesDTO {

	private String id;
	private String title;
	private String overview;
	private String urlBanner;
	private String imdbId;

	public ScratchSeriesDTO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getUrlBanner() {
		return urlBanner;
	}

	public void setUrlBanner(String urlBanner) {
		this.urlBanner = urlBanner;
	}

	public String getImdbId() {
		return imdbId;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	@Override
	public String toString() {
		return "ScratchSeriesDTO [id=" + id + ", title=" + title + ", overview=" + overview + ", urlBanner=" + urlBanner
				+ ", imdbId=" + imdbId + "]";
	}

}
