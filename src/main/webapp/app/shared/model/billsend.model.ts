import { Moment } from 'moment';

export interface IBillsend {
    id?: number;
    image?: string;
    dateCreated?: Moment;
    dateModified?: Moment;
}

export class Billsend implements IBillsend {
    constructor(public id?: number, public image?: string, public dateCreated?: Moment, public dateModified?: Moment) {}
}
