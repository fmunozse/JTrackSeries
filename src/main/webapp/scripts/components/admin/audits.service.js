'use strict';

angular.module('jtrackseriesApp')
    .factory('AuditsService', function ($http) {
        return {
            getStatsRecordsByYearMonth:  function () {
                return $http.get('api/audits/statsRecordsByYearMonth').then(function (response) {
                    return response.data;
                });	
            },        	
            findAll: function () {
                return $http.get('api/audits/').then(function (response) {
                    return response.data;
                });
            },
            findByDates: function (fromDate, toDate) {

                var formatDate =  function (dateToFormat) {
                    if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
                        return dateToFormat.getYear() + '-' + dateToFormat.getMonth() + '-' + dateToFormat.getDay();
                    }
                    return dateToFormat;
                };

                return $http.get('api/audits/', {params: {fromDate: formatDate(fromDate), toDate: formatDate(toDate)}}).then(function (response) {
                    return response.data;
                });
            }
        };
    });
