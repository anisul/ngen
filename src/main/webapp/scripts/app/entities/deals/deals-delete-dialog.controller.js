'use strict';

angular.module('ngenApp')
	.controller('DealsDeleteController', function($scope, $uibModalInstance, entity, Deals) {

        $scope.deals = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Deals.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
