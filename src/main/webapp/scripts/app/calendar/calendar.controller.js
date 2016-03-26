'use strict';

angular.module('jtrackseriesApp')
    .controller('CalendarController', function ($scope, $state, CalendarService, Episode, ParseLinks, $log, DateUtils, $compile, EpisodeViewed, uiCalendarConfig) {

        $scope.episode = new Object();
        $scope.eventDetailSelected = new Object();        

        var onSaveSuccess = function (result) {
			$log.debug("onSaveSuccess - result", result);

            $scope.$emit('jtrackseriesApp:episodeUpdate', result);
            $scope.isSaving = false;
        };
        var onSaveError = function (result) {
            $scope.isSaving = false;
        };        
        
        
        $scope.pad = function (num, size) {
            var s = num+"";
            while (s.length < size) s = "0" + s;
            return s;
        }
        
 
        $scope.setViewed = function (event) {       

        	EpisodeViewed.update({id: event.id, set: !event.episode.viewed},
            	function (result, onSaveSuccess, onSaveError) {
        			$log.debug("result", result);
        			//$log.debug("header", header);
            		event.color = (result.viewed) ? '#A3A3A3' : "#337ab7";
            		event.episode=result;
        			uiCalendarConfig.calendars.calendar.fullCalendar('rerenderEvents')        			
        		});            
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
                $scope.setViewed(event);      
                //$scope.eventClicked = event;
                //$scope.eventDetailSelected = event;
            } ,
            eventDrop:  function(event) {
                $log.debug("eventDrop",  event);
                Episode.get({id : event.id}, function(result) {
                    result.datePublish = event._start;
                	$scope.episode = result;
                    Episode.update($scope.episode, onSaveSuccess, onSaveError);
                });
            } ,    
            eventMouseover: function(event) {
                $scope.eventDetailSelected = event;        
            }, 
            eventRender:  function(event, element) {            	
                element.attr({
                	'uib-popover-template': "'scripts/app/calendar/eventDetail.html'",
                    'popover-trigger': 'mouseenter',
                    'popover-title': event.title,
                    'tabindex': 0,
                    
                    'popover-append-to-body': true
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
                        	eventCalendar.title = episode.serie.title + " :: " + episode.season + "x" + $scope.pad(episode.episodeNumber,2);
                        	eventCalendar.start = DateUtils.convertLocaleDateToServer(episode.datePublish); 
                        	eventCalendar.allDay=true;
                        	eventCalendar.color = (episode.viewed) ? '#A3A3A3' : "#337ab7";
                        	eventCalendar.episode=episode;
                        	return eventCalendar;
                        };
                        newArr.push( transformEventCalendar(episode) );
                    });
                    $scope.eventSources[0] = newArr;                    
                });                

            }            
          }
        };

    });