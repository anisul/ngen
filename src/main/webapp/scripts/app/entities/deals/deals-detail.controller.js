'use strict';

angular.module('ngenApp')
    .controller('DealsDetailController', function ($scope, $rootScope, $stateParams, entity, Deals, Restaurant) {
        $scope.deals = entity;
        $scope.load = function (id) {
            Deals.get({id: id}, function(result) {
                $scope.deals = result;
            });
        };
        var unsubscribe = $rootScope.$on('ngenApp:dealsUpdate', function(event, result) {
            $scope.deals = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
