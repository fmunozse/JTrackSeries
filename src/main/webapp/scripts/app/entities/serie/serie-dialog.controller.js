'use strict';

angular.module('jTrackSeriesApp').controller('SerieDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Serie', 'Season',
        function($scope, $stateParams, $modalInstance, entity, Serie, Season) {

        $scope.serie = entity;
        $scope.seasons = Season.query();
        $scope.load = function(id) {
            Serie.get({id : id}, function(result) {
                $scope.serie = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jTrackSeriesApp:serieUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.serie.id != null) {
                Serie.update($scope.serie, onSaveFinished);
            } else {
                Serie.save($scope.serie, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
