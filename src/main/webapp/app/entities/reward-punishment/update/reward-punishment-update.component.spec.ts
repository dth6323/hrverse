import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { RewardPunishmentService } from '../service/reward-punishment.service';
import { IRewardPunishment } from '../reward-punishment.model';
import { RewardPunishmentFormService } from './reward-punishment-form.service';

import { RewardPunishmentUpdateComponent } from './reward-punishment-update.component';

describe('RewardPunishment Management Update Component', () => {
  let comp: RewardPunishmentUpdateComponent;
  let fixture: ComponentFixture<RewardPunishmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rewardPunishmentFormService: RewardPunishmentFormService;
  let rewardPunishmentService: RewardPunishmentService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RewardPunishmentUpdateComponent],
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
      .overrideTemplate(RewardPunishmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RewardPunishmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rewardPunishmentFormService = TestBed.inject(RewardPunishmentFormService);
    rewardPunishmentService = TestBed.inject(RewardPunishmentService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const rewardPunishment: IRewardPunishment = { id: 456 };
      const employee: IEmployee = { id: 27707 };
      rewardPunishment.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 31718 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rewardPunishment });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rewardPunishment: IRewardPunishment = { id: 456 };
      const employee: IEmployee = { id: 1691 };
      rewardPunishment.employee = employee;

      activatedRoute.data = of({ rewardPunishment });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.rewardPunishment).toEqual(rewardPunishment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRewardPunishment>>();
      const rewardPunishment = { id: 123 };
      jest.spyOn(rewardPunishmentFormService, 'getRewardPunishment').mockReturnValue(rewardPunishment);
      jest.spyOn(rewardPunishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rewardPunishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rewardPunishment }));
      saveSubject.complete();

      // THEN
      expect(rewardPunishmentFormService.getRewardPunishment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(rewardPunishmentService.update).toHaveBeenCalledWith(expect.objectContaining(rewardPunishment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRewardPunishment>>();
      const rewardPunishment = { id: 123 };
      jest.spyOn(rewardPunishmentFormService, 'getRewardPunishment').mockReturnValue({ id: null });
      jest.spyOn(rewardPunishmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rewardPunishment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rewardPunishment }));
      saveSubject.complete();

      // THEN
      expect(rewardPunishmentFormService.getRewardPunishment).toHaveBeenCalled();
      expect(rewardPunishmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRewardPunishment>>();
      const rewardPunishment = { id: 123 };
      jest.spyOn(rewardPunishmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rewardPunishment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rewardPunishmentService.update).toHaveBeenCalled();
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
