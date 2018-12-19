import { Component, ChangeDetectionStrategy, OnInit, OnDestroy } from '@angular/core';
import {
    startOfDay,
    endOfDay,
    subDays,
    addDays,
    endOfMonth,
    isSameDay,
    isSameMonth,
    addHours,
    addMinutes,
    format,
    startOfMonth,
    getDaysInMonth
} from 'date-fns';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent, CalendarMonthViewDay } from 'angular-calendar';
import { JhiEventManager } from 'ng-jhipster';
import { Router } from '@angular/router';
import { Principal } from 'app/core';
import { Subject, Subscription } from 'rxjs';

const colors: any = {
    red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
    },
    blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
    },
    yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
    },
    green: {
        primary: '#008000',
        secondary: '#C3FDB8'
    }
};

@Component({
    selector: 'jhi-history',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: 'history.component.html',
    styleUrls: ['history.component.css']
})
export class HistoryComponent implements OnInit, OnDestroy {
    modalRef: NgbModalRef;
    isCollapsed = true;

    view = 'month';

    viewDate: Date = new Date();

    modalData: {
        action: string;
        event: CalendarEvent;
    };

    eventSubscriber: Subscription;

    actions: CalendarEventAction[] = [
        {
            label: '✏️',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.handleEvent('edit', event);
            }
        },
        {
            label: '❌',
            onClick: ({ event }: { event: CalendarEvent }): void => {
                this.handleEvent('delete', event);
            }
        }
    ];

    refresh: Subject<any> = new Subject();

    events: CalendarEvent[] = [];

    activeDayIsOpen = true;

    currentAccount: any;

    constructor(
        // private pointsService: PointsService,
        // private bloodPressureService: BloodPressureService,
        // private weightService: WeightService,
        private principal: Principal,
        // private preferencesService: PreferencesService,
        private router: Router,
        private eventManager: JhiEventManager
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.populateCalendar();
        this.registerForChanges();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerForChanges() {
        this.eventSubscriber = this.eventManager.subscribe('pointsListModification', () => this.reset());
        this.eventSubscriber = this.eventManager.subscribe('bloodPressureListModification', () => this.reset());
        this.eventSubscriber = this.eventManager.subscribe('weightListModification', () => this.reset());
    }

    reset() {
        this.events = [];
        this.populateCalendar();
    }

    viewDateChanged() {
        this.reset();
    }

    populateCalendar() {}

    eventTimesChanged({ event, newStart, newEnd }: CalendarEventTimesChangedEvent): void {
        event.start = newStart;
        event.end = newEnd;
        this.handleEvent('Dropped or resized', event);
        this.refresh.next();
    }

    handleEvent(action: string, event: CalendarEvent): void {
        action = action === 'Clicked' ? 'edit' : action;
        this.modalData = { event, action };
        let url = this.router.createUrlTree(['/', { outlets: { popup: event.meta.entity + '/' + event.meta.id + '/' + action } }]);
        if (action === 'edit') {
            url = this.router.createUrlTree(['/' + event.meta.entity, event.meta.id, 'edit']);
        }
        this.router.navigateByUrl(url.toString());
    }
}
