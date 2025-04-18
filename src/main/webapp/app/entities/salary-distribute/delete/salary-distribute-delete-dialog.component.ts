import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISalaryDistribute } from '../salary-distribute.model';
import { SalaryDistributeService } from '../service/salary-distribute.service';

@Component({
  standalone: true,
  templateUrl: './salary-distribute-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SalaryDistributeDeleteDialogComponent {
  salaryDistribute?: ISalaryDistribute;

  protected salaryDistributeService = inject(SalaryDistributeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salaryDistributeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
