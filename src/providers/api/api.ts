import { Platform } from 'ionic-angular';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

function shuffle(a) {
  for (let i = a.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [a[i], a[j]] = [a[j], a[i]];
  }
  return a;
}

@Injectable()
export class ApiProvider {

  rowerowaPolskaApi: string;

  constructor(
    public http: HttpClient,
    private platform: Platform
  ) {
    this.rowerowaPolskaApi = this.platform.is('cordova') 
      ? "https://api.rowerowapolska.pl/" 
      : "/rowerowa-polska-api/"
  }

  fetchActivities(queryParams: any={}, opts: any={}) {
    console.log(queryParams, opts)
    return new Promise((resolve, reject) => {
      let body: any = {
        ...queryParams,
        // 'location[longitude_from]': '20',
        // 'location[latitude_from]': '51',
        // 'location[longitude_to]': '22',
        // 'location[latitude_to]': '53',
        '_zoom': '1',
        '_segment': '1'
      }
      const fromLon = parseFloat(body['location[longitude_from]'])
      const fromLat = parseFloat(body['location[latitude_from]'])
      body = {
        ...body,
        'location[longitude_from]': fromLon - .3,
        'location[latitude_from]': fromLat - .3,
        'location[longitude_to]':  fromLon + .3,
        'location[latitude_to]': fromLat + .3,
      }
      const promises = []
      for (let i = 1; i <= 100; i++) {
        let params = new HttpParams()
        Object.keys(body).forEach((key) => {
          params = params.set(key, body[key])
        })
        params = params.set('page', i.toString())
        promises.push(
          new Promise((resolve) => {
            this.http.get(`${this.rowerowaPolskaApi}public/api/activities?${params.toString()}`)
              .toPromise()
              .then(resolve, resolve)
          })
        )
      }
      let delta = .02
      if (opts.destination && opts.type === 'fastest') {
        delta = .015
      }
      Promise.all(promises).then((e) => {
        let out = []
        e = e.filter((item) => Array.isArray(item))
        e.forEach(item => out = [...out, ...item])
        const checkItem = (item) => {
          const last = item.path.coordinates[item.path.coordinates.length - 1]
          const first = item.path.coordinates[0]
          if (
            Math.abs(fromLon - first[0]) < delta &&
            Math.abs(fromLat - first[1]) < delta
          ) {
            item.path.coordinates = [...[[fromLon, fromLat]], ...item.path.coordinates]
            return true
          } else if (
            Math.abs(fromLon - last[0]) < delta &&
            Math.abs(fromLat - last[1]) < delta
          ) {
            item.path.coordinates.reverse()
            item.path.coordinates = [...[[fromLon, fromLat]], ...item.path.coordinates]
            return true
          }
          return false;
        }
        out = out.filter((item) => {
          const checked = checkItem(item);
          if (!checked) {
            return false
          }
          if (opts.withReturn) {
            const last = item.path.coordinates[item.path.coordinates.length - 1]
            if (
              Math.abs(fromLon - last[0]) < delta &&
              Math.abs(fromLat - last[1]) < delta
            ) {
              item.path.coordinates = [...item.path.coordinates, ...[[fromLon, fromLat]]]
              return true
            }
            return false;
          } else if (opts.destination) {
            const last = item.path.coordinates[item.path.coordinates.length - 1]
            if (
              Math.abs(opts.destination.lng - last[0]) < delta &&
              Math.abs(opts.destination.lat - last[1]) < delta
            ) {
              item.path.coordinates = [...[[fromLon, fromLat]], ...item.path.coordinates, ...[[opts.destination.lng, opts.destination.lat]]]
              return true
            }
            return false;
          } 
          return true
        })
        if (opts.type === 'random') {
          out = shuffle(out)
        } else if (opts.type === 'fastest') {
          out = out.sort((a, b) => {
            if (opts.destination) {
              if (a.duration < b.duration) {
                return -1
              } else if (a.duration > b.duration) {
                return 1
              }
            } else {
              if (a.averageSpeed < b.averageSpeed) {
                return 1
              } else if (a.averageSpeed > b.averageSpeed) {
                return -1
              }
            }
            return 0
          })
        }
        console.log(out.length)
        resolve(out)
      }, reject)
    })
  }

  checkFirst() {
    
  }

}
