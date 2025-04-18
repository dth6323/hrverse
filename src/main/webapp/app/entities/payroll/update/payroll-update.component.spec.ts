import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IWage } from 'app/entities/wage/wage.model';
import { WageService } from 'app/entities/wage/service/wage.service';
import { ISalaryDistribute } from 'app/entities/salary-distribute/salary-distribute.model';
import { SalaryDistributeService } from 'app/entities/salary-distribute/service/salary-distribute.service';
import { IPayroll } from '../payroll.model';
import { PayrollService } from '../service/payroll.service';
import { PayrollFormService } from './payroll-form.service';

import { PayrollUpdateComponent } from './payroll-update.component';

describe('Payroll Management Update Component', () => {
  let comp: PayrollUpdateComponent;
  let fixture: ComponentFixture<PayrollUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let payrollFormService: PayrollFormService;
  let payrollService: PayrollService;
  let employeeService: EmployeeService;
  let wageService: WageService;
  let salaryDistributeService: SalaryDistributeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PayrollUpdateComponent],
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
      .overrideTemplate(PayrollUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PayrollUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    payrollFormService = TestBed.inject(PayrollFormService);
    payrollService = TestBed.inject(PayrollService);
    employeeService = TestBed.inject(EmployeeService);
    wageService = TestBed.inject(WageService);
    salaryDistributeService = TestBed.inject(SalaryDistributeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const payroll: IPayroll = { id: 456 };
      const employee: IEmployee = { id: 15900 };
      payroll.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 6499 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payroll });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Wage query and add missing value', () => {
      const payroll: IPayroll = { id: 456 };
      const wage: IWage = { id: 10137 };
      payroll.wage = wage;

      const wageCollection: IWage[] = [{ id: 32598 }];
      jest.spyOn(wageService, 'query').mockReturnValue(of(new HttpResponse({ body: wageCollection })));
      const additionalWages = [wage];
      const expectedCollection: IWage[] = [...additionalWages, ...wageCollection];
      jest.spyOn(wageService, 'addWageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payroll });
      comp.ngOnInit();

      expect(wageService.query).toHaveBeenCalled();
      expect(wageService.addWageToCollectionIfMissing).toHaveBeenCalledWith(
        wageCollection,
        ...additionalWages.map(expect.objectContaining),
      );
      expect(comp.wagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SalaryDistribute query and add missing value', () => {
      const payroll: IPayroll = { id: 456 };
      const salaryDistribute: ISalaryDistribute = { id: 9713 };
      payroll.salaryDistribute = salaryDistribute;

      const salaryDistributeCollection: ISalaryDistribute[] = [{ id: 25918 }];
      jest.spyOn(salaryDistributeService, 'query').mockReturnValue(of(new HttpResponse({ body: salaryDistributeCollection })));
      const additionalSalaryDistributes = [salaryDistribute];
      const expectedCollection: ISalaryDistribute[] = [...additionalSalaryDistributes, ...salaryDistributeCollection];
      jest.spyOn(salaryDistributeService, 'addSalaryDistributeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ payroll });
      comp.ngOnInit();

      expect(salaryDistributeService.query).toHaveBeenCalled();
      expect(salaryDistributeService.addSalaryDistributeToCollectionIfMissing).toHaveBeenCalledWith(
        salaryDistributeCollection,
        ...additionalSalaryDistributes.map(expect.objectContaining),
      );
      expect(comp.salaryDistributesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const payroll: IPayroll = { id: 456 };
      const employee: IEmployee = { id: 30898 };
      payroll.employee = employee;
      const wage: IWage = { id: 2566 };
      payroll.wage = wage;
      const salaryDistribute: ISalaryDistribute = { id: 9729 };
      payroll.salaryDistribute = salaryDistribute;

      activatedRoute.data = of({ payroll });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.wagesSharedCollection).toContain(wage);
      expect(comp.salaryDistributesSharedCollection).toContain(salaryDistribute);
      expect(comp.payroll).toEqual(payroll);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayroll>>();
      const payroll = { id: 123 };
      jest.spyOn(payrollFormService, 'getPayroll').mockReturnValue(payroll);
      jest.spyOn(payrollService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payroll });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payroll }));
      saveSubject.complete();

      // THEN
      expect(payrollFormService.getPayroll).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(payrollService.update).toHaveBeenCalledWith(expect.objectContaining(payroll));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayroll>>();
      const payroll = { id: 123 };
      jest.spyOn(payrollFormService, 'getPayroll').mockReturnValue({ id: null });
      jest.spyOn(payrollService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payroll: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: payroll }));
      saveSubject.complete();

      // THEN
      expect(payrollFormService.getPayroll).toHaveBeenCalled();
      expect(payrollService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPayroll>>();
      const payroll = { id: 123 };
      jest.spyOn(payrollService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ payroll });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(payrollService.update).toHaveBeenCalled();
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

    describe('compareWage', () => {
      it('Should forward to wageService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(wageService, 'compareWage');
        comp.compareWage(entity, entity2);
        expect(wageService.compareWage).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSalaryDistribute', () => {
      it('Should forward to salaryDistributeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(salaryDistributeService, 'compareSalaryDistribute');
        comp.compareSalaryDistribute(entity, entity2);
        expect(salaryDistributeService.compareSalaryDistribute).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
