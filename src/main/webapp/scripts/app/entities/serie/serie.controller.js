'use strict';

angular.module('jTrackSeriesApp')
    .controller('SerieController', function ($scope, $state, $modal, Serie, ParseLinks) {
      
        $scope.series = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Serie.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
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
                externalLink: null,
                description: null,
                notes: null,
                id: null
            };
        };
    });
