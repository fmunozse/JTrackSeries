'use strict';

angular.module('jtrackseriesApp')
    .factory('Serie', function ($resource, DateUtils) {
        return $resource('api/series/:id', {}, {        	
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.firstAired = DateUtils.convertLocaleDateFromServer(data.firstAired);
                    data.lastUpdated = DateUtils.convertDateTimeFromServer(data.lastUpdated);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.firstAired = DateUtils.convertLocaleDateToServer(data.firstAired);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.firstAired = DateUtils.convertLocaleDateToServer(data.firstAired);
                    return angular.toJson(data);
                }
            },
        	'updateAllSeriesFromTvDb':  {
                method: 'PUT',
                url: 'api/updateAllSeriesFromTvDb/'
            },
            'getStatSerieBySeasonAndSerieId': {
            	method: 'GET',
            	url:'api/series/:id/statsviewed',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.serie.firstAired = DateUtils.convertLocaleDateFromServer(data.serie.firstAired);
                    data.serie.lastUpdated = DateUtils.convertDateTimeFromServer(data.serie.lastUpdated);
                    return data;
                }            	
            },
            'getStatSeries': {
            	method: 'GET',
            	url:'api/series/statsviewed',
            	isArray: true	
            },
            'hasMoreSeasonThan': {
            	method: 'GET',
            	url:'api/series/:id/hasMoreSeasonThan/:season'       	
            },
            'getStatsRecordsByYearMonth': {
            	method: 'GET',
            	url:'api/series/statsRecordsByYearMonth',
            	isArray: true	
            }
        });
    });
