'use strict';

angular.module('jtrackseriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('manualTracking', {
                parent: 'entity',
                url: '/manualTrackings',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ManualTrackings'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/manualTracking/manualTrackings.html',
                        controller: 'ManualTrackingController'
                    }
                },
                resolve: {
                }
            })
            .state('manualTracking.detail', {
                parent: 'entity',
                url: '/manualTracking/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ManualTracking'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/manualTracking/manualTracking-detail.html',
                        controller: 'ManualTrackingDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ManualTracking', function($stateParams, ManualTracking) {
                        return ManualTracking.get({id : $stateParams.id});
                    }]
                }
            })
            .state('manualTracking.new', {
                parent: 'manualTracking',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/manualTracking/manualTracking-dialog.html',
                        controller: 'ManualTrackingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    season: null,
                                    totalEpisodes: null,
                                    lastViewed: null,
                                    dateRemainder: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('manualTracking', null, { reload: true });
                    }, function() {
                        $state.go('manualTracking');
                    })
                }]
            })
            .state('manualTracking.edit', {
                parent: 'manualTracking',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/manualTracking/manualTracking-dialog.html',
                        controller: 'ManualTrackingDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ManualTracking', function(ManualTracking) {
                                return ManualTracking.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('manualTracking', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('manualTracking.delete', {
                parent: 'manualTracking',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/manualTracking/manualTracking-delete-dialog.html',
                        controller: 'ManualTrackingDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ManualTracking', function(ManualTracking) {
                                return ManualTracking.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('manualTracking', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
