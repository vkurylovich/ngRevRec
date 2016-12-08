(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('RevCycle', RevCycle);

    RevCycle.$inject = ['$resource'];

    function RevCycle ($resource) {
        var resourceUrl =  'revrecapp/' + 'api/rev-cycles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
