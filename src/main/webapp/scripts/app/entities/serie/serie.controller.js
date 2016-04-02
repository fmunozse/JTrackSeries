'use strict';

angular.module('jtrackseriesApp')
    .controller('SerieController', function ($scope, $state, Serie, ParseLinks, $log) {

        $scope.series = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Serie.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.series = result;
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
            $scope.serie = {
                title: null,
                description: null,
                externalLink: null,
                externalId: null,
                imdbId: null,
                status: null,
                firstAired: null,
                notes: null,
                id: null
            };
        };
        
        $scope.updateFromTvDb = function () {
        	Serie.updateAllSeriesFromTvDb();
        	
        }
    });
