import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CreateFileComponent } from './create-file.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { AppRoutingModule } from '../app-routing.module';

describe('CreateFileComponent', () => {
  let component: CreateFileComponent;
  let fixture: ComponentFixture<CreateFileComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateFileComponent ],
      imports: [HttpClientModule,RouterTestingModule,AppRoutingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
