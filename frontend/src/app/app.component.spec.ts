import { TestBed, waitForAsync, ComponentFixture, fakeAsync, tick } from '@angular/core/testing';
import {Observable, of} from 'rxjs';
import {delay} from 'rxjs/operators';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { UserService } from "./services/user.service";

describe('AppComponent', () => {
  let app : AppComponent;
  let fixture : ComponentFixture<AppComponent>
  let userService : UserService;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,HttpClientModule,AppRoutingModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  }));

  beforeEach(
    () => {
      userService = TestBed.inject(UserService);
      fixture = TestBed.createComponent(AppComponent);
      app = fixture.componentInstance;
      fixture.detectChanges();
    }
  );

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it(`should have as title 'qgen'`, () => {
    expect(app.title).toEqual('qgen');
  });

  it('should render title', () => {
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('h2').textContent).toContain('qgen');
  });

  it('User is authenticated', () => {
    userService.cookieService.set('username','admin');
  });

  it('User is not authenticated', () => {
    userService.cookieService.set('username','');
  });
  
  it('The user has visited any page before', () => {
    userService.cookieService.set('lastPage','login');
  });

  it('The user has not visited any page before', () => {
    userService.cookieService.set('lastPage','');
  });

  it('User tries to logout', () => {
    app.logout();
    expect(userService.authenticated).toBe(false);
  });

  it('User logout successfully', () => {
    userService.cookieService.set('username','admin');
    userService.logout(userService.cookieService.get('username')).subscribe(
      data => {
        console.log("-Done.");
      },
      err => {
        console.log("-Error: "+err.error.message);
      }
    );
  });

  it('User logout successfully 2', fakeAsync(() => {
    userService.cookieService.set('username','admin');
    const obj = new Observable();
    /*const fakedFetchedList = [
      new QuoteModel("I love unit testing", "Mon 4, 2018")
    ];*/
    //const quoteService = fixture.debugElement.injector.get(userService);
    let spy = spyOn(userService, "logout").and.returnValue(of({ a: 22222, b: 111111 }));
    (done: DoneFn) => {
      userService.logout(userService.cookieService.get('username')).subscribe(value => {
        expect(value).toBe('observable value');
        done();
    })};

    //spyOn(userService, 'logout').and.callFake( ()=> null);

    // Trigger ngOnInit()
    fixture.detectChanges();

    //expect(component.loading).toBeTruthy();
    //expect(component.users).toBeUndefined();
    //expect(userService.logout).toHaveBeenCalledWith();

    // Simulates the asynchronous passage of time
    tick(1);

    //expect(component.loading).toBeFalsy();
    //expect(component.users).toEqual([user]);
  }));
  
  it('User logout successfully 3', () => {
    userService.cookieService.set('username','admin');
    userService.logout(userService.cookieService.get('username'));
	
	spyOn(userService, 'logout').and.callFake(('') => {
		//return Observable.from('');
		return null;
	})
  });
});
