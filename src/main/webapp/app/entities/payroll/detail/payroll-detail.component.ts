import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPayroll } from '../payroll.model';

@Component({
  standalone: true,
  selector: 'jhi-payroll-detail',
  templateUrl: './payroll-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PayrollDetailComponent {
  payroll = input<IPayroll | null>(null);

  previousState(): void {
    window.history.back();
  }
}
