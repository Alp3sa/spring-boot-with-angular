import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { GetFilesComponent } from './get-files.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { AppRoutingModule } from '../app-routing.module';

describe('GetFilesComponent', () => {
  let component: GetFilesComponent;
  let fixture: ComponentFixture<GetFilesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule,RouterTestingModule,AppRoutingModule],
      declarations: [ GetFilesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
