'use strict';

angular.module('jtrackseriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('scratch', {
                parent: 'site',
                url: '/scratch',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'scratch series'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/scratch/scratch-series.html',
                        controller: 'ScratchSeriesController'
                    }
                },
                resolve: {
                }
            })
    });
