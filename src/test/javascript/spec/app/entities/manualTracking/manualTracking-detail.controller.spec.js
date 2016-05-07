'use strict';

describe('Controller Tests', function() {

    describe('ManualTracking Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockManualTracking, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockManualTracking = jasmine.createSpy('MockManualTracking');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ManualTracking': MockManualTracking,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ManualTrackingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jtrackseriesApp:manualTrackingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
