import { CustomFormPageModule } from './../pages/custom-form/custom-form.module';
import { ChooseRoutePageModule } from './../pages/choose-route/choose-route.module';
import { HomePageModule } from './../pages/home/home.module';
import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';
import { LaunchNavigator } from '@ionic-native/launch-navigator';
import { Geolocation } from '@ionic-native/geolocation';
import { HTTP } from '@ionic-native/http';

import { MyApp } from './app.component';

import { GooglePlacesAutocompleteComponentModule } from 'ionic2-google-places-autocomplete';
import { SelectTypePageModule } from '../pages/select-type/select-type.module';
import { ApiProvider } from '../providers/api/api';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    MyApp
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    GooglePlacesAutocompleteComponentModule,
    HomePageModule,
    ChooseRoutePageModule,
    CustomFormPageModule,
    SelectTypePageModule,
    HttpClientModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    ApiProvider,
    LaunchNavigator,
    Geolocation,
    HTTP
  ]
})
export class AppModule {}
