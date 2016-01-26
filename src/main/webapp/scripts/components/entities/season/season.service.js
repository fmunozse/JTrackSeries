'use strict';

angular.module('jtrackseriesApp')
    .factory('Season', function ($resource, DateUtils) {
        return $resource('api/seasons/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
