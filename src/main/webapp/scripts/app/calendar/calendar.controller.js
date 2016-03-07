'use strict';

angular.module('jtrackseriesApp')
    .controller('CalendarController', function ($scope, $state, CalendarService, Episode, ParseLinks, $log, DateUtils, $compile) {

        $scope.episode = new Object();

        var onSaveSuccess = function (result) {
            $scope.$emit('jtrackseriesApp:episodeUpdate', result);
            $scope.isSaving = false;
        };
        var onSaveError = function (result) {
            $scope.isSaving = false;
        };        
        
        //$scope.eventSources = [$scope.events];
        $scope.eventSources = [];
        
        /* config object */
        $scope.uiConfig = {
          calendar:{
            height: 450,
            editable: true,
            firstDay:1, 
            header:{
              left: 'month basicWeek',
              center: 'title',
              right: 'today prev,next'
            },
            dayClick: function(event) {
                $log.debug("dayClick" , event);
            } ,    
            eventClick:  function(event) {
                $log.debug("eventClick",  event);
            } ,
            eventDrop:  function(event) {
                $log.debug("eventDrop",  event);
                Episode.get({id : event.id}, function(result) {
                    result.datePublish = event._start;
                	$scope.episode = result;
                    Episode.update($scope.episode, onSaveSuccess, onSaveError);
                });
            } ,    
            eventRender:  function(event, element) {
                //$log.debug("eventRender",  event);                
                element.attr({
                    'uib-tooltip': event.title,
                    'tooltip-append-to-body': true
                });
                $compile(element)($scope);
            } ,   
            viewRender: function(view, element) {
                $log.debug("View Changed: ", view.visStart, view.visEnd, view.start, view.end);
                
                var fromDate = DateUtils.convertLocaleDateToServer(view.start._d) ;
                var toDate = DateUtils.convertLocaleDateToServer(view.end._d); 
                
                CalendarService.episodesByDates(fromDate, toDate).then(function (episodes) {
                    var newArr = [];
                    angular.forEach(episodes, function (episode) {
                        $log.debug("episode:: ", episode );
                        var transformEventCalendar = function (episode) {
                        	var eventCalendar = new Object();
                        	eventCalendar.id = episode.id;
                        	eventCalendar.title = episode.serie.title + " :: " + episode.title;
                        	eventCalendar.start = DateUtils.convertLocaleDateToServer(episode.datePublish); 
                        	eventCalendar.allDay=true;
                        	return eventCalendar;
                        };
                        newArr.push( transformEventCalendar(episode) );
                    });
                    $scope.eventSources[0] = newArr;                    
                });                

            }            
          }
        };
        
        /* Render Tooltip */
//       $scope.eventRender = function( event, element, view ) {
//           element.attr({'tooltip': event.title,
//                         'tooltip-append-to-body': true});
//           $compile(element)($scope);
//       };
        
    });