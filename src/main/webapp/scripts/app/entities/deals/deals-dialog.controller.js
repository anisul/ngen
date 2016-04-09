'use strict';

angular.module('ngenApp').controller('DealsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Deals', 'Restaurant',
        function($scope, $stateParams, $uibModalInstance, entity, Deals, Restaurant) {

        $scope.deals = entity;
        $scope.restaurants = Restaurant.query();
        $scope.load = function(id) {
            Deals.get({id : id}, function(result) {
                $scope.deals = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('ngenApp:dealsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.deals.id != null) {
                Deals.update($scope.deals, onSaveSuccess, onSaveError);
            } else {
                Deals.save($scope.deals, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartDate = {};

        $scope.datePickerForStartDate.status = {
            opened: false
        };

        $scope.datePickerForStartDateOpen = function($event) {
            $scope.datePickerForStartDate.status.opened = true;
        };
        $scope.datePickerForEndDate = {};

        $scope.datePickerForEndDate.status = {
            opened: false
        };

        $scope.datePickerForEndDateOpen = function($event) {
            $scope.datePickerForEndDate.status.opened = true;
        };
}]);
