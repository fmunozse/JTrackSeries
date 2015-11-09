'use strict';

angular.module('jTrackSeriesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('season', {
                parent: 'entity',
                url: '/seasons',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Seasons'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/season/seasons.html',
                        controller: 'SeasonController'
                    }
                },
                resolve: {
                }
            })
            .state('season.detail', {
                parent: 'entity',
                url: '/season/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Season'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/season/season-detail.html',
                        controller: 'SeasonDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Season', function($stateParams, Season) {
                        return Season.get({id : $stateParams.id});
                    }]
                }
            })
            .state('season.new', {
                parent: 'season',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/season/season-dialog.html',
                        controller: 'SeasonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    orderNumber: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('season', null, { reload: true });
                    }, function() {
                        $state.go('season');
                    })
                }]
            })
            .state('season.edit', {
                parent: 'season',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/season/season-dialog.html',
                        controller: 'SeasonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Season', function(Season) {
                                return Season.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('season', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
