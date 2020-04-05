import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule } from '@angular/material/dialog';

import './vendor';
import { DaarBibliSharedModule } from 'app/shared/shared.module';
import { DaarBibliCoreModule } from 'app/core/core.module';
import { DaarBibliAppRoutingModule } from './app-routing.module';
import { DaarBibliHomeModule } from './home/home.module';
import { DaarBibliEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { SuggestionComponent } from 'app/home/suggestion/suggestion.component';
import { MatIconModule } from '@angular/material/icon';
@NgModule({
  imports: [
    BrowserModule,
    DaarBibliSharedModule,
    DaarBibliCoreModule,
    DaarBibliHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    DaarBibliEntityModule,
    DaarBibliAppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatDialogModule,
    MatIconModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent, SuggestionComponent],
  entryComponents: [SuggestionComponent],
  providers: [{ provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } }],
  bootstrap: [MainComponent]
})
export class DaarBibliAppModule {}
