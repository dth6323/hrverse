import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { ContractTerminationService } from '../service/contract-termination.service';
import { IContractTermination } from '../contract-termination.model';
import { ContractTerminationFormService } from './contract-termination-form.service';

import { ContractTerminationUpdateComponent } from './contract-termination-update.component';

describe('ContractTermination Management Update Component', () => {
  let comp: ContractTerminationUpdateComponent;
  let fixture: ComponentFixture<ContractTerminationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractTerminationFormService: ContractTerminationFormService;
  let contractTerminationService: ContractTerminationService;
  let contractService: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ContractTerminationUpdateComponent],
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
      .overrideTemplate(ContractTerminationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractTerminationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractTerminationFormService = TestBed.inject(ContractTerminationFormService);
    contractTerminationService = TestBed.inject(ContractTerminationService);
    contractService = TestBed.inject(ContractService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Contract query and add missing value', () => {
      const contractTermination: IContractTermination = { id: 456 };
      const contract: IContract = { id: 29419 };
      contractTermination.contract = contract;

      const contractCollection: IContract[] = [{ id: 20613 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const additionalContracts = [contract];
      const expectedCollection: IContract[] = [...additionalContracts, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contractTermination });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(
        contractCollection,
        ...additionalContracts.map(expect.objectContaining),
      );
      expect(comp.contractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contractTermination: IContractTermination = { id: 456 };
      const contract: IContract = { id: 1353 };
      contractTermination.contract = contract;

      activatedRoute.data = of({ contractTermination });
      comp.ngOnInit();

      expect(comp.contractsSharedCollection).toContain(contract);
      expect(comp.contractTermination).toEqual(contractTermination);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractTermination>>();
      const contractTermination = { id: 123 };
      jest.spyOn(contractTerminationFormService, 'getContractTermination').mockReturnValue(contractTermination);
      jest.spyOn(contractTerminationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractTermination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractTermination }));
      saveSubject.complete();

      // THEN
      expect(contractTerminationFormService.getContractTermination).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractTerminationService.update).toHaveBeenCalledWith(expect.objectContaining(contractTermination));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractTermination>>();
      const contractTermination = { id: 123 };
      jest.spyOn(contractTerminationFormService, 'getContractTermination').mockReturnValue({ id: null });
      jest.spyOn(contractTerminationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractTermination: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractTermination }));
      saveSubject.complete();

      // THEN
      expect(contractTerminationFormService.getContractTermination).toHaveBeenCalled();
      expect(contractTerminationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractTermination>>();
      const contractTermination = { id: 123 };
      jest.spyOn(contractTerminationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractTermination });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractTerminationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContract', () => {
      it('Should forward to contractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(contractService, 'compareContract');
        comp.compareContract(entity, entity2);
        expect(contractService.compareContract).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
