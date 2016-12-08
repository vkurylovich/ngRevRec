(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rev-cycle', {
            parent: 'entity',
            url: '/rev-cycle',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RevCycles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rev-cycle/rev-cycles.html',
                    controller: 'RevCycleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('rev-cycle-detail', {
            parent: 'entity',
            url: '/rev-cycle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'RevCycle'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rev-cycle/rev-cycle-detail.html',
                    controller: 'RevCycleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'RevCycle', function($stateParams, RevCycle) {
                    return RevCycle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rev-cycle',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rev-cycle-detail.edit', {
            parent: 'rev-cycle-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rev-cycle/rev-cycle-dialog.html',
                    controller: 'RevCycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevCycle', function(RevCycle) {
                            return RevCycle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rev-cycle.new', {
            parent: 'rev-cycle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rev-cycle/rev-cycle-dialog.html',
                    controller: 'RevCycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                month: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rev-cycle', null, { reload: 'rev-cycle' });
                }, function() {
                    $state.go('rev-cycle');
                });
            }]
        })
        .state('rev-cycle.edit', {
            parent: 'rev-cycle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rev-cycle/rev-cycle-dialog.html',
                    controller: 'RevCycleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RevCycle', function(RevCycle) {
                            return RevCycle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rev-cycle', null, { reload: 'rev-cycle' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rev-cycle.delete', {
            parent: 'rev-cycle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rev-cycle/rev-cycle-delete-dialog.html',
                    controller: 'RevCycleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RevCycle', function(RevCycle) {
                            return RevCycle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rev-cycle', null, { reload: 'rev-cycle' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
