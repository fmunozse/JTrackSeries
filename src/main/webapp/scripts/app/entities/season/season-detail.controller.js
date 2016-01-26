'use strict';

angular.module('jtrackseriesApp')
    .controller('SeasonDetailController', function ($scope, $rootScope, $stateParams, entity, Season, Serie, Episode) {
        $scope.season = entity;
        $scope.load = function (id) {
            Season.get({id: id}, function(result) {
                $scope.season = result;
            });
        };
        var unsubscribe = $rootScope.$on('jtrackseriesApp:seasonUpdate', function(event, result) {
            $scope.season = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
