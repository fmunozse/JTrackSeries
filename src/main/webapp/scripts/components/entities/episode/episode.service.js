'use strict';

angular.module('jTrackSeriesApp')
    .factory('Episode', function ($resource, DateUtils) {
        return $resource('api/episodes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.datePublish = DateUtils.convertLocaleDateFromServer(data.datePublish);
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
            }
        });
    });
