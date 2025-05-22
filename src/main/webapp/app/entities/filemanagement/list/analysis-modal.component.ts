import { Component, inject, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeUpdateComponent } from '../../employee/update/employee-update.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Dayjs } from 'dayjs';
import dayjs from 'dayjs/esm';
import { Gender } from '../../enumerations/gender.model';
import { IDepartment } from '../../department/department.model';
@Component({
  standalone: true,
  selector: 'jhi-analysis-modal',
  templateUrl: './analysis-modal.component.html',
})
export class AnalysisModalComponent implements OnInit {
  @Input() analysisContent: any;
  @Input() fileName = '';
  cleanedContent: any;
  parsedData: any;
  private modalService = inject(NgbModal);
  constructor(public activeModal: NgbActiveModal) {}
  /* eslint-disable */
  ngOnInit(): void {
    this.cleanedContent = this.analysisContent
      .replace(/```json/g, '')
      .replace(/```/g, '')
      .replace(/'''/g, '')
      .trim();
    this.parsedData = JSON.parse(this.cleanedContent);
    console.log(this.parsedData);
  }
  openCreate(): void {
    try {
      const employeeData = {
        name: this.parsedData.name || '',
        phone: this.parsedData.phone || '',
        email: this.parsedData.email || '',
        address: this.parsedData.address || '',
        gender: this.parsedData.gender ? (this.parsedData.gender.toLowerCase() === 'male' ? Gender.MALE : Gender.FEMALE) : null,
        dateOfBirth: this.parsedData.dateOfbirth
          ? dayjs(this.parsedData.dateOfbirth, 'DD/MM/YYYY') // Parse thành đối tượng Day.js
          : null,
        department:
          this.parsedData.recommendedDepartment && (this.parsedData.recommendedDepartment.id ?? this.parsedData.recommendedDepartment['id'])
            ? ({
                id: parseInt(this.parsedData.recommendedDepartment.id ?? this.parsedData.recommendedDepartment['id'], 10),
              } as IDepartment)
            : null,
      };
      const modalRef = this.modalService.open(EmployeeUpdateComponent);
      modalRef.componentInstance.employeeTmp = employeeData;

      // Close the current modal after opening the new one
      this.activeModal.close();
    } catch (error) {
      console.error('Error parsing analysisContent:', error);
    }
  }
}
