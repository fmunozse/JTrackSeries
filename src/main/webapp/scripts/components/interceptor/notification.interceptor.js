 'use strict';

angular.module('jtrackseriesApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jtrackseriesApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jtrackseriesApp-params')});
                }
                return response;
            }
        };
    });
