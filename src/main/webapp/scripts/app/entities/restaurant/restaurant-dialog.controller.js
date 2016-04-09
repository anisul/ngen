'use strict';

angular.module('ngenApp').controller('RestaurantDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Restaurant', 'Deals',
        function($scope, $stateParams, $uibModalInstance, entity, Restaurant, Deals) {

        $scope.restaurant = entity;
        $scope.dealss = Deals.query();
        $scope.load = function(id) {
            Restaurant.get({id : id}, function(result) {
                $scope.restaurant = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ngenApp:restaurantUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.restaurant.id != null) {
                Restaurant.update($scope.restaurant, onSaveSuccess, onSaveError);
            } else {
                Restaurant.save($scope.restaurant, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
