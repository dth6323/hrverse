import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IContractTermination } from '../contract-termination.model';

@Component({
  standalone: true,
  selector: 'jhi-contract-termination-detail',
  templateUrl: './contract-termination-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ContractTerminationDetailComponent {
  contractTermination = input<IContractTermination | null>(null);

  previousState(): void {
    window.history.back();
  }
}
