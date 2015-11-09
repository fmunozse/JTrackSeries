'use strict';

angular.module('jTrackSeriesApp')
    .controller('SerieController', function ($scope, Serie, ParseLinks) {
        $scope.series = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Serie.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.series = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Serie.get({id: id}, function(result) {
                $scope.serie = result;
                $('#deleteSerieConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Serie.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSerieConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.serie = {
                title: null,
                externalLink: null,
                description: null,
                notes: null,
                id: null
            };
        };
    });
