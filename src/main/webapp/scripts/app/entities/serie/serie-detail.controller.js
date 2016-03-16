'use strict';

angular.module('jtrackseriesApp')
    .controller('SerieDetailController', function ($scope, $rootScope, $stateParams, entity, Serie, Episode, $log,$resource, ParseLinks) {
        $scope.serie = entity;
        $scope.episodes = [];
        $scope.serieId = $stateParams.id;
        
        $scope.predicate = 'id';
        $scope.reverse = false;
        $scope.page = 1;
        $scope.totalItems=0;

        $scope.loadAll = function () {
            var EpisodesBySerieId = $resource('/api/serie/:id/episodes');
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

    });
