import { Platform } from 'ionic-angular';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';


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

  fetchActivities(queryParams={}) {
    return new Promise((resolve, reject) => {
      const body = {
        ...queryParams,
        'location[longitude_from]': '20',
        'location[latitude_from]': '51',
        'location[longitude_to]': '21',
        'location[latitude_to]': '52',
        '_zoom': '1',
        '_segment': '1'
      }
      let params = new HttpParams()
      Object.keys(body).forEach((key) => {
        params = params.set(key, body[key])
      })
      this.http.get(`${this.rowerowaPolskaApi}public/api/activities?${params.toString()}`)
        .toPromise()
        .then((e) => {
          console.log(e)
          resolve(e)
        }, reject)
    })
  }

}
