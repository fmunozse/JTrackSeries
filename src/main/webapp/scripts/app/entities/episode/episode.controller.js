'use strict';

angular.module('jTrackSeriesApp')
    .controller('EpisodeController', function ($scope, $state, $modal, Episode, ParseLinks) {
      
        $scope.episodes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Episode.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
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
                datePublish: null,
                viewed: false,
                notes: null,
                id: null
            };
        };
    });
