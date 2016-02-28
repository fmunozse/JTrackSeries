'use strict';

angular.module('jtrackseriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calendar', {
                parent: 'site',
                url: '/calendar',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Calendar'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/calendar/calendar.html',
                        controller: 'CalendarController'
                    }
                },
                resolve: {
                }
            })
    });
