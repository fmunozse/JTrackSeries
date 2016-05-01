package com.jtrackseries.web.rest.dto;

import java.time.LocalDate;

public class StatsRecordsByCreateDate {
	
	public LocalDate createDate;
	public Long totalRecords;
	

	public StatsRecordsByCreateDate(Integer year, Integer month,  Long totalRecords) {
		super();		
		this.createDate = LocalDate.of(year, month +1 , 1).minusDays(1);		
		this.totalRecords = totalRecords;
	}
	

	public String toString() {
        return "StatsSeriesLastSeason {" +
                "createDate=" + createDate +
                ", totalRecords='" + totalRecords + "'" +
                '}';
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}
	
}
