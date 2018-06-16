import { HTTP } from '@ionic-native/http';
import { Platform, AlertController } from 'ionic-angular';
import { Component, Input, Output, EventEmitter, Inject } from '@angular/core';
import { Http } from '@angular/http';

import 'rxjs/add/operator/map';


@Component({
  selector: 'places-autocomplete',
  templateUrl: 'places-autocomplete.html'
})
export class PlacesAutocompleteComponent {

  @Output("callback") callback : EventEmitter<any> = new EventEmitter();

  @Input("placeholder") placeholder : string;

  @Input("types") types : string;

  @Input("type") type : string;

  @Input("key") key : string;

  @Input("offset") offset : string;

  @Input("location") location : string;

  @Input("radius") radius : string;

  @Input("language") language : string;

  @Input("components") components : string;

  @Input("strictbounds") strictbounds : string;

  locals: any[];

  googleApi: string;
  query: string = ''

  constructor(
    @Inject(Http) public http: Http, 
    private platform: Platform,
    private nativeHttp: HTTP,
    private alertCtrl: AlertController
  ) {
    if (this.placeholder == null) {
      this.placeholder = "Search";
    }
    this.googleApi = this.platform.is('cordova') ? 'https://maps.googleapis.com/maps/api/' : '/google-api/'
  }

  public autocomplete(input: string): any {
    let typesParam: string = this.types != null ? ("&types=" + this.types) : "";
    let typeParam: string = this.type != null ? ("&type=" + this.type) : "";
    let offsetParam: string = this.offset != null ? ("&offset=" + this.offset) : "";
    let locationParam: string = this.location != null ? ("&location=" + this.location) : "";
    let radiusParam: string = this.radius != null ? ("&radius=" + this.radius) : "";
    let languageParam: string = this.language != null ? ("&language=" + this.language) : "";
    let componentsParam: string = this.components != null ? ("&components=" + this.components) : "";
    let strictboundsParam: string = this.strictbounds != null ? ("&strictbounds=" + this.strictbounds) : "";
    let params = typesParam + typeParam + offsetParam + locationParam + radiusParam + languageParam + componentsParam + strictboundsParam;
    if (this.platform.is('cordova') && this.platform.is('ios')) {
      return this.nativeHttp.get(this.googleApi + "place/autocomplete/json?input="+input+"&key="+this.key+params, null, null)
    }
    return this.http.get(this.googleApi + "place/autocomplete/json?input="+input+"&key="+this.key+params)
      .map(res => res.json());
  }

  getLocals(ev: any) {
    let val = ev.target.value;
    if (val && val.trim().length > 3) {
      if (this.platform.is('cordova') && this.platform.is('ios')) {
        this.autocomplete(val).then(res => {
          this.locals = JSON.parse(res.data).predictions
        })
      } else {
        this.autocomplete(val).subscribe(res => {
          this.locals = res.predictions.filter((item) => item.place_id);
        });
      }
    } else {
      this.locals = [];
    }
  }

  detail(item) {
    const promises = []
    if (this.platform.is('cordova') && this.platform.is('ios')) {
      promises.push(this.nativeHttp.get(
        this.googleApi + "geocode/json?place_id="+item.place_id+"&key="+this.key, null, null
      ))
    } else {
      promises.push(
        this.http.get(this.googleApi + "geocode/json?place_id="+item.place_id+"&key="+this.key).toPromise()
      )
    }
    Promise.all(promises).then((e:any) => {
      let resp
      if (this.platform.is('cordova') && this.platform.is('ios')) {
        resp = JSON.parse(e[0].data)
      } else {
        resp = e[0].json()
      }
      this.query = item.description
      let location 
      try {
        location = resp.results[0].geometry.location
      } catch (error) {
        return this.alertCtrl.create({
          message: 'Oooops, can\'t find a location for this place',
          buttons: [{
            text: 'Ok'
          }]
        })
      }
      this.callback.emit([location]);
      this.locals = [];
    })
  }
}