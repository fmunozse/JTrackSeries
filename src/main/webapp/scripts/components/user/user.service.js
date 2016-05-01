'use strict';

angular.module('jtrackseriesApp')
    .factory('User', function ($resource) {
        return $resource('api/users/:login', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                },
                'save': { method:'POST' },
                'update': { method:'PUT' },
                'delete':{ method:'DELETE'},
                'getStatsRecordsByYearMonth': {
                	method: 'GET',
                	url:'api/users/statsRecordsByYearMonth',
                	isArray: true	
                }
            });
        });
