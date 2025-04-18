import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ContractTypeService } from '../service/contract-type.service';
import { IContractType } from '../contract-type.model';
import { ContractTypeFormService } from './contract-type-form.service';

import { ContractTypeUpdateComponent } from './contract-type-update.component';

describe('ContractType Management Update Component', () => {
  let comp: ContractTypeUpdateComponent;
  let fixture: ComponentFixture<ContractTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contractTypeFormService: ContractTypeFormService;
  let contractTypeService: ContractTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ContractTypeUpdateComponent],
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
      .overrideTemplate(ContractTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contractTypeFormService = TestBed.inject(ContractTypeFormService);
    contractTypeService = TestBed.inject(ContractTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const contractType: IContractType = { id: 456 };

      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      expect(comp.contractType).toEqual(contractType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractType>>();
      const contractType = { id: 123 };
      jest.spyOn(contractTypeFormService, 'getContractType').mockReturnValue(contractType);
      jest.spyOn(contractTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractType }));
      saveSubject.complete();

      // THEN
      expect(contractTypeFormService.getContractType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contractTypeService.update).toHaveBeenCalledWith(expect.objectContaining(contractType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractType>>();
      const contractType = { id: 123 };
      jest.spyOn(contractTypeFormService, 'getContractType').mockReturnValue({ id: null });
      jest.spyOn(contractTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contractType }));
      saveSubject.complete();

      // THEN
      expect(contractTypeFormService.getContractType).toHaveBeenCalled();
      expect(contractTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContractType>>();
      const contractType = { id: 123 };
      jest.spyOn(contractTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contractType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contractTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
