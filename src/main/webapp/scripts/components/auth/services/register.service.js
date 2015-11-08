'use strict';

angular.module('jTrackSeriesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


