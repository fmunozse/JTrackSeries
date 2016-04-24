'use strict';

angular.module('jtrackseriesApp')
    .controller('SerieDetailController', function ($scope, $rootScope, $stateParams, entity, Serie, Episode, $log,$resource, ParseLinks, EpisodeViewed) {
        $scope.serie = entity;
        $scope.episodes = [];
        $scope.serieId = $stateParams.id;
        
        $scope.predicate = 'id';
        $scope.reverse = false;
        $scope.page = 1;
        $scope.totalItems=0;
        
     	$scope.statSerieBySeasonAndSerieId = {
     			totalViewed:0,
     			totalEpisodes:1
     			}; 
        Serie.getStatSerieBySeasonAndSerieId(
        		{id : $scope.serieId}, function (result, header) {
        			$scope.statSerieBySeasonAndSerieId = result;
        	});
     	
        $scope.loadAll = function () {
            var EpisodesBySerieId = $resource('/api/series/:id/episodes');
            EpisodesBySerieId.query({id: $scope.serieId, page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
            	$scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.episodes = result;

            });            
        }
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadEpisodes($scope.serieId);
        };
        $scope.loadAll();
        
        
        $scope.load = function (id) {
            Serie.get({id: id}, function(result) {
                $scope.serie = result;
            });
        };
        var unsubscribe = $rootScope.$on('jtrackseriesApp:serieUpdate', function(event, result) {
            $scope.serie = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        $scope.setViewed = function (id, set) {        	
        	EpisodeViewed.update({id: id, set:set},
            	function (result, header) {
        			//Only update when is updated whe match the season of the episode selected with the stats
        			if ($scope.statSerieBySeasonAndSerieId.season == result.season) {
            			if (result.viewed) {
            				$scope.statSerieBySeasonAndSerieId.totalViewed ++
            			} else {
            				$scope.statSerieBySeasonAndSerieId.totalViewed --
            			}        				
        			}

            });            
        };

    });
