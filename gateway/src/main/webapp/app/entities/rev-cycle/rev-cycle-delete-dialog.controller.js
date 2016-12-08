(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('RevCycleDeleteController',RevCycleDeleteController);

    RevCycleDeleteController.$inject = ['$uibModalInstance', 'entity', 'RevCycle'];

    function RevCycleDeleteController($uibModalInstance, entity, RevCycle) {
        var vm = this;

        vm.revCycle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RevCycle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
