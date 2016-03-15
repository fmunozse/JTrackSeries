'use strict';

angular.module('jtrackseriesApp')
    .factory('ScratchService', function ($http) {
        return {
            findSeriesByTitle: function (title, lan) {
                return $http.get('api/scratch/serie/' + title,  {params:{lan: lan}}).then(function (response) {
                    return response.data;
                });           
            },
            importSerieById: function (id, lan) {
                return $http.get('api/scratch/importSerieById/' + id,  {params:{lan: lan}}).then(function (response) {
                    return response.data;
                });             	
            }
        };
    });