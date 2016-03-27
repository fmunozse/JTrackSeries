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
            }
        });
    });
