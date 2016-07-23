$(document).ready(function () {
    ko.applyBindings(new ViewModel());
});

function initMap() {
    // Dummy google callback
}

function ViewModel() {
    var self = this;

    self.autoRefresh = ko.observable(true);
    self.autoRefreshText = ko.observable("");
    self.googleMap = ko.observable({
        lat: 49.264374,
        lng: -123.1189124
    });
    self.pokemonMarkers = ko.observableArray([]);

    self.toggleAutoRefresh = function() {
        self.autoRefresh(!self.autoRefresh());
    };

    self.addMarker = function(location) {
        var marker = new google.maps.Marker({
            map: self.googleMap().map,
            position: {lat: location.lat, lng: location.lng},
            title: location.title,
            icon: {
                url: location.iconUri,
                scaledSize: new google.maps.Size(32, 32),
                origin: new google.maps.Point(0, 0),
                anchor: new google.maps.Point(16, 32)
            }
        });
        var infoWindow = new google.maps.InfoWindow({
            content: location.content
        });
        marker.addListener('click', function() {
            infoWindow.open(self.googleMap().map, marker);
        });
        self.pokemonMarkers.push(marker);
    };

    self.clearAllMarkers = function() {
        while(self.pokemonMarkers().length > 0) {
            var marker = self.pokemonMarkers.pop();
            marker.setMap(null);
        }
    };

    self.getTestLocations = function() {
        $.ajax({
            url: '/getTestLocations'
        }).then(function(locations) {
            self.clearAllMarkers();
            for (var i = 0; i < locations.length; i++) {
                self.addMarker(locations[i]);
            }
        });
    };

    self.initSubscription = function() {
        var intervalId;
        self.autoRefresh.subscribe(function(needAutoRefresh) {
            if(needAutoRefresh) {
                intervalId = setInterval(self.getTestLocations, 5000);
                self.autoRefreshText("Stop auto refresh");
            }
            else {
                clearInterval(intervalId);
                self.autoRefreshText("Start auto refresh");
            }
        });
    };

    self.init = function () {
        self.initSubscription();
    };

    self.init();
}

ko.bindingHandlers.map = {

    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var map = ko.utils.unwrapObservable(valueAccessor());

        var latLng = new google.maps.LatLng(
            ko.utils.unwrapObservable(map.lat),
            ko.utils.unwrapObservable(map.lng)
        );

        var mapOptions = {
            center: latLng,
            zoom: 15
        };

        map.map = new google.maps.Map(element, mapOptions);
    }
};

var viewModel = new ViewModel();

// var map;
// var markers = [];
//
// var addMarker = function(location) {
//     var marker = new google.maps.Marker({
//         map: map,
//         position: {lat: location.lat, lng: location.lng},
//         title: location.title,
//         icon: {
//             url: location.iconUri,
//             scaledSize: new google.maps.Size(32, 32),
//             origin: new google.maps.Point(0, 0),
//             anchor: new google.maps.Point(16, 32)
//         }
//     });
//     var infoWindow = new google.maps.InfoWindow({
//         content: location.content
//     });
//     marker.addListener('click', function() {
//         infoWindow.open(map, marker);
//     });
//     markers.push(marker);
// };
//
// var setMapOnAll = function(map) {
//     for (var i = 0; i < markers.length; i++) {
//         markers[i].setMap(map);
//     }
// };
//
// var getTestLocations = function() {
//     $.ajax({
//         url: '/getTestLocations'
//     }).then(function(locations) {
//         setMapOnAll(null);
//         for (var i = 0; i < locations.length; i++) {
//             addMarker(locations[i]);
//         }
//     });
// };
//
// function initMap() {
//     map = new google.maps.Map(document.getElementById('map'), {
//         center: {lat: 49.264374, lng: -123.1189124},
//         zoom: 15
//     });
//     getTestLocations();
// }
//
// setInterval(getTestLocations, 5000);