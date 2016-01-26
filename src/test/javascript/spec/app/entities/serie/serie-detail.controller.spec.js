'use strict';

describe('Controller Tests', function() {

    describe('Serie Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSerie, MockSeason;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSerie = jasmine.createSpy('MockSerie');
            MockSeason = jasmine.createSpy('MockSeason');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Serie': MockSerie,
                'Season': MockSeason
            };
            createController = function() {
                $injector.get('$controller')("SerieDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jtrackseriesApp:serieUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
