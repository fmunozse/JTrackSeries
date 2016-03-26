'use strict';

angular.module('jtrackseriesApp')
    .controller('ScratchSeriesController', function ($scope, $state, ScratchService, ParseLinks, $log) {

        $scope.success = null;
        $scope.error = null;
        $scope.errorSeriesExists = null;
        
        $scope.title = "fargo";        
        $scope.scratchSeries = [];
       
        $scope.search = function() {        	
        	$log.debug("query: " , $scope.title);
            ScratchService.findSeriesByTitle( $scope.title, 'en').then(function (result) {
                $scope.scratchSeries = result;         
            });               
        }
        
        $scope.import = function(id) {
        	$log.debug("import: " , id);

        	$scope.error = null;
            $scope.errorSeriesExists = null;
            
            ScratchService.importSerieById( id , 'en').then(
            	function successCallback (result) {
	            	$log.debug("import - ok: " , result);
	                $scope.success = 'OK';
	                
	            	var serie = result;
	            	$state.go('serie.detail',{id:serie.id});
                
            	}, function errorCallback (response) {
		            $scope.success = null;
		        	var headers = response.headers();
		        	var msgError = headers['x-jtrackseriesapp-error'];
		        	
		            if (response.status === 400 && msgError === 'Series with externalId already in use') {
		                $scope.errorSeriesExists = 'ERROR';
		            } else {
		                $scope.error = 'ERROR';
		            }
            	});
        }
 
    });