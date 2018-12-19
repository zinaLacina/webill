import { Moment } from 'moment';

export interface IMeters {
    id?: number;
    latituude?: number;
    longitude?: number;
    qrCode?: File;
    addressMeters?: string;
    dateCreated?: Moment;
    dateModified?: Moment;
}

export class Meters implements IMeters {
    constructor(
        public id?: number,
        public latituude?: number,
        public longitude?: number,
        public qrCode?: File,
        public addressMeters?: string,
        public dateCreated?: Moment,
        public dateModified?: Moment
    ) {}
}
