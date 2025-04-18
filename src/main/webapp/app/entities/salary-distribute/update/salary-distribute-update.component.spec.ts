import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SalaryDistributeService } from '../service/salary-distribute.service';
import { ISalaryDistribute } from '../salary-distribute.model';
import { SalaryDistributeFormService } from './salary-distribute-form.service';

import { SalaryDistributeUpdateComponent } from './salary-distribute-update.component';

describe('SalaryDistribute Management Update Component', () => {
  let comp: SalaryDistributeUpdateComponent;
  let fixture: ComponentFixture<SalaryDistributeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salaryDistributeFormService: SalaryDistributeFormService;
  let salaryDistributeService: SalaryDistributeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SalaryDistributeUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SalaryDistributeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalaryDistributeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salaryDistributeFormService = TestBed.inject(SalaryDistributeFormService);
    salaryDistributeService = TestBed.inject(SalaryDistributeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const salaryDistribute: ISalaryDistribute = { id: 456 };

      activatedRoute.data = of({ salaryDistribute });
      comp.ngOnInit();

      expect(comp.salaryDistribute).toEqual(salaryDistribute);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryDistribute>>();
      const salaryDistribute = { id: 123 };
      jest.spyOn(salaryDistributeFormService, 'getSalaryDistribute').mockReturnValue(salaryDistribute);
      jest.spyOn(salaryDistributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryDistribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryDistribute }));
      saveSubject.complete();

      // THEN
      expect(salaryDistributeFormService.getSalaryDistribute).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(salaryDistributeService.update).toHaveBeenCalledWith(expect.objectContaining(salaryDistribute));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryDistribute>>();
      const salaryDistribute = { id: 123 };
      jest.spyOn(salaryDistributeFormService, 'getSalaryDistribute').mockReturnValue({ id: null });
      jest.spyOn(salaryDistributeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryDistribute: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salaryDistribute }));
      saveSubject.complete();

      // THEN
      expect(salaryDistributeFormService.getSalaryDistribute).toHaveBeenCalled();
      expect(salaryDistributeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISalaryDistribute>>();
      const salaryDistribute = { id: 123 };
      jest.spyOn(salaryDistributeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salaryDistribute });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salaryDistributeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
