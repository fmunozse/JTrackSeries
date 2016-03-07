'use strict';

angular.module('jtrackseriesApp')
    .controller('SerieDetailController', function ($scope, $rootScope, $stateParams, entity, Serie, Episode) {
        $scope.serie = entity;
        $scope.load = function (id) {
            Serie.get({id: id}, function(result) {
                $scope.serie = result;
            });
        };
        var unsubscribe = $rootScope.$on('jtrackseriesApp:serieUpdate', function(event, result) {
            $scope.serie = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
