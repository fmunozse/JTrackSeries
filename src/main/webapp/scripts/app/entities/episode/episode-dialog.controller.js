'use strict';

angular.module('jtrackseriesApp').controller('EpisodeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Episode', 'Serie', 'DateUtil',
        function($scope, $stateParams, $uibModalInstance, entity, Episode, Serie, DateUtil) {

    	$scope.dateTimeformat = DateUtils.dateTimeformat();    	
        $scope.episode = entity;
        $scope.series = Serie.query();
        $scope.load = function(id) {
            Episode.get({id : id}, function(result) {
                $scope.episode = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jtrackseriesApp:episodeUpdate', result);
            $uibModalInstance.close(result);
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
            $uibModalInstance.dismiss('cancel');
        };
        
        $scope.datePickerForDatePublish = {};
        $scope.datePickerForDatePublish.status = {
            opened: false
        };
        $scope.datePickerForDatePublishOpen = function($event) {
            $scope.datePickerForDatePublish.status.opened = true;
        };
}]);
