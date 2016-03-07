'use strict';

angular.module('jtrackseriesApp')
    .controller('EpisodeDetailController', function ($scope, $rootScope, $stateParams, entity, Episode, Serie) {
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

    });
