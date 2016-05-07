'use strict';

angular.module('jtrackseriesApp')
    .controller('ManualTrackingDetailController', function ($scope, $rootScope, $stateParams, entity, ManualTracking, User) {
        $scope.manualTracking = entity;
        $scope.load = function (id) {
            ManualTracking.get({id: id}, function(result) {
                $scope.manualTracking = result;
            });
        };
        var unsubscribe = $rootScope.$on('jtrackseriesApp:manualTrackingUpdate', function(event, result) {
            $scope.manualTracking = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
