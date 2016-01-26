'use strict';

angular.module('jtrackseriesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


