'use strict';

angular.module('ngenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('restaurant', {
                parent: 'entity',
                url: '/restaurants',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ngenApp.restaurant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/restaurant/restaurants.html',
                        controller: 'RestaurantController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('restaurant');
                        $translatePartialLoader.addPart('restaurantType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('restaurant.detail', {
                parent: 'entity',
                url: '/restaurant/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ngenApp.restaurant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/restaurant/restaurant-detail.html',
                        controller: 'RestaurantDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('restaurant');
                        $translatePartialLoader.addPart('restaurantType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Restaurant', function($stateParams, Restaurant) {
                        return Restaurant.get({id : $stateParams.id});
                    }]
                }
            })
            .state('restaurant.new', {
                parent: 'restaurant',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/restaurant/restaurant-dialog.html',
                        controller: 'RestaurantDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('restaurant', null, { reload: true });
                    }, function() {
                        $state.go('restaurant');
                    })
                }]
            })
            .state('restaurant.edit', {
                parent: 'restaurant',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/restaurant/restaurant-dialog.html',
                        controller: 'RestaurantDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Restaurant', function(Restaurant) {
                                return Restaurant.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('restaurant', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('restaurant.delete', {
                parent: 'restaurant',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/restaurant/restaurant-delete-dialog.html',
                        controller: 'RestaurantDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Restaurant', function(Restaurant) {
                                return Restaurant.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('restaurant', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
