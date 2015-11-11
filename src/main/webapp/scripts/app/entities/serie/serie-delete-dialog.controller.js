'use strict';

angular.module('jTrackSeriesApp')
	.controller('SerieDeleteController', function($scope, $modalInstance, entity, Serie) {

        $scope.serie = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Serie.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });