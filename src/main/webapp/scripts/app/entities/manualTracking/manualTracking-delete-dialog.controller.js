'use strict';

angular.module('jtrackseriesApp')
	.controller('ManualTrackingDeleteController', function($scope, $uibModalInstance, entity, ManualTracking) {

        $scope.manualTracking = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ManualTracking.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
