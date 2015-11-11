'use strict';

angular.module('jTrackSeriesApp')
	.controller('SeasonDeleteController', function($scope, $modalInstance, entity, Season) {

        $scope.season = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Season.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });