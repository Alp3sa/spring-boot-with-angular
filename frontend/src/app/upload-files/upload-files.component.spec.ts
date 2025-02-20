import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { UploadFilesComponent } from './upload-files.component';

import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { AppRoutingModule } from '../app-routing.module';

describe('UploadFilesComponent', () => {
  let component: UploadFilesComponent;
  let fixture: ComponentFixture<UploadFilesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ UploadFilesComponent ],
      imports: [RouterTestingModule,HttpClientModule,AppRoutingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
