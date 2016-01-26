'use strict';

angular.module('jtrackseriesApp')
	.controller('EpisodeDeleteController', function($scope, $uibModalInstance, entity, Episode) {

        $scope.episode = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Episode.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
