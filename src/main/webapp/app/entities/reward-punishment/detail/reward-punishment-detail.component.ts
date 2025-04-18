import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IRewardPunishment } from '../reward-punishment.model';

@Component({
  standalone: true,
  selector: 'jhi-reward-punishment-detail',
  templateUrl: './reward-punishment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RewardPunishmentDetailComponent {
  rewardPunishment = input<IRewardPunishment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
