import { Component, inject, input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISalaryDistribute } from '../salary-distribute.model';
import { SalaryDistributeService } from '../service/salary-distribute.service';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';
import { SortByDirective, SortDirective } from '../../../shared/sort';
import { SalaryDistribute } from '../employee-detail.model';
import { PayrollService } from '../../payroll/service/payroll.service';
import { NewPayroll } from '../../payroll/payroll.model';
import { AlertService } from 'app/core/util/alert.service';
@Component({
  standalone: true,
  selector: 'jhi-salary-distribute-detail',
  templateUrl: './salary-distribute-detail.component.html',
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    HasAnyAuthorityDirective,
    SortByDirective,
    SortDirective,
  ],
})
export class SalaryDistributeDetailComponent implements OnInit {
  salaryDistribute = input<ISalaryDistribute | null>(null);
  id: string | null = null;
  protected payroll: NewPayroll | null = null;
  protected route: ActivatedRoute = inject(ActivatedRoute);
  protected employeeDetail?: SalaryDistribute[] = [];
  protected salaryDistributeService = inject(SalaryDistributeService);
  protected payrollService = inject(PayrollService);
  protected al = inject(AlertService);
  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id');
    this.showemployee();
  }
  exportReport(): void {
    if (this.id) {
      this.salaryDistributeService.exportDetails(this.id).subscribe({
        next(response) {
          const blob = new Blob([response.body!], { type: 'application/pdf' });
          const contentDisposition = response.headers.get('content-disposition');
          let fileName = 'salary-report.pdf'; // Tên mặc định

          if (contentDisposition) {
            // Xử lý Content-Disposition: filename="..." hoặc filename=...
            const fileNameMatch = contentDisposition.match(/filename(?:="|=(.*?)(?:"|$))/i);
            if (fileNameMatch?.[1]) {
              fileName = fileNameMatch[1];
            } else {
              // Trường hợp filename không có dấu ngoặc kép
              const simpleMatch = contentDisposition.match(/filename=([^;]+)/i);
              if (simpleMatch?.[1]) {
                fileName = simpleMatch[1].trim();
              }
            }
          }

          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = fileName;
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error(err) {
          console.error('Lỗi khi xuất báo cáo:', err);
        },
      });
    }
  }
  caculate(): void {
    const value: ISalaryDistribute | null = this.salaryDistribute();
    if (value?.id && value.startDate && value.endDate) {
      this.salaryDistributeService.caculate(value.startDate, value.endDate, value.id).subscribe({
        next: res => {
          this.showemployee();
          this.al.addAlert({ type: 'success', message: 'Tính lương thành công' });
        },
        error: err => {
          this.al.addAlert({ type: 'danger', message: 'Có lỗi khi tính lương!' });
        },
      });
    }
  }
  showemployee(): any {
    if (this.id != null)
      return this.salaryDistributeService.showemployee(this.id).subscribe({
        next: data => {
          this.employeeDetail = data;
        },
      });
  }

  previousState(): void {
    window.history.back();
  }
}
