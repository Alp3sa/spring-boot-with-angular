import { TestBed } from '@angular/core/testing';

import { DatabaseFieldService } from './database-field.service';

import { HttpClient,HttpHandler } from '@angular/common/http';

describe('DatabaseFieldService', () => {
  let service: DatabaseFieldService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClient,HttpHandler]
    });
    service = TestBed.inject(DatabaseFieldService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
