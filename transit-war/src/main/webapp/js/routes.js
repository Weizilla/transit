(function() {
    var app = angular.module("transit", []);

    app.controller("RoutesController", function($http) {
        var vm = this;
        vm.routes = [];

        $http.get("api/routes").success(function(data) {
            vm.routes = data;
        });
    });
})();