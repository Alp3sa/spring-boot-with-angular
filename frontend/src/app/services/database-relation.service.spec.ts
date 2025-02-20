import { TestBed } from '@angular/core/testing';

import { DatabaseRelationService } from './database-relation.service';

import { HttpClient,HttpHandler } from '@angular/common/http';

describe('DatabaseRelationService', () => {
  let service: DatabaseRelationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClient,HttpHandler]
    });
    service = TestBed.inject(DatabaseRelationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
