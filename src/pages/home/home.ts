import { Geolocation } from '@ionic-native/geolocation';
import { Component, OnInit } from '@angular/core';
import { NavController, LoadingController, Platform } from 'ionic-angular';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage implements OnInit {

  mode:'distance'|'destination' = 'distance'

  gender: string = 'none'

  distanceForm: FormGroup
  destinationForm: FormGroup
  withReturn: boolean = false

  destination: string
  geolocation: any 
  distance: any = {
    lower: 5,
    upper: 40
  }

  constructor(
    public navCtrl: NavController,
    private loadingCtrl: LoadingController,
    private platform: Platform,
    private geolocationService: Geolocation
  ) { }

  ngOnInit() {
    let loading = this.loadingCtrl.create()
    loading.present()
    if (this.platform.is('cordova')) {
      this.geolocationService.getCurrentPosition().then((resp) => {
        // resp.coords.latitude
        // resp.coords.longitude
        loading.dismiss()
        this.geolocation = {
          latitude: resp.coords.latitude,
          longitude: resp.coords.longitude
        }
       }).catch((error) => {
        loading.dismiss()
        this.geolocation = {
          latitude: 52.239444,
          longitude: 21.045556
        }
       });
    } else {
      loading.dismiss()
      this.geolocation = {
        latitude: 52.239444,
        longitude: 21.045556
      }
    }
    this.distanceForm = new FormGroup({
      distance: new FormControl('', [Validators.required]),
      withReturn: new FormControl(false)
    })
  }

  setMode(mode) {
    this.mode = mode
  }

  placeSelected(e) {
    this.destination = e;
  }

  next() {
    const params = {
      'location[longitude_from]': this.geolocation.longitude,
      'location[latitude_from]': this.geolocation.latitude
    }
    const opts = {
      mode: this.mode
    }
    if (this.gender !== 'none') {
      params['userMetadata:sex'] = this.gender
    }
    if (this.mode === 'distance') {
      params['distance_from'] = this.distance.lower * 1000
      params['distance_to'] = this.distance.upper * 1000
      opts['withReturn'] = this.withReturn
    } else if (this.mode === 'destination') {
      if (!this.destination) {
        return alert('Please provide destination')
      }
      opts['destination'] = this.destination
    }
    this.navCtrl.push('SelectTypePage', { opts, params })
  }

}
