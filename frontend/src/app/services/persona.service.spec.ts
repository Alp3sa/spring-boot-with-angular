import { TestBed } from '@angular/core/testing';

import { PersonaService } from './persona.service';

import { HttpClient,HttpHandler } from '@angular/common/http';

describe('PersonaService', () => {
  let service: PersonaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClient,HttpHandler]
    });
    service = TestBed.inject(PersonaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
