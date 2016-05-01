'use strict';

angular.module('jtrackseriesApp')
    .controller('StatsRecordsController', function ($scope, $log, DateUtils, Serie, Episode, User, AuditsService) {
        $scope.updatingStatsSerie = true;
        $scope.updatingStatsEpisode = true;
        $scope.updatingStatsNumRecords = true;

        
        $scope.separator = '.';
        $scope.dataStatsSeries = [{
        	color: '#1f77b4',
        	key: "Series",
            values: []
         }];
        $scope.dataStatsEpisodes = [{
        	color: '#ff7f0e',
        	key: "Episodes",
            values: []
         }];
        $scope.dataStatsUsers = [{
        	color: '#2ca02c',
        	key: "Users",
            values: []
        }];
        $scope.dataStatsAudits = [{
        	color: '#d62728',
        	key: "Audits",
        	values: []
    }];        	
        
        $scope.refresh = function () {        	
            
            //clean previous data
            $scope.dataStatsUsers[0].values.length = 0; // User
            $scope.dataStatsAudits[0].values.length = 0; // Audits
            $scope.dataStatsSeries[0].values.length = 0; // Series
            $scope.dataStatsEpisodes[0].values.length = 0; // Episodes
                        
            User.getStatsRecordsByYearMonth( function (result, header) {
                $scope.updatingStatsNumRecords = false;
            	angular.forEach(result, function (stat) {            		
        			var entry = {
        					"x": DateUtils.convertLocaleDateFromServer(stat.createDate).getTime(), 
        					"y": stat.totalRecords 
        					};
            		$log.debug("User entry", entry);
            		$scope.dataStatsUsers[0].values.push (entry);            		
            	});            	
            }); // end User.getStatsRecordsByYearMonth
            
            AuditsService.getStatsRecordsByYearMonth().then( function (result) {
                $scope.updatingStatsNumRecords = false;
            	angular.forEach(result, function (stat) {            		
        			var entry = {
        					"x": DateUtils.convertLocaleDateFromServer(stat.createDate).getTime(), 
        					"y": stat.totalRecords 
        					};
            		$log.debug("Audtis entry", new Date(entry.x), entry.y);
            		$scope.dataStatsAudits[0].values.push (entry);            		
            	});            	
            }); // end Audits.getStatsRecordsByYearMonth

            Serie.getStatsRecordsByYearMonth( function (result, header) {
            	angular.forEach(result, function (stat) {            		
        			var entry = {
        					"x": DateUtils.convertLocaleDateFromServer(stat.createDate).getTime(), 
        					"y": stat.totalRecords 
        					};        			
            		$log.debug("Serie entry", entry);
            		$scope.dataStatsSeries[0].values.push (entry);            		
            	});            	
            }); // end Serie.getStatsRecordsByYearMonth               

            Episode.getStatsRecordsByYearMonth( function (result, header) {
            	angular.forEach(result, function (stat) {            		
        			var entry = {
        					"x": DateUtils.convertLocaleDateFromServer(stat.createDate).getTime(), 
        					"y": stat.totalRecords 
        					};        			
            		$log.debug("Episode entry", entry);
            		$scope.dataStatsEpisodes[0].values.push (entry);            		
            	});            	
            }); // end Episode.getStatsRecordsByYearMonth            
            
        }; //end refresh
        
        $scope.refresh();

        $scope.optionsStatsUsers = {
            chart: {
                type: 'lineChart',
                height: 225,
                margin : {
                    top: 10,
                    right: 0,
                    bottom: 40,
                    left: 30
                },
                showLegend: false,
                lines: { //options for basic line model; main chart
                    forceY: 0
                },
                x: function(d){ return d.x; },
                y: function(d){ return d.y; },
                useInteractiveGuideline: true,
                xAxis: {
                    rotateLabels: 30,
                    tickFormat: function(d){
                        return d3.time.format('%m-%Y')(new Date(d))
                    }                       
                },
                yAxis: {
                    tickFormat: function(d){
                        return d3.format('.01')(d);
                    },
                    axisLabelDistance: -10
                }
            },
            title: {
                enable: true,
                text: 'Records x Month of Users'
            }               
        };
        
        $scope.optionsStatsAudits = {
                chart: {
                    type: 'lineChart',
                    height: 225,
                    margin : {
                        top: 10,
                        right: 0,
                        bottom: 40,
                        left: 30
                    },
                    showLegend: false,                    
                    lines: { //options for basic line model; main chart
                        forceY: 0
                    },
                    x: function(d){ return d.x; },
                    y: function(d){ return d.y; },
                    useInteractiveGuideline: true,
                    xAxis: {
                        rotateLabels: 30,
                        tickFormat: function(d){
                            return d3.time.format('%m-%Y')(new Date(d))
                        }                    
                    },
                    yAxis: {
                        tickFormat: function(d){
                            return d3.format('.01')(d);
                        },
                        axisLabelDistance: -10
                    }
                },
                title: {
                    enable: true,
                    text: 'Records x Month of Audits'
                }               
            };

        $scope.optionsStatsSeries = {
                chart: {
                    type: 'lineChart',
                    height: 225,
                    margin : {
                        top: 10,
                        right: 0,
                        bottom: 40,
                        left: 30
                    },
                    showLegend: false,                    
                    lines: { //options for basic line model; main chart
                        forceY: 0
                    },
                    //forceY:0,                    
                    x: function(d){ return d.x; },
                    y: function(d){ return d.y; },
                    useInteractiveGuideline: true,
                    xAxis: {
                        rotateLabels: 30,
                        tickFormat: function(d){
                            return d3.time.format('%m-%Y')(new Date(d))
                        }                    
                    },
                    yAxis: {
                        tickFormat: function(d){
                            return d3.format('.01')(d);
                        },
                        axisLabelDistance: -10
                    }
                },
                title: {
                    enable: true,
                    text: 'Records x Month of Series'
                }               
            };
            
        $scope.optionsStatsEpisodes = {
                chart: {
                    type: 'lineChart',
                    height: 225,
                    margin : {
                        top: 10,
                        right: 0,
                        bottom: 40,
                        left: 30
                    },
                    showLegend: false,                    
                    lines: { //options for basic line model; main chart
                        forceY: 0
                    },
                    //forceY:0,                    
                    x: function(d){ return d.x; },
                    y: function(d){ return d.y; },
                    useInteractiveGuideline: true,
                    xAxis: {
                        showMaxMin: true,
                        rotateLabels: 30,
                        tickFormat: function(d){
                            return d3.time.format('%m-%Y')(new Date(d))
                        }                        
                    },
                    yAxis: {
                        tickFormat: function(d){
                            return d3.format('.01')(d);
                        },
                        axisLabelDistance: -10
                    }
                },
                title: {
                    enable: true,
                    text: 'Records x Month of Episodes'
                }               
            };
    });