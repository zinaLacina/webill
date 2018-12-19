import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils } from 'ng-jhipster';

import { IMeters } from 'app/shared/model/meters.model';
import { MetersService } from './meters.service';

@Component({
    selector: 'jhi-meters-update',
    templateUrl: './meters-update.component.html'
})
export class MetersUpdateComponent implements OnInit {
    meters: IMeters;
    isSaving: boolean;
    dateCreated: string;
    dateModified: string;

    //title: string = 'Drag and drop to change cordinate';
    lat: number = 34.6913;
    lng: number = 135.1830;

    //for marker
    latitude: number = 34.6913;
    longitude: number = 135.1830;

    // initialize coordinates


    constructor(private dataUtils: JhiDataUtils, private metersService: MetersService, private activatedRoute: ActivatedRoute) {

    }
    onChangeLocation(event){
        this.latitude = event.coords.lat;
        this.longitude = event.coords.lng;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ meters }) => {
            this.meters = meters;

            this.latitude = this.meters.latituude != null ? this.meters.latituude : 34.6913;
            this.longitude = this.meters.longitude != null ? this.meters.longitude : 135.1830;

        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        //console.log(this.meters);
        this.isSaving = true;
        this.meters.latituude = this.latitude ;
        this.meters.longitude = this.longitude;
        //console.log(this.meters);
        //console.log(this.metersService.convertDateFromClient(this.meters));

        if (this.meters.id !== undefined) {
            this.subscribeToSaveResponse(this.metersService.update(this.meters));
        } else {
            this.subscribeToSaveResponse(this.metersService.create(this.meters));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMeters>>) {
        result.subscribe((res: HttpResponse<IMeters>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
