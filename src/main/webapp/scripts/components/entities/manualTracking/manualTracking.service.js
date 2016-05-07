'use strict';

angular.module('jtrackseriesApp')
    .factory('ManualTracking', function ($resource, DateUtils) {
        return $resource('api/manualTrackings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateRemainder = DateUtils.convertLocaleDateFromServer(data.dateRemainder);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateRemainder = DateUtils.convertLocaleDateToServer(data.dateRemainder);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateRemainder = DateUtils.convertLocaleDateToServer(data.dateRemainder);
                    return angular.toJson(data);
                }
            }
        });
    });
