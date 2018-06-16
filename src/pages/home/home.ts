import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  mode:'distance'|'destination' = 'distance'

  constructor(public navCtrl: NavController) {

  }

  setMode(mode) {
    this.mode = mode
  }

  placeSelected(e) {
    console.log(e)
  }

  next() {
    this.navCtrl.push('SelectTypePage')
  }

}
