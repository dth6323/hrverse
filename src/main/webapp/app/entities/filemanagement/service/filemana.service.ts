import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class FilemanaService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/files');

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file); // Đính kèm file vào formData
    return this.http.post(`${this.resourceUrl}/upload`, formData, {
      observe: 'response',
      responseType: 'text',
    });
  }
  searchFile(keyword: string): Observable<any> {
    return this.http.get(`${this.resourceUrl}/search-by-content`, {
      params: { keyword },
      observe: 'response',
    });
  }
  searchFileWithHighlight(keyword: string): Observable<any> {
    return this.http.get(`${this.resourceUrl}/searchWithHighlight`, {
      params: { keyword },
      observe: 'response',
    });
  }
  getFile(fileName: string): Observable<Blob> {
    const params = new HttpParams().set('filename', fileName); // Thêm fileName vào query params
    return this.http.get(`${this.resourceUrl}/readfiles`, {
      params,
      responseType: 'blob',
    });
  }
  getAllFiles(): Observable<any> {
    return this.http.get(`${this.resourceUrl}/filesall`);
  }
}
