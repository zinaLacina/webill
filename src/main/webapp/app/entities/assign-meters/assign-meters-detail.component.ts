import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssignMeters } from 'app/shared/model/assign-meters.model';

@Component({
    selector: 'jhi-assign-meters-detail',
    templateUrl: './assign-meters-detail.component.html'
})
export class AssignMetersDetailComponent implements OnInit {
    assignMeters: IAssignMeters;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ assignMeters }) => {
            this.assignMeters = assignMeters;
        });
    }

    previousState() {
        window.history.back();
    }
}
