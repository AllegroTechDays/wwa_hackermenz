import { Component, Input, AfterViewInit, ViewChild, ElementRef } from '@angular/core';

// key: AIzaSyDhFOr6iwopYe7ij_w6Wa3dT3Fc6S9L33c

declare const google: any;

@Component({
  selector: 'route-preview',
  templateUrl: 'route-preview.html'
})
export class RoutePreviewComponent implements AfterViewInit {

  @Input() path: any
  @Input() markers: any
  @ViewChild('map') mapEl: ElementRef
  mapPath: any;
  map: any

  constructor() { }

  ngAfterViewInit() {
    setTimeout(() => {
      this.map = new google.maps.Map(this.mapEl.nativeElement, {
        zoom: 3,
        center: {lat: 0, lng: -180},
        mapTypeId: 'terrain'
      });
      if (this.markers) {
        this.markers.forEach((marker) => {
          new google.maps.Marker({
            position: marker,
            map: this.map
          });
        })
      }
      this.drawPath()
    }, 500)
  }

  drawPath() {
    if (this.mapPath) {
      this.mapPath.setMap(null)
    }
    this.mapPath = new google.maps.Polyline({
      path: this.path,
      geodesic: true,
      strokeColor: '#FF0000',
      strokeOpacity: 1.0,
      strokeWeight: 2
    });
    this.mapPath.setMap(this.map);

    var bounds = new google.maps.LatLngBounds();
    this.path.forEach((item) => {
      bounds.extend(new google.maps.LatLng(item['lat'], item['lng']));
    })
    this.map.fitBounds(bounds);
  }

  ngOnChanges() {
    if (this.map) {
      this.drawPath()
    }
  }

}
