 'use strict';

angular.module('jTrackSeriesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jTrackSeriesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jTrackSeriesApp-params')});
                }
                return response;
            }
        };
    });
