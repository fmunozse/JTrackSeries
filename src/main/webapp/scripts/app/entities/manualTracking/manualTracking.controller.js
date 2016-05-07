'use strict';

angular.module('jtrackseriesApp')
    .controller('ManualTrackingController', function ($scope, $state, ManualTracking) {

        $scope.manualTrackings = [];
        $scope.loadAll = function() {
            ManualTracking.query(function(result) {
               $scope.manualTrackings = result;
            });
        };
        $scope.loadAll();

        
        var onSaveSuccess = function (result) {
            //$scope.$emit('jtrackseriesApp:manualTrackingUpdate', result);
        	console.log("result ", result);
        };

        var onSaveError = function (result) {
        };
        
        $scope.updateLastViewed = function (manualTracking) {
        	console.log(manualTracking);
            ManualTracking.update(manualTracking,onSaveSuccess,onSaveError);
        }
        
        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.manualTracking = {
                title: null,
                season: null,
                totalEpisodes: null,
                lastViewed: null,
                dateRemainder: null,
                id: null
            };
        };
    });
