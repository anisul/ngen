'use strict';

angular.module('ngenApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


