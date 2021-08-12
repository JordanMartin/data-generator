import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GeneratePageComponent } from './pages/generate-page/generate-page.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/generate',
    pathMatch: 'full'
  },
  { path: 'generate', component: GeneratePageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
