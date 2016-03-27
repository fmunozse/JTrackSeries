'use strict';

angular.module('jtrackseriesApp').controller('SerieDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Serie', 'Episode','DateUtils',
        function($scope, $stateParams, $uibModalInstance, entity, Serie, Episode, DateUtils) {
    	
    	$scope.dateTimeformat = DateUtils.dateTimeformat();    	
        $scope.serie = entity;
        $scope.episodes = Episode.query();
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
        $scope.datePickerForFirstAired = {};
        $scope.datePickerForFirstAired.status = {
            opened: false
        };
        $scope.datePickerForFirstAiredOpen = function($event) {
            $scope.datePickerForFirstAired.status.opened = true;
        };
        
}]);
