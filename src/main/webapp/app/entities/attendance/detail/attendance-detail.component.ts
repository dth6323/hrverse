import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAttendance } from '../attendance.model';

@Component({
  standalone: true,
  selector: 'jhi-attendance-detail',
  templateUrl: './attendance-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AttendanceDetailComponent {
  attendance = input<IAttendance | null>(null);

  previousState(): void {
    window.history.back();
  }
}
