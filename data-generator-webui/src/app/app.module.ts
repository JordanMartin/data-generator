import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatGridListModule} from '@angular/material/grid-list';
import {GeneratorDocComponent} from './components/generator-doc/generator-doc.component';
import {MatListModule} from '@angular/material/list';
import {MatExpansionModule} from '@angular/material/expansion';
import {DefinitionEditorComponent} from './components/definition-editor/definition-editor.component';
import {GeneratePageComponent} from './pages/generate-page/generate-page.component';
import {FormsModule} from '@angular/forms';
import {DataPreviewComponent} from './components/data-preview/data-preview.component';
import {MatSelectModule} from '@angular/material/select';
import {MatOptionModule} from '@angular/material/core';
import {MatIconModule} from '@angular/material/icon';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatDialogModule} from "@angular/material/dialog";
import {DownloadForm} from "./components/download-form/download-form.component";
import {TemplateEditorComponent} from "./components/template-editor/template-editor.component";
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {OutputConfigComponent} from './components/output-config/output-config.component';
import {SimplifyJavaTypePipe} from "./components/generator-doc/simplify-java-type.pipe";
import {FavoritesComponent} from './components/favorites/favorites.component';
import {MatMenuModule} from '@angular/material/menu';
import {MatBadgeModule} from "@angular/material/badge";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatRadioModule} from '@angular/material/radio';
import {ManageFavoritesComponent} from './components/manage-favorites/manage-favorites.component';
import { DateAgoComponent } from './components/manage-favorites/date-ago.component';
import { CreateFavoriteComponent } from './components/favorites/create-favorite.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";

@NgModule({
  declarations: [
    AppComponent,
    GeneratorDocComponent,
    DefinitionEditorComponent,
    GeneratePageComponent,
    DataPreviewComponent,
    DownloadForm,
    TemplateEditorComponent,
    OutputConfigComponent,
    SimplifyJavaTypePipe,
    FavoritesComponent,
    ManageFavoritesComponent,
    DateAgoComponent,
    CreateFavoriteComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    MatToolbarModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatListModule,
    MatExpansionModule,
    MatSelectModule,
    MatOptionModule,
    MatIconModule,
    MatDialogModule,
    MatSlideToggleModule,
    MatButtonToggleModule,
    MatProgressBarModule,
    MatMenuModule,
    MatBadgeModule,
    MatTooltipModule,
    MatRadioModule,
    MatSnackBarModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
