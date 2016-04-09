'use strict';

describe('Controller Tests', function() {

    describe('Deals Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDeals, MockRestaurant;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDeals = jasmine.createSpy('MockDeals');
            MockRestaurant = jasmine.createSpy('MockRestaurant');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Deals': MockDeals,
                'Restaurant': MockRestaurant
            };
            createController = function() {
                $injector.get('$controller')("DealsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ngenApp:dealsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
