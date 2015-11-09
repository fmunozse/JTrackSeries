'use strict';

angular.module('jTrackSeriesApp')
    .controller('EpisodeDetailController', function ($scope, $rootScope, $stateParams, entity, Episode, Season) {
        $scope.episode = entity;
        $scope.load = function (id) {
            Episode.get({id: id}, function(result) {
                $scope.episode = result;
            });
        };
        var unsubscribe = $rootScope.$on('jTrackSeriesApp:episodeUpdate', function(event, result) {
            $scope.episode = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
