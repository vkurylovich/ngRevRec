(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .filter("getMonthFromNumberFilter", function(){
            return function(number){
                if(isNaN(number)||number<1){
                   return number;
                }
                 else{
                   var n = ["January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
                   return n[number-1];
                 }
            }
        })
        .controller('RevCycleController', RevCycleController);

    RevCycleController.$inject = ['$scope', '$state', 'RevCycle'];    

    function RevCycleController ($scope, $state, RevCycle) {
        var vm = this;

        vm.revCycles = [];

        loadAll();

        function loadAll() {
            RevCycle.query(function(result) {
                vm.revCycles = result;
                vm.searchQuery = null;
            });
        }
    }
})();
