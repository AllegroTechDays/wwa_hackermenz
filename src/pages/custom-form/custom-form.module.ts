import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { CustomFormPage } from './custom-form';

@NgModule({
  declarations: [
    CustomFormPage,
  ],
  imports: [
    IonicPageModule.forChild(CustomFormPage),
  ],
})
export class CustomFormPageModule {}
