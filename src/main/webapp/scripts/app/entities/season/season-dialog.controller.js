'use strict';

angular.module('jTrackSeriesApp').controller('SeasonDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Season', 'Serie', 'Episode',
        function($scope, $stateParams, $modalInstance, entity, Season, Serie, Episode) {

        $scope.season = entity;
        $scope.series = Serie.query();
        $scope.episodes = Episode.query();
        $scope.load = function(id) {
            Season.get({id : id}, function(result) {
                $scope.season = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jTrackSeriesApp:seasonUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.season.id != null) {
                Season.update($scope.season, onSaveFinished);
            } else {
                Season.save($scope.season, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
