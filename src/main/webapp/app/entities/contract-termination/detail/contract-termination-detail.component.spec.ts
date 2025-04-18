import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContractTerminationDetailComponent } from './contract-termination-detail.component';

describe('ContractTermination Management Detail Component', () => {
  let comp: ContractTerminationDetailComponent;
  let fixture: ComponentFixture<ContractTerminationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractTerminationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./contract-termination-detail.component').then(m => m.ContractTerminationDetailComponent),
              resolve: { contractTermination: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContractTerminationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContractTerminationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contractTermination on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContractTerminationDetailComponent);

      // THEN
      expect(instance.contractTermination()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
