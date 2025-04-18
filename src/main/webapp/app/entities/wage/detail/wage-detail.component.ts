import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IWage } from '../wage.model';

@Component({
  standalone: true,
  selector: 'jhi-wage-detail',
  templateUrl: './wage-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WageDetailComponent {
  wage = input<IWage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
