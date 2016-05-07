'use strict';

angular.module('jtrackseriesApp').controller('ManualTrackingDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ManualTracking', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, ManualTracking, User) {

        $scope.manualTracking = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            ManualTracking.get({id : id}, function(result) {
                $scope.manualTracking = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jtrackseriesApp:manualTrackingUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.manualTracking.id != null) {
                ManualTracking.update($scope.manualTracking, onSaveSuccess, onSaveError);
            } else {
                ManualTracking.save($scope.manualTracking, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDateRemainder = {};

        $scope.datePickerForDateRemainder.status = {
            opened: false
        };

        $scope.datePickerForDateRemainderOpen = function($event) {
            $scope.datePickerForDateRemainder.status.opened = true;
        };
}]);
