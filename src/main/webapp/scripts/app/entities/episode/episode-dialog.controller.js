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

        var onSaveFinished = function (result) {
            $scope.$emit('jTrackSeriesApp:episodeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.episode.id != null) {
                Episode.update($scope.episode, onSaveFinished);
            } else {
                Episode.save($scope.episode, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
