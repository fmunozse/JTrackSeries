'use strict';

angular.module('jtrackseriesApp')
    .factory('CalendarService', function ($http) {
        return {
            findAll: function () {
                return $http.get('api/audits/').then(function (response) {
                    return response.data;
                });
            },
            episodesByDates: function (fromDate, toDate) {

                var formatDate =  function (dateToFormat) {
                    if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
                        return dateToFormat.getYear() + '-' + dateToFormat.getMonth() + '-' + dateToFormat.getDay();
                    }
                    return dateToFormat;
                };

                return $http.get('api/calendar/episodesByDates/' + formatDate(fromDate) + '/' +  formatDate(toDate)).then(function (response) {
                    return response.data;
                });
            }
        };
    });