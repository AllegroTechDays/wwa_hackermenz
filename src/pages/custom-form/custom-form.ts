import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';


@IonicPage()
@Component({
  selector: 'page-custom-form',
  templateUrl: 'custom-form.html',
})
export class CustomFormPage {
  time: any = {
    upper:300,
    lower:5
  }
  speed: any = {
    upper:40,
    lower:5
  }
  bikeType = 'none'

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams
  ) { }

  next() {
    const params: any = {
      ...this.navParams.get('params') || {},
      averageSpeed_from: this.speed.lower,
      averageSpeed_to: this.speed.upper,
      duration_from: this.time.lower * 60,
      duration_to: this.time.upper * 60
    }
    if (this.bikeType !== 'none') {
      params.bikeType = this.bikeType
    }
    const opts = {
      ...this.navParams.get('opts') || {}
    }
    this.navCtrl.push('ChooseRoutePage', { params, opts })
  }

}
