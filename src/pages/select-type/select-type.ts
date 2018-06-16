import { Component } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';

@IonicPage()
@Component({
  selector: 'page-select-type',
  templateUrl: 'select-type.html',
})
export class SelectTypePage {

  types: any[] = [{
    name: 'Fastest',
    value: 'fastest',
    color: '#F5A623'
  }, {
    name: 'Custom',
    value: 'custom',
    color: '#D0021B'
  }, {
    name: 'Random',
    value: 'random',
    color: '#B8E986'
  }]

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams
  ) { }

  selectType(type) {
    this.navCtrl.push('ChooseRoutePage', { type })
  }

}
