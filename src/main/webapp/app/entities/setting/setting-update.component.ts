import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils } from 'ng-jhipster';

import { ISetting } from 'app/shared/model/setting.model';
import { SettingService } from './setting.service';

@Component({
    selector: 'jhi-setting-update',
    templateUrl: './setting-update.component.html'
})
export class SettingUpdateComponent implements OnInit {
    setting: ISetting;
    isSaving: boolean;
    dateCreated: string;
    dateModified: string;

    selectedFiles: FileList;
    currentFileUpload: File;

    constructor(private dataUtils: JhiDataUtils, private settingService: SettingService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ setting }) => {
            this.setting = setting;
            this.dateCreated = this.setting.dateCreated != null ? this.setting.dateCreated.format(DATE_TIME_FORMAT) : null;
            this.dateModified = this.setting.dateModified != null ? this.setting.dateModified.format(DATE_TIME_FORMAT) : null;
        });
    }

    // byteSize(field) {
    //     return this.dataUtils.byteSize(field);
    // }

    // openFile(contentType, field) {
    //     return this.dataUtils.openFile(contentType, field);
    // }

    setFileData(event) {
        this.selectedFiles = event.target.files;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.setting.dateCreated = this.dateCreated != null ? moment(this.dateCreated, DATE_TIME_FORMAT) : null;
        this.setting.dateModified = this.dateModified != null ? moment(this.dateModified, DATE_TIME_FORMAT) : null;
        this.currentFileUpload = this.selectedFiles.item(0);

        if (this.setting.id !== undefined) {
            //console.log(this.setting);
            this.subscribeToSaveResponse(this.settingService.update(this.setting, this.currentFileUpload));
            this.selectedFiles = undefined;
        } else {
            //console.log(this.setting);
            this.subscribeToSaveResponse(this.settingService.create(this.setting, this.currentFileUpload));
            this.selectedFiles = undefined;
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISetting>>) {
        result.subscribe((res: HttpResponse<ISetting>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
