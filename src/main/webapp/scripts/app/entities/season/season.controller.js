'use strict';

angular.module('jTrackSeriesApp')
    .controller('SeasonController', function ($scope, $state, $modal, Season, ParseLinks) {
      
        $scope.seasons = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Season.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.seasons = result;
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
            $scope.season = {
                title: null,
                orderNumber: null,
                notes: null,
                id: null
            };
        };
    });
