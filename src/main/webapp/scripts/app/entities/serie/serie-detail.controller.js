'use strict';

angular.module('jTrackSeriesApp')
    .controller('SerieDetailController', function ($scope, $rootScope, $stateParams, entity, Serie, Season) {
        $scope.serie = entity;
        $scope.load = function (id) {
            Serie.get({id: id}, function(result) {
                $scope.serie = result;
            });
        };
        var unsubscribe = $rootScope.$on('jTrackSeriesApp:serieUpdate', function(event, result) {
            $scope.serie = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
