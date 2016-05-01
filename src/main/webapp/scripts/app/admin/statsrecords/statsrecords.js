'use strict';

angular.module('jtrackseriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('statsrecords', {
                parent: 'admin',
                url: '/statsrecords',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Stats Records'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/statsrecords/statsrecords.html',
                        controller: 'StatsRecordsController'
                    }
                },
                resolve: {
                    
                }
            });
    });