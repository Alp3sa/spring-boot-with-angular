import { TestBed } from '@angular/core/testing';

import { DatabaseService } from './database.service';

import { HttpClient,HttpHandler } from '@angular/common/http';

describe('DatabaseService', () => {
  let service: DatabaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClient,HttpHandler]
    });
    service = TestBed.inject(DatabaseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
