'use strict';

angular.module('jtrackseriesApp')
    .controller('MainController', function ($state, $scope, $modal, Principal, $log, Serie) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            
            Serie.getStatSeries( function (result, header) {
            	angular.forEach(result, function (stat) {
            		var pending = stat.totalEpisodes - stat.totalViewed;
                    if (pending > 0) {    
            			var entryViewed = {
            					"stat": stat, 
            					"value": stat.totalViewed 
            					};
            			var entryTotal = {
            					"stat": stat, 
            					"value": pending 
            					};            			
            			$scope.data[0].values.push(entryViewed);            			
            			$scope.data[1].values.push(entryTotal);
                    }
            	});
            });
                        
            $scope.options = {
	            chart: {
	            	multibar: {
	            		dispatch: {
	            			elementClick: function(e){ 
	                        	$state.go('serie.detail',{id:e.data.stat.serie.id}); 
	                        }
	                    }
	                },	            	
	                type: 'multiBarHorizontalChart',	                
	                height: 450,                
	                x: function(d){return d.stat.serie.title + " (" + d.stat.season + ")";},
	                y: function(d){return d.value;},
	                stacked: true,
	                showControls: true,
	                showValues: true,
	                duration: 500,
	                "margin": {
	                    "top": 20,
	                    "right": 100,
	                    "bottom": 40,
	                    "left": 200
	                  },	                
	                xAxis: {
	                    showMaxMin: false
	                },
	                yAxis: {
	                    axisLabel: 'Episodes ( Viewed / Pending)',
	                    tickFormat: function(d){
	                        return d3.format('.2')(d);
	                    }
	                }
	            }
	        };

	        $scope.data = [
	            {
	                "key": "Viewed",
	                "color": "#AEC7E8",
	                "values": []	                
	            },
	            {
	                "key": "Pending",
	                "color": "#1f77b4",
	                "values": []
	            }
	        ]
        
        });
        
        
        $scope.openModalImage = function (imageSrc, imageDescription) {
        	$modal.open({
        		templateUrl: "scripts/app/main/modalImage.html",
        		resolve: {
        			imageSrcToUse: function () {
        				return imageSrc;
        			},
        			imageDescriptionToUse: function () {
        				return imageDescription;
        			}
        		},
        		controller: [
        		  "$scope", "imageSrcToUse", "imageDescriptionToUse",
        			function ($scope, imageSrcToUse, imageDescriptionToUse) {
        				$scope.ImageSrc = imageSrcToUse;
        				return $scope.ImageDescription = imageDescriptionToUse;
        		  }
        		]
        	});
        };
        
    });
