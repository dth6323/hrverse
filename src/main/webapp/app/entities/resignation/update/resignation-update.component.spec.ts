import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ResignationService } from '../service/resignation.service';
import { IResignation } from '../resignation.model';
import { ResignationFormService } from './resignation-form.service';

import { ResignationUpdateComponent } from './resignation-update.component';

describe('Resignation Management Update Component', () => {
  let comp: ResignationUpdateComponent;
  let fixture: ComponentFixture<ResignationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let resignationFormService: ResignationFormService;
  let resignationService: ResignationService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ResignationUpdateComponent],
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
      .overrideTemplate(ResignationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ResignationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    resignationFormService = TestBed.inject(ResignationFormService);
    resignationService = TestBed.inject(ResignationService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const resignation: IResignation = { id: 456 };
      const employee: IEmployee = { id: 16585 };
      resignation.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 13405 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ resignation });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const resignation: IResignation = { id: 456 };
      const employee: IEmployee = { id: 5164 };
      resignation.employee = employee;

      activatedRoute.data = of({ resignation });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.resignation).toEqual(resignation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResignation>>();
      const resignation = { id: 123 };
      jest.spyOn(resignationFormService, 'getResignation').mockReturnValue(resignation);
      jest.spyOn(resignationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resignation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resignation }));
      saveSubject.complete();

      // THEN
      expect(resignationFormService.getResignation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(resignationService.update).toHaveBeenCalledWith(expect.objectContaining(resignation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResignation>>();
      const resignation = { id: 123 };
      jest.spyOn(resignationFormService, 'getResignation').mockReturnValue({ id: null });
      jest.spyOn(resignationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resignation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: resignation }));
      saveSubject.complete();

      // THEN
      expect(resignationFormService.getResignation).toHaveBeenCalled();
      expect(resignationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IResignation>>();
      const resignation = { id: 123 };
      jest.spyOn(resignationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ resignation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(resignationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
