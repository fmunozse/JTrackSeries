'use strict';

angular.module('jtrackseriesApp')
    .controller('EpisodeDetailController', function ($scope, $rootScope, $stateParams, entity, Episode, Serie, EpisodeViewed, $log) {
        $scope.episode = entity;
        $scope.load = function (id) {
            Episode.get({id: id}, function(result) {
                $scope.episode = result;
            });
        };
        var unsubscribe = $rootScope.$on('jtrackseriesApp:episodeUpdate', function(event, result) {
            $scope.episode = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.setViewed = function (id, viewed) {        	
			$log.debug ("id", id);
			$log.debug ("viewed", viewed);

        	EpisodeViewed.update({id: id, set:viewed},
            	function (result, header) {
        			$log.debug ("result", result);
        			//$log.debug ("header", header);
            });            
        };
        
    });
