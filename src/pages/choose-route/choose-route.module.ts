import { ComponentsModule } from './../../components/components.module';
import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { ChooseRoutePage } from './choose-route';

@NgModule({
  declarations: [
    ChooseRoutePage,
  ],
  imports: [
    IonicPageModule.forChild(ChooseRoutePage),
    ComponentsModule
  ],
})
export class ChooseRoutePageModule {}
