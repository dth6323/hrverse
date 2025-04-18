import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRewardPunishment, NewRewardPunishment } from '../reward-punishment.model';

export type PartialUpdateRewardPunishment = Partial<IRewardPunishment> & Pick<IRewardPunishment, 'id'>;

type RestOf<T extends IRewardPunishment | NewRewardPunishment> = Omit<T, 'applyDate'> & {
  applyDate?: string | null;
};

export type RestRewardPunishment = RestOf<IRewardPunishment>;

export type NewRestRewardPunishment = RestOf<NewRewardPunishment>;

export type PartialUpdateRestRewardPunishment = RestOf<PartialUpdateRewardPunishment>;

export type EntityResponseType = HttpResponse<IRewardPunishment>;
export type EntityArrayResponseType = HttpResponse<IRewardPunishment[]>;

@Injectable({ providedIn: 'root' })
export class RewardPunishmentService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reward-punishments');

  create(rewardPunishment: NewRewardPunishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rewardPunishment);
    return this.http
      .post<RestRewardPunishment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rewardPunishment: IRewardPunishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rewardPunishment);
    return this.http
      .put<RestRewardPunishment>(`${this.resourceUrl}/${this.getRewardPunishmentIdentifier(rewardPunishment)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rewardPunishment: PartialUpdateRewardPunishment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rewardPunishment);
    return this.http
      .patch<RestRewardPunishment>(`${this.resourceUrl}/${this.getRewardPunishmentIdentifier(rewardPunishment)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRewardPunishment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRewardPunishment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRewardPunishmentIdentifier(rewardPunishment: Pick<IRewardPunishment, 'id'>): number {
    return rewardPunishment.id;
  }

  compareRewardPunishment(o1: Pick<IRewardPunishment, 'id'> | null, o2: Pick<IRewardPunishment, 'id'> | null): boolean {
    return o1 && o2 ? this.getRewardPunishmentIdentifier(o1) === this.getRewardPunishmentIdentifier(o2) : o1 === o2;
  }

  addRewardPunishmentToCollectionIfMissing<Type extends Pick<IRewardPunishment, 'id'>>(
    rewardPunishmentCollection: Type[],
    ...rewardPunishmentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rewardPunishments: Type[] = rewardPunishmentsToCheck.filter(isPresent);
    if (rewardPunishments.length > 0) {
      const rewardPunishmentCollectionIdentifiers = rewardPunishmentCollection.map(rewardPunishmentItem =>
        this.getRewardPunishmentIdentifier(rewardPunishmentItem),
      );
      const rewardPunishmentsToAdd = rewardPunishments.filter(rewardPunishmentItem => {
        const rewardPunishmentIdentifier = this.getRewardPunishmentIdentifier(rewardPunishmentItem);
        if (rewardPunishmentCollectionIdentifiers.includes(rewardPunishmentIdentifier)) {
          return false;
        }
        rewardPunishmentCollectionIdentifiers.push(rewardPunishmentIdentifier);
        return true;
      });
      return [...rewardPunishmentsToAdd, ...rewardPunishmentCollection];
    }
    return rewardPunishmentCollection;
  }

  protected convertDateFromClient<T extends IRewardPunishment | NewRewardPunishment | PartialUpdateRewardPunishment>(
    rewardPunishment: T,
  ): RestOf<T> {
    return {
      ...rewardPunishment,
      applyDate: rewardPunishment.applyDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRewardPunishment: RestRewardPunishment): IRewardPunishment {
    return {
      ...restRewardPunishment,
      applyDate: restRewardPunishment.applyDate ? dayjs(restRewardPunishment.applyDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRewardPunishment>): HttpResponse<IRewardPunishment> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRewardPunishment[]>): HttpResponse<IRewardPunishment[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
