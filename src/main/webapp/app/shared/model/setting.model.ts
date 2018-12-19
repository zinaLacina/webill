import { Moment } from 'moment';

export interface ISetting {
    id?: number;
    companyName?: string;
    companyLogo?: File;
    companyNumber?: string;
    companyAddress?: string;
    dateCreated?: Moment;
    dateModified?: Moment;
}

export class Setting implements ISetting {
    constructor(
        public id?: number,
        public companyName?: string,
        public companyLogo?: File,
        public companyNumber?: string,
        public companyAddress?: string,
        public dateCreated?: Moment,
        public dateModified?: Moment
    ) {}
}
