import { Component, inject, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeUpdateComponent } from '../../employee/update/employee-update.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Dayjs } from 'dayjs';
import dayjs from 'dayjs/esm';
import { Gender } from '../../enumerations/gender.model';
@Component({
  standalone: true,
  selector: 'jhi-analysis-modal',
  templateUrl: './analysis-modal.component.html',
})
export class AnalysisModalComponent {
  @Input() analysisContent = '';
  @Input() fileName = '';
  private modalService = inject(NgbModal);

  constructor(public activeModal: NgbActiveModal) {}
  openCreate(): void {
    try {
      const cleanedContent = this.analysisContent
        .replace(/```json/g, '')
        .replace(/```/g, '')
        .replace(/'''/g, '')
        .trim();
      // Parse the JSON string from analysisContent
      const parsedData = JSON.parse(cleanedContent);
      const employeeData = {
        name: parsedData.name || '',
        phone: parsedData.phone || '',
        email: parsedData.email || '',
        address: parsedData.address || '',
        gender: parsedData.gender ? (parsedData.gender.toLowerCase() === 'male' ? Gender.MALE : Gender.FEMALE) : null,
        dateOfBirth: parsedData.dateOfbirth
          ? dayjs(parsedData.dateOfbirth, 'DD/MM/YYYY') // Parse thành đối tượng Day.js
          : null,
      };

      //eslint-disable-next-line
      console.log(employeeData);
      const modalRef = this.modalService.open(EmployeeUpdateComponent);
      modalRef.componentInstance.employeeTmp = employeeData;

      // Close the current modal after opening the new one
      this.activeModal.close();
    } catch (error) {
      console.error('Error parsing analysisContent:', error);
    }
  }
}
