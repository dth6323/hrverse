import { Component, OnInit, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription, tap } from 'rxjs';

import { FilemanaService } from '../service/filemana.service';
import { combineLatest } from 'rxjs/internal/operators/combineLatest';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { IFileModel } from '../file.model';
@Component({
  standalone: true,
  selector: 'jhi-file',
  templateUrl: './filemana.component.html',
  imports: [FormsModule],
})
export class FileComponent implements OnInit {
  subscription: Subscription | null = null;
  files?: IFileModel[];
  isLoading = false;
  selectedFile: File | null = null;
  keyword = '';
  private fileService = inject(FilemanaService);
  private modalService = inject(NgbModal);
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
        // Tải lại danh sách file sau khi upload thành công
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
}
