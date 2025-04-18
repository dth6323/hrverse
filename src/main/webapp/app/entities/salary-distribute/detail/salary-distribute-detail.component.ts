import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISalaryDistribute } from '../salary-distribute.model';

@Component({
  standalone: true,
  selector: 'jhi-salary-distribute-detail',
  templateUrl: './salary-distribute-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SalaryDistributeDetailComponent {
  salaryDistribute = input<ISalaryDistribute | null>(null);

  previousState(): void {
    window.history.back();
  }
}
