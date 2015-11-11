'use strict';

angular.module('jTrackSeriesApp').controller('EpisodeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Episode', 'Season',
        function($scope, $stateParams, $modalInstance, entity, Episode, Season) {

        $scope.episode = entity;
        $scope.seasons = Season.query();
        $scope.load = function(id) {
            Episode.get({id : id}, function(result) {
                $scope.episode = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jTrackSeriesApp:episodeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.episode.id != null) {
                Episode.update($scope.episode, onSaveSuccess, onSaveError);
            } else {
                Episode.save($scope.episode, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
