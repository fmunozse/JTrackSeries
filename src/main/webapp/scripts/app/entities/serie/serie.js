'use strict';

angular.module('jTrackSeriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('serie', {
                parent: 'entity',
                url: '/series',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Series'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serie/series.html',
                        controller: 'SerieController'
                    }
                },
                resolve: {
                }
            })
            .state('serie.detail', {
                parent: 'entity',
                url: '/serie/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Serie'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/serie/serie-detail.html',
                        controller: 'SerieDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Serie', function($stateParams, Serie) {
                        return Serie.get({id : $stateParams.id});
                    }]
                }
            })
            .state('serie.new', {
                parent: 'serie',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/serie/serie-dialog.html',
                        controller: 'SerieDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    externalLink: null,
                                    description: null,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('serie', null, { reload: true });
                    }, function() {
                        $state.go('serie');
                    })
                }]
            })
            .state('serie.edit', {
                parent: 'serie',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/serie/serie-dialog.html',
                        controller: 'SerieDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Serie', function(Serie) {
                                return Serie.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('serie', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
