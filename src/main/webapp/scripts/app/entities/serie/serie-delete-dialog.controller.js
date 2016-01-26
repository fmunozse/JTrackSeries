'use strict';

angular.module('jtrackseriesApp')
	.controller('SerieDeleteController', function($scope, $uibModalInstance, entity, Serie) {

        $scope.serie = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Serie.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
