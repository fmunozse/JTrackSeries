'use strict';

angular.module('jtrackseriesApp').controller('SerieDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Serie', 'Season',
        function($scope, $stateParams, $uibModalInstance, entity, Serie, Season) {

        $scope.serie = entity;
        $scope.seasons = Season.query();
        $scope.load = function(id) {
            Serie.get({id : id}, function(result) {
                $scope.serie = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jtrackseriesApp:serieUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.serie.id != null) {
                Serie.update($scope.serie, onSaveSuccess, onSaveError);
            } else {
                Serie.save($scope.serie, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
