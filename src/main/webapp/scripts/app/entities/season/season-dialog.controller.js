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

        var onSaveSuccess = function (result) {
            $scope.$emit('jTrackSeriesApp:seasonUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.season.id != null) {
                Season.update($scope.season, onSaveSuccess, onSaveError);
            } else {
                Season.save($scope.season, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
