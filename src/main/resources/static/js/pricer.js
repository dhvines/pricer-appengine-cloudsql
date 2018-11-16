//noinspection JSUnresolvedFunction
var app = angular.module('pricer', ['ui.bootstrap']);

app.factory('priceHolder', function() {
    return {
        setSymbol: setSymbol,
        getSymbol: getSymbol,
        setValue: setValue,
        getValue: getValue
    };

    function setSymbol(symbol) {this.symbol = symbol; }
    function getSymbol() {return this.symbol; }
    function setValue(value) {this.value = value; }
    function getValue() {return this.value ;}
});

app.controller('prices', function(priceHolder, $scope, $http, $modal) {
    $scope.hideErrorDetails = true;
    $scope.orderColumn = 'symbol';
    $scope.orderReverse = false;

    function hideErrorDetails() {
        $scope.hideErrorDetails = true;
    }

    function displayErrorDetails(response){
        var errorData = response.data;
        $scope.errorDetails = "Status=" + errorData.status + ", Error=" + errorData.error + ", Message=" + errorData.message;
        $scope.hideErrorDetails = false;
    }

    function refreshPrices(){
        $http.get('/prices').then(function (response){
            $scope.prices = response.data;
         }, function onError(response) {
            displayErrorDetails(response);
        });
    }

    function createPrice(symbol, value){
        var price = {'symbol': symbol, 'value': value};

        $http.post('/prices', price).then(function() {
            refreshPrices();
        }, function onError(response) {
            displayErrorDetails(response);
        });
    }

    function deletePrice(symbol){
        $http.delete('/prices/' + symbol).then(function(){
            refreshPrices();
        }, function onError(response){
            displayErrorDetails(response);
        });
    }

    function updatePrice(symbol, value){
        var price = {'symbol': symbol, 'value': value};
        $http.put('/prices', price).then(function(){
            refreshPrices();
         }, function onError(response){
             displayErrorDetails(response);
         });
    }

    $scope.closeErrorDetails = function (){
        hideErrorDetails();
    };

    $scope.refreshPrices = function(){
        hideErrorDetails();
        refreshPrices();
    };

    $scope.createPrice = function(){
        hideErrorDetails();

        priceHolder.setSymbol('');
        priceHolder.setValue('');

        $modal.open({templateUrl: 'createDialog.html', controller: 'createDialog'}).result.then(
            function ok(){
                createPrice(priceHolder.getSymbol(), priceHolder.getValue());
            }
        );
    };

    $scope.deletePrice = function(symbol){
        hideErrorDetails();

        priceHolder.setSymbol(symbol);

        $modal.open({templateUrl: 'deleteDialog.html', controller: 'deleteDialog'}).result.then(
             function ok(){
                 deletePrice(symbol);
             }
        );
    };

    $scope.updatePrice = function(symbol, value){
        hideErrorDetails();

        priceHolder.setSymbol('');
        priceHolder.setValue('');

        $modal.open({templateUrl: 'updateDialog.html', controller: 'updateDialog'}).result.then(
             function ok(){
                 updatePrice(symbol, priceHolder.getValue());
             }
        );
    };

    $scope.orderByColumn = function(column){
        hideErrorDetails();

        if(column == $scope.orderColunm){
            $scope.orderReverse = !$scope.orderReverse;
        }else{
            $scope.orderColunm = column;
            $scope.orderReverse = false;
        }
    };

    $scope.refreshPrices();
});

app.controller("deleteDialog", function(priceHolder, $scope, $modalInstance){
    $scope.symbol = priceHolder.getSymbol();
    $scope.value = priceHolder.getValue();

    $scope.delete = function(){
        $modalInstance.close();
    };

    $scope.cancel = function(){
        $modalInstance.dismiss('cancel');
    };

});

app.controller("createDialog", function(priceHolder, $scope, $modalInstance){
    $scope.symbol = priceHolder.getSymbol();
    $scope.value = priceHolder.getValue();

    $scope.create = function(){
        priceHolder.setSymbol($scope.symbol);
        priceHolder.setValue($scope.value);
        $modalInstance.close();
    };

    $scope.cancel = function(){
        $modalInstance.dismiss('cancel');
    };
});

app.controller("updateDialog", function(priceHolder, $scope, $modalInstance){
    $scope.symbol = priceHolder.getSymbol();
    $scope.value = priceHolder.getValue();

    $scope.update = function(){
        priceHolder.setValue($scope.value);
        $modalInstance.close();
    };

    $scope.cancel = function(){
        $modalInstance.dismiss('cancel');
    };
});

app.directive('focusElement', function($timeout) {
    return {
        scope: {trigger: '@focusElement'},
        link: function(scope, element) {
            scope.$watch('trigger', function(value){
                if(value == "true"){
                    $timeout(function() {
                        element[0].focus();
                    });
                }
            });
        }
    };
});