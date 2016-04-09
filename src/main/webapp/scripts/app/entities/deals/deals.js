'use strict';

angular.module('ngenApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('deals', {
                parent: 'entity',
                url: '/dealss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ngenApp.deals.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deals/dealss.html',
                        controller: 'DealsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deals');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('deals.detail', {
                parent: 'entity',
                url: '/deals/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ngenApp.deals.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deals/deals-detail.html',
                        controller: 'DealsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deals');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Deals', function($stateParams, Deals) {
                        return Deals.get({id : $stateParams.id});
                    }]
                }
            })
            .state('deals.new', {
                parent: 'deals',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deals/deals-dialog.html',
                        controller: 'DealsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    startDate: null,
                                    endDate: null,
                                    note: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('deals', null, { reload: true });
                    }, function() {
                        $state.go('deals');
                    })
                }]
            })
            .state('deals.edit', {
                parent: 'deals',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deals/deals-dialog.html',
                        controller: 'DealsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Deals', function(Deals) {
                                return Deals.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('deals', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('deals.delete', {
                parent: 'deals',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deals/deals-delete-dialog.html',
                        controller: 'DealsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Deals', function(Deals) {
                                return Deals.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('deals', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
