'use strict';

angular.module('jTrackSeriesApp')
    .controller('EpisodeController', function ($scope, Episode, ParseLinks) {
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

        $scope.delete = function (id) {
            Episode.get({id: id}, function(result) {
                $scope.episode = result;
                $('#deleteEpisodeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Episode.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEpisodeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.episode = {
                title: null,
                datePublish: null,
                viewed: false,
                id: null
            };
        };
    });
