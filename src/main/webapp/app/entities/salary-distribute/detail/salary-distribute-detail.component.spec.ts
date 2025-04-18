import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SalaryDistributeDetailComponent } from './salary-distribute-detail.component';

describe('SalaryDistribute Management Detail Component', () => {
  let comp: SalaryDistributeDetailComponent;
  let fixture: ComponentFixture<SalaryDistributeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalaryDistributeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./salary-distribute-detail.component').then(m => m.SalaryDistributeDetailComponent),
              resolve: { salaryDistribute: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SalaryDistributeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalaryDistributeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load salaryDistribute on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SalaryDistributeDetailComponent);

      // THEN
      expect(instance.salaryDistribute()).toEqual(expect.objectContaining({ id: 123 }));
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
