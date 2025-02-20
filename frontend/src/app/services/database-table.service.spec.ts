import { TestBed } from '@angular/core/testing';

import { DatabaseTableService } from './database-table.service';

import { HttpClient,HttpHandler } from '@angular/common/http';

describe('DatabaseTableService', () => {
  let service: DatabaseTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClient,HttpHandler]
    });
    service = TestBed.inject(DatabaseTableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
