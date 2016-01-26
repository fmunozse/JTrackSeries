'use strict';

angular.module('jtrackseriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('episode', {
                parent: 'entity',
                url: '/episodes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Episodes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/episode/episodes.html',
                        controller: 'EpisodeController'
                    }
                },
                resolve: {
                }
            })
            .state('episode.detail', {
                parent: 'entity',
                url: '/episode/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Episode'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/episode/episode-detail.html',
                        controller: 'EpisodeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Episode', function($stateParams, Episode) {
                        return Episode.get({id : $stateParams.id});
                    }]
                }
            })
            .state('episode.new', {
                parent: 'episode',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/episode/episode-dialog.html',
                        controller: 'EpisodeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    datePublish: null,
                                    viewed: false,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('episode', null, { reload: true });
                    }, function() {
                        $state.go('episode');
                    })
                }]
            })
            .state('episode.edit', {
                parent: 'episode',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/episode/episode-dialog.html',
                        controller: 'EpisodeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Episode', function(Episode) {
                                return Episode.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('episode', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('episode.delete', {
                parent: 'episode',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/episode/episode-delete-dialog.html',
                        controller: 'EpisodeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Episode', function(Episode) {
                                return Episode.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('episode', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
