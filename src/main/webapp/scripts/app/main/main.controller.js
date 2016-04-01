'use strict';

angular.module('jtrackseriesApp')
    .controller('MainController', function ($scope, $modal, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
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
