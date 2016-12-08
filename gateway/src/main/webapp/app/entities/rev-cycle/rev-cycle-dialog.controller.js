(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('RevCycleDialogController', RevCycleDialogController);

    RevCycleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RevCycle'];

    function RevCycleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RevCycle) {
        var vm = this;

        vm.revCycle = entity;
        vm.clear = clear;
        vm.save = save;

        $scope.Months = [{ Value: 1, Name: 'January' }, { Value: 2, Name: 'Febuary' }, { Value: 3, Name: 'March' }, { Value: 4, Name: 'April' }, 
                         { Value: 5, Name: 'May' }, { Value: 6, Name: 'June' }, { Value: 7, Name: 'July' }, { Value: 8, Name: 'August' },
                         { Value: 9, Name: 'September' }, { Value: 10, Name: 'October' }, { Value: 11, Name: 'November' }, { Value: 12, Name: 'December' }];

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.revCycle.id !== null) {
                RevCycle.update(vm.revCycle, onSaveSuccess, onSaveError);
            } else {
                RevCycle.save(vm.revCycle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:revCycleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
