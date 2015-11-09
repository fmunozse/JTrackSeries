'use strict';

angular.module('jTrackSeriesApp')
    .controller('SeasonDetailController', function ($scope, $rootScope, $stateParams, entity, Season, Serie, Episode) {
        $scope.season = entity;
        $scope.load = function (id) {
            Season.get({id: id}, function(result) {
                $scope.season = result;
            });
        };
        var unsubscribe = $rootScope.$on('jTrackSeriesApp:seasonUpdate', function(event, result) {
            $scope.season = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
