import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IResignation } from '../resignation.model';

@Component({
  standalone: true,
  selector: 'jhi-resignation-detail',
  templateUrl: './resignation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ResignationDetailComponent {
  resignation = input<IResignation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
