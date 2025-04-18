import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRewardPunishment } from '../reward-punishment.model';
import { RewardPunishmentService } from '../service/reward-punishment.service';

@Component({
  standalone: true,
  templateUrl: './reward-punishment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RewardPunishmentDeleteDialogComponent {
  rewardPunishment?: IRewardPunishment;

  protected rewardPunishmentService = inject(RewardPunishmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rewardPunishmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
