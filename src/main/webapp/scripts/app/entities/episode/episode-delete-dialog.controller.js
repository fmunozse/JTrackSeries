'use strict';

angular.module('jTrackSeriesApp')
	.controller('EpisodeDeleteController', function($scope, $modalInstance, entity, Episode) {

        $scope.episode = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Episode.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });