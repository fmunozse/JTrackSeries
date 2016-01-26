'use strict';

angular.module('jtrackseriesApp')
	.controller('SeasonDeleteController', function($scope, $uibModalInstance, entity, Season) {

        $scope.season = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Season.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
