import { IonicModule } from 'ionic-angular';
import { NgModule } from '@angular/core';
import { RoutePreviewComponent } from './route-preview/route-preview';
import { PlacesAutocompleteComponent } from './places-autocomplete/places-autocomplete';
@NgModule({
	declarations: [RoutePreviewComponent,
    PlacesAutocompleteComponent],
	imports: [IonicModule],
	exports: [RoutePreviewComponent,
    PlacesAutocompleteComponent]
})
export class ComponentsModule {}
