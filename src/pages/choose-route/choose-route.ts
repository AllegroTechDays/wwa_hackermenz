import { LaunchNavigator } from '@ionic-native/launch-navigator';
import { ApiProvider } from './../../providers/api/api';
import { Component, OnInit } from '@angular/core';
import { IonicPage, NavController, NavParams } from 'ionic-angular';
import { SimplifyLL } from 'simplify-ts'


@IonicPage()
@Component({
  selector: 'page-choose-route',
  templateUrl: 'choose-route.html',
})
export class ChooseRoutePage implements OnInit {

  activities: any[]
  currentActivityIdx: number
  currentPath: any[]
  simpifiedPath: any[]

  distance
  time
  avgSpeed

  constructor(
    public navCtrl: NavController, 
    public navParams: NavParams,
    private api: ApiProvider,
    private launchNavigator: LaunchNavigator
  ) { }

  ngOnInit() {
    this.api.fetchActivities().then((activities: any[]) => {
      this.activities = activities
      if (this.activities.length > 0) {
        this.currentActivityIdx = 0
        this.selectCurrentPath()
      }
    }, (err) => {
      console.log("ERROR", err)
    })
  }

  selectCurrentPath() {
    const points = []
    const currentActivity = this.activities[this.currentActivityIdx]
    
    this.distance = (currentActivity.distance / 1000.0).toFixed(2)
    this.avgSpeed = currentActivity.averageSpeed.toFixed(2)
    this.time = (currentActivity.duration / 60.0).toFixed(0)

    this.currentPath = currentActivity.path.coordinates.map(item => {
      points.push({
        latitude: item[1], 
        longitude: item[0]
      })
      return {
        lat: item[1],
        lng: item[0]
      }
    })
    const tolerance: number = .002;
    const highQuality: boolean = true;
    this.simpifiedPath = SimplifyLL(points, tolerance, highQuality).map(item => {
      return {
        lat: item.latitude,
        lng: item.longitude
      }
    });
  }

  next() {
    if (this.currentActivityIdx >= this.activities.length - 1) {
      this.currentActivityIdx = 0
    } else {
      this.currentActivityIdx += 1
    }
    this.selectCurrentPath()
  }

  launchNavigation() {
    let to = ''
    this.simpifiedPath.forEach((item) => {
      if (to) {
        to += '+to:'
      }
      to += `${item.lat},${item.lng}`
    })
    this.launchNavigator.navigate(to, {
      
    }).then(e => {
      console.log(e)
    }, err => {
      console.log("ERROR", err)
    })
  }

}
