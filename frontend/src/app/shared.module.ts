import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatComponentsModule } from './mat-components.module';
import { JwtModule } from '@auth0/angular-jwt';



@NgModule({
  imports:[
    CommonModule,
    JwtModule
  ],
  exports:[
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatComponentsModule
  ]
})
export class SharedModule { }
