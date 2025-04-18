import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPayroll } from '../payroll.model';
import { PayrollService } from '../service/payroll.service';

@Component({
  standalone: true,
  templateUrl: './payroll-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PayrollDeleteDialogComponent {
  payroll?: IPayroll;

  protected payrollService = inject(PayrollService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.payrollService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
