'use strict';

describe('Season Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockSeason, MockSerie, MockEpisode;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockSeason = jasmine.createSpy('MockSeason');
        MockSerie = jasmine.createSpy('MockSerie');
        MockEpisode = jasmine.createSpy('MockEpisode');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Season': MockSeason,
            'Serie': MockSerie,
            'Episode': MockEpisode
        };
        createController = function() {
            $injector.get('$controller')("SeasonDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jTrackSeriesApp:seasonUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
