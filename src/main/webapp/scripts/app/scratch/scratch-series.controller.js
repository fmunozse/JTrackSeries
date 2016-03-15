'use strict';

angular.module('jtrackseriesApp')
    .controller('ScratchSeriesController', function ($scope, $state, ScratchService, ParseLinks, $log) {

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
            ScratchService.importSerieById( id , 'en').then(function (result) {
            	//$scope.scratchSeries = result;   
                //$state.go('serie');
            	var serie = result;
            	$state.go('serie.detail',{id:serie.id});
                
            });  
        }
 
    });