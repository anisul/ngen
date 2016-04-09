'use strict';

angular.module('ngenApp')
    .controller('RestaurantController', function ($scope, $state, Restaurant, ParseLinks) {

        $scope.restaurants = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Restaurant.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.restaurants = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.restaurant = {
                name: null,
                address: null,
                phone: null,
                verified: null,
                bannerFile: null,
                about: null,
                category: null,
                gmapCode: null,
                id: null
            };
        };
    });
