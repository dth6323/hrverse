import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContractTermination } from '../contract-termination.model';
import { ContractTerminationService } from '../service/contract-termination.service';

@Component({
  standalone: true,
  templateUrl: './contract-termination-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContractTerminationDeleteDialogComponent {
  contractTermination?: IContractTermination;

  protected contractTerminationService = inject(ContractTerminationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractTerminationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
