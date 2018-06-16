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
    const opts = {
      ...this.navParams.get('opts') || {}
    }
    const params = {
      ...this.navParams.get('params') || {}
    }
    opts.type = type
    if (type === 'custom') {
      return this.navCtrl.push('CustomFormPage', { opts, params })
    }
    this.navCtrl.push('ChooseRoutePage', { opts, params })
  }

}
