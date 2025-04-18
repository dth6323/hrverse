import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { WageService } from '../service/wage.service';
import { IWage } from '../wage.model';
import { WageFormService } from './wage-form.service';

import { WageUpdateComponent } from './wage-update.component';

describe('Wage Management Update Component', () => {
  let comp: WageUpdateComponent;
  let fixture: ComponentFixture<WageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wageFormService: WageFormService;
  let wageService: WageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WageUpdateComponent],
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
      .overrideTemplate(WageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wageFormService = TestBed.inject(WageFormService);
    wageService = TestBed.inject(WageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const wage: IWage = { id: 456 };

      activatedRoute.data = of({ wage });
      comp.ngOnInit();

      expect(comp.wage).toEqual(wage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWage>>();
      const wage = { id: 123 };
      jest.spyOn(wageFormService, 'getWage').mockReturnValue(wage);
      jest.spyOn(wageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wage }));
      saveSubject.complete();

      // THEN
      expect(wageFormService.getWage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(wageService.update).toHaveBeenCalledWith(expect.objectContaining(wage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWage>>();
      const wage = { id: 123 };
      jest.spyOn(wageFormService, 'getWage').mockReturnValue({ id: null });
      jest.spyOn(wageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wage }));
      saveSubject.complete();

      // THEN
      expect(wageFormService.getWage).toHaveBeenCalled();
      expect(wageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWage>>();
      const wage = { id: 123 };
      jest.spyOn(wageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
