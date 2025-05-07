import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IWage } from 'app/entities/wage/wage.model';
import { WageService } from 'app/entities/wage/service/wage.service';
import { IContractType } from 'app/entities/contract-type/contract-type.model';
import { ContractTypeService } from 'app/entities/contract-type/service/contract-type.service';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ContractFormService } from './contract-form.service';

import { ContractUpdateComponent } from './contract-update.component';

describe('Contract Management Update Component', () => {
  let comp: ContractUpdateComponent;
  let fixture: ComponentFixture<ContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractFormService: ContractFormService;
  let contractService: ContractService;
  let employeeService: EmployeeService;
  let wageService: WageService;
  let contractTypeService: ContractTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ContractUpdateComponent],
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
      .overrideTemplate(ContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractFormService = TestBed.inject(ContractFormService);
    contractService = TestBed.inject(ContractService);
    employeeService = TestBed.inject(EmployeeService);
    wageService = TestBed.inject(WageService);
    contractTypeService = TestBed.inject(ContractTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const employee: IEmployee = { id: 19147 };
      contract.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 12791 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Wage query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const wage: IWage = { id: 10137 };
      contract.wage = wage;

      const wageCollection: IWage[] = [{ id: 32598 }];
      jest.spyOn(wageService, 'query').mockReturnValue(of(new HttpResponse({ body: wageCollection })));
      const additionalWages = [wage];
      const expectedCollection: IWage[] = [...additionalWages, ...wageCollection];
      jest.spyOn(wageService, 'addWageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(wageService.query).toHaveBeenCalled();
      expect(wageService.addWageToCollectionIfMissing).toHaveBeenCalledWith(
        wageCollection,
        ...additionalWages.map(expect.objectContaining),
      );
      expect(comp.wagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ContractType query and add missing value', () => {
      const contract: IContract = { id: 456 };
      const contractType: IContractType = { id: 4534 };
      contract.contractType = contractType;

      const contractTypeCollection: IContractType[] = [{ id: 11709 }];
      jest.spyOn(contractTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: contractTypeCollection })));
      const additionalContractTypes = [contractType];
      const expectedCollection: IContractType[] = [...additionalContractTypes, ...contractTypeCollection];
      jest.spyOn(contractTypeService, 'addContractTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(contractTypeService.query).toHaveBeenCalled();
      expect(contractTypeService.addContractTypeToCollectionIfMissing).toHaveBeenCalledWith(
        contractTypeCollection,
        ...additionalContractTypes.map(expect.objectContaining),
      );
      expect(comp.contractTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contract: IContract = { id: 456 };
      const employee: IEmployee = { id: 31803 };
      contract.employee = employee;
      const wage: IWage = { id: 2566 };
      contract.wage = wage;
      const contractType: IContractType = { id: 4887 };
      contract.contractType = contractType;

      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.wagesSharedCollection).toContain(wage);
      expect(comp.contractTypesSharedCollection).toContain(contractType);
      expect(comp.contract).toEqual(contract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue(contract);
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractService.update).toHaveBeenCalledWith(expect.objectContaining(contract));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractFormService, 'getContract').mockReturnValue({ id: null });
      jest.spyOn(contractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contract }));
      saveSubject.complete();

      // THEN
      expect(contractFormService.getContract).toHaveBeenCalled();
      expect(contractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContract>>();
      const contract = { id: 123 };
      jest.spyOn(contractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractService.update).toHaveBeenCalled();
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

    describe('compareContractType', () => {
      it('Should forward to contractTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractTypeService, 'compareContractType');
        comp.compareContractType(entity, entity2);
        expect(contractTypeService.compareContractType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
