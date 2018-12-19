import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IMeters } from 'app/shared/model//meters.model';

export interface IAssignMeters {
    id?: number;
    startDateContract?: Moment;
    endDateContract?: Moment;
    reasonEnd?: string;
    enabled?: boolean;
    createdAt?: Moment;
    updateAt?: Moment;
    metersUser?: IUser;
    historyMeterUser?: IMeters;
}

export class AssignMeters implements IAssignMeters {
    constructor(
        public id?: number,
        public startDateContract?: Moment,
        public endDateContract?: Moment,
        public reasonEnd?: string,
        public enabled?: boolean,
        public createdAt?: Moment,
        public updateAt?: Moment,
        public metersUser?: IUser,
        public historyMeterUser?: IMeters
    ) {
        this.enabled = this.enabled || false;
    }
}
