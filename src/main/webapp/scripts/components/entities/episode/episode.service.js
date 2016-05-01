'use strict';

angular.module('jtrackseriesApp')
    .factory('Episode', function ($resource, DateUtils) {
        return $resource('api/episodes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.datePublish = DateUtils.convertLocaleDateFromServer(data.datePublish);
                    data.lastUpdated = DateUtils.convertDateTimeFromServer(data.lastUpdated);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.datePublish = DateUtils.convertLocaleDateToServer(data.datePublish);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.datePublish = DateUtils.convertLocaleDateToServer(data.datePublish);
                    return angular.toJson(data);
                }
            },
            'getStatsRecordsByYearMonth': {
            	method: 'GET',
            	url:'api/episodes/statsRecordsByYearMonth',
            	isArray: true	
            }
        });
    });


angular.module('jtrackseriesApp')
.factory('EpisodeViewed', function ($resource) {
    return $resource('api/episodes/:id/viewed', {id:'@id', set:'@set'}, {
        'update': {
            method: 'PUT'
        },    		
    })
});

