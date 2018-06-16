import { HTTP } from '@ionic-native/http';
import { Platform } from 'ionic-angular';
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

  constructor(
    @Inject(Http) public http: Http, 
    private platform: Platform,
    private nativeHttp: HTTP
  ) {
    if (this.placeholder == null) {
      this.placeholder = "Search";
    }
    this.googleApi = this.platform.is('cordova') ? 'https://maps.googleapis.com/maps/api/place/' : '/google-place-api/'
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
      return this.nativeHttp.get(this.googleApi + "autocomplete/json?input="+input+"&key="+this.key+params, null, null)
    }
    return this.http.get(this.googleApi + "autocomplete/json?input="+input+"&key="+this.key+params)
      .map(res => res.json());
  }

  getLocals(ev: any) {
    let val = ev.target.value;
    if (val && val.trim().length > 3) {
      if (this.platform.is('cordova') && this.platform.is('ios')) {
        this.autocomplete(val).then(res => {
          console.log(res)
          this.locals = JSON.parse(res.data).predictions
        })
      } else {
        this.autocomplete(val).subscribe(res => {
          this.locals = res.predictions;
        });
      }
    } else {
      this.locals = [];
    }
  }

  detail(item) {
    this.callback.emit([item]);
    this.locals = [];
  }
}