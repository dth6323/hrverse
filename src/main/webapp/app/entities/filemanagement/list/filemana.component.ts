import { Component, OnInit, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, tap } from 'rxjs';
import { GenerateContentResult, GoogleGenerativeAI } from '@google/generative-ai';
import { FilemanaService } from '../service/filemana.service';
import { FormsModule } from '@angular/forms';
import { IFileModel } from '../file.model';
import * as pdfjsLib from 'pdfjs-dist';
import { firstValueFrom } from 'rxjs';
import type { PDFDocumentLoadingTask } from 'pdfjs-dist/types/src/display/api';
import { AnalysisModalComponent } from './analysis-modal.component';
import { DepartmentService } from '../../department/service/department.service';
import { HttpResponse } from '@angular/common/http';
import { IDepartment } from '../../department/department.model';
@Component({
  standalone: true,
  selector: 'jhi-file',
  templateUrl: './filemana.component.html',
  imports: [FormsModule],
})
/* eslint-disable */
export class FileComponent implements OnInit {
  subscription: Subscription | null = null;
  files?: IFileModel[];
  isLoading = false;
  selectedFile: File | null = null;
  keyword = '';
  pdfText = '';
  private fileService = inject(FilemanaService);
  private modalService = inject(NgbModal);
  private deService = inject(DepartmentService);
  private genAI = new GoogleGenerativeAI('AIzaSyByIRLR_YmMrFbTmohJqlm_jMMvWa7oBGg');
  ngOnInit(): void {
    this.loadFiles();
  }
  searchFile(): void {
    this.isLoading = true;
    this.fileService.searchFile(this.keyword).subscribe({
      next: data => {
        this.files = data.body ?? [];
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }
  async analyzeFileContent(fileName: string): Promise<void> {
    try {
      const content = await this.extractTextFromPdfFileName(fileName);
      if (!content) {
        // Mở modal với thông báo lỗi
        this.openAnalysisModal('Không đọc được nội dung từ file PDF', fileName);
        return;
      }

      const departmentsResponse = await firstValueFrom(
        this.deService.query().pipe(
          tap((res: HttpResponse<IDepartment[]>) => {
            if (!res.body) {
              throw new Error('Không lấy được danh sách phòng ban');
            }
          }),
        ),
      );
      console.log(departmentsResponse);
      //eslint-disable
      const departments =
        departmentsResponse.body?.map((dept: IDepartment) => ({ id: dept.id || 'unknown', name: dept.departmentName || 'Unknown' })) || [];
      const departmentInfo = departments.map(dept => `ID: ${dept.id},Department: ${dept.name}`).join('\n');
      const prompt = `
      Please parse the following CV content and return the information in this JSON format
      Additionally, based on the following list of departments and their descriptions, recommend the most suitable department for this candidate and provide a brief explanation for your recommendation:
      ${departmentInfo}. If a field is missing, enter "Unknown":
      {
        "name": "",
        "phone": "",
        "email": "",
        "address": "",
        "gender": "male or female or Unknown",
        "dateOfBirth": "",
        "skill": "",
        "workExperience": "",
        "degree": "",
        "recommendedDepartment": {
          "id": "",
          "name": "",
          "explanation": ""
        }
      }
      CV Content:
      ${content}
    `;
      const model = this.genAI.getGenerativeModel({ model: 'gemini-2.0-flash' });
      const result = await model.generateContent(prompt);
      const response = result.response;
      const genText = response.text();

      this.openAnalysisModal(genText, fileName);
    } catch (error) {
      console.error('Lỗi phân tích với GenAI:', error);
      // Mở modal với thông báo lỗi
      this.openAnalysisModal(`Lỗi khi phân tích nội dung PDF: `, fileName);
    }
  }
  async extractTextFromPdfFileName(fileName: string): Promise<string> {
    try {
      this.isLoading = true;
      // Thiết lập workerSrc cho JHipster
      pdfjsLib.GlobalWorkerOptions.workerSrc = '/content/pdfjs/pdf.worker.mjs';

      const blob = await firstValueFrom(this.fileService.getFile(fileName));
      if (blob.size === 0) {
        throw new Error('Tệp PDF rỗng');
      }

      const arrayBuffer = await blob.arrayBuffer();
      const typedArray = new Uint8Array(arrayBuffer);

      const loadingTask = pdfjsLib.getDocument({ data: typedArray });
      const pdf = await loadingTask.promise;

      let textContent = '';
      for (let i = 1; i <= pdf.numPages; i++) {
        const page = await pdf.getPage(i);
        const text = await page.getTextContent();
        const pageText = (text.items as { str: string }[]).map(item => item.str).join(' ');
        textContent += pageText + '\n';
      }

      this.pdfText = textContent;
      if (!textContent.trim()) {
        alert('PDF không chứa văn bản có thể trích xuất.');
        return '';
      }
      return textContent;
    } catch (err) {
      console.error('Lỗi đọc PDF:', err);
      alert('Không thể đọc nội dung PDF. Vui lòng kiểm tra tệp hoặc định dạng.');
      return '';
    } finally {
      this.isLoading = false;
    }
  }

  downloadFile(fileName: string): void {
    this.isLoading = true;
    this.fileService.getFile(fileName).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        this.isLoading = false;
      },
      error: () => {
        alert('Error downloading file.');
        this.isLoading = false;
      },
    });
  }
  searchFileWithHighLight(): void {
    this.isLoading = true;
    this.fileService.searchFileWithHighlight(this.keyword).subscribe({
      next: data => {
        this.files = data.body ?? [];
        this.isLoading = false;
      },
    });
  }
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  uploadFile(): void {
    if (!this.selectedFile) {
      alert('Please select a file to upload.');
      return;
    }

    this.isLoading = true;

    this.fileService.uploadFile(this.selectedFile).subscribe({
      next: () => {
        alert('File uploaded successfully!');
        this.loadFiles();
      },
      complete: () => {
        this.isLoading = false;
        this.selectedFile = null;
        const inputElement = document.querySelector('input[type="file"]') as HTMLInputElement;
        inputElement.value = ''; // Clear the file input value
      },
    });
  }
  private loadFiles(): void {
    this.isLoading = true;
    this.fileService.getAllFiles().subscribe({
      next: (response: string[]) => {
        this.files = response.map(fileName => ({
          fileName,
          content: null,
          highlight: null,
        }));
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }
  private openAnalysisModal(analysisContent: any, fileName: string): void {
    const modalRef = this.modalService.open(AnalysisModalComponent);
    modalRef.componentInstance.analysisContent = analysisContent;
    modalRef.componentInstance.fileName = fileName;
  }
}
