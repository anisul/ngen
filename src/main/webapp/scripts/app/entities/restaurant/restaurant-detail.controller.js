'use strict';

angular.module('ngenApp')
    .controller('RestaurantDetailController', function ($scope, $rootScope, $stateParams, entity, Restaurant, Deals) {
        $scope.restaurant = entity;
        $scope.load = function (id) {
            Restaurant.get({id: id}, function(result) {
                $scope.restaurant = result;
            });
        };
        var unsubscribe = $rootScope.$on('ngenApp:restaurantUpdate', function(event, result) {
            $scope.restaurant = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
