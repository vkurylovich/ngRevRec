(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('RevCycleDetailController', RevCycleDetailController);

    RevCycleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RevCycle'];

    function RevCycleDetailController($scope, $rootScope, $stateParams, previousState, entity, RevCycle) {
        var vm = this;

        vm.revCycle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:revCycleUpdate', function(event, result) {
            vm.revCycle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
