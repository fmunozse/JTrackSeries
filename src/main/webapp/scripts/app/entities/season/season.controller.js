'use strict';

angular.module('jTrackSeriesApp')
    .controller('SeasonController', function ($scope, Season, ParseLinks) {
        $scope.seasons = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Season.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.seasons = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Season.get({id: id}, function(result) {
                $scope.season = result;
                $('#deleteSeasonConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Season.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSeasonConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.season = {
                title: null,
                orderNumber: null,
                id: null
            };
        };
    });
