import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IResignation } from '../resignation.model';
import { ResignationService } from '../service/resignation.service';

@Component({
  standalone: true,
  templateUrl: './resignation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ResignationDeleteDialogComponent {
  resignation?: IResignation;

  protected resignationService = inject(ResignationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resignationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
