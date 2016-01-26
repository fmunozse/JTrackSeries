'use strict';

describe('Controller Tests', function() {

    describe('Episode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEpisode, MockSeason;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEpisode = jasmine.createSpy('MockEpisode');
            MockSeason = jasmine.createSpy('MockSeason');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Episode': MockEpisode,
                'Season': MockSeason
            };
            createController = function() {
                $injector.get('$controller')("EpisodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jtrackseriesApp:episodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
