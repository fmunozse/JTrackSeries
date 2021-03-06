'use strict';

angular.module('jtrackseriesApp')
    .controller('EpisodeController', function ($scope, $state, Episode, ParseLinks, EpisodeViewed) {

        $scope.episodes = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Episode.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.episodes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.episode = {
                title: null,
                season: null,
                episodeNumber: null,
                datePublish: null,
                viewed: false,
                externalId: null,
                description: null,
                notes: null,
                id: null
            };
        };
        
        $scope.setViewed = function (id, set) {        	
        	EpisodeViewed.update({id: id, set:set},
            	function (result, header) {
        			//Nothing
            });            
        };
    });
