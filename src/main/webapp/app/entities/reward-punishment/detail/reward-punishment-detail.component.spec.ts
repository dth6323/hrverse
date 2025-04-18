import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RewardPunishmentDetailComponent } from './reward-punishment-detail.component';

describe('RewardPunishment Management Detail Component', () => {
  let comp: RewardPunishmentDetailComponent;
  let fixture: ComponentFixture<RewardPunishmentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RewardPunishmentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./reward-punishment-detail.component').then(m => m.RewardPunishmentDetailComponent),
              resolve: { rewardPunishment: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RewardPunishmentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RewardPunishmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rewardPunishment on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RewardPunishmentDetailComponent);

      // THEN
      expect(instance.rewardPunishment()).toEqual(expect.objectContaining({ id: 123 }));
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
