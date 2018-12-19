import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IBillsend } from 'app/shared/model/billsend.model';
import { BillsendService } from './billsend.service';

@Component({
    selector: 'jhi-billsend-update',
    templateUrl: './billsend-update.component.html'
})
export class BillsendUpdateComponent implements OnInit {
    billsend: IBillsend;
    isSaving: boolean;
    dateCreated: string;
    dateModified: string;

    selectedFiles: FileList;
    currentFileUpload: File;

    constructor(private billsendService: BillsendService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ billsend }) => {
            this.billsend = billsend;
            this.dateCreated = this.billsend.dateCreated != null ? this.billsend.dateCreated.format(DATE_TIME_FORMAT) : null;
            this.dateModified = this.billsend.dateModified != null ? this.billsend.dateModified.format(DATE_TIME_FORMAT) : null;
        });
    }

    setFileData(event) {
        this.selectedFiles = event.target.files;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.billsend.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : null;
        this.billsend.dateModified = this.dateModified != null ? moment(this.dateModified, DATE_TIME_FORMAT) : null;
        this.currentFileUpload = this.selectedFiles.item(0);
        if (this.billsend.id !== undefined) {
            this.subscribeToSaveResponse(this.billsendService.update(this.billsend, this.currentFileUpload));
        } else {
            this.subscribeToSaveResponse(this.billsendService.create(this.billsend, this.currentFileUpload));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBillsend>>) {
        result.subscribe((res: HttpResponse<IBillsend>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
