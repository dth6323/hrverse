import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWage } from '../wage.model';
import { WageService } from '../service/wage.service';

@Component({
  standalone: true,
  templateUrl: './wage-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WageDeleteDialogComponent {
  wage?: IWage;

  protected wageService = inject(WageService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
