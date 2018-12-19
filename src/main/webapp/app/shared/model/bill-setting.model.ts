import { Moment } from 'moment';

export const enum TypeBill {
    COMPANY = 'COMPANY',
    LOYALCUSTOMER = 'LOYALCUSTOMER',
    NORMALCUSTOMER = 'NORMALCUSTOMER',
    GOVERNEMENT = 'GOVERNEMENT'
}

export interface IBillSetting {
    id?: number;
    typeBill?: TypeBill;
    pricePerKW?: number;
    discount?: number;
    tax?: number;
    processing?: number;
    enabled?: boolean;
    dateCreated?: Moment;
    dateModified?: Moment;
}

export class BillSetting implements IBillSetting {
    constructor(
        public id?: number,
        public typeBill?: TypeBill,
        public pricePerKW?: number,
        public discount?: number,
        public tax?: number,
        public processing?: number,
        public enabled?: boolean,
        public dateCreated?: Moment,
        public dateModified?: Moment
    ) {
        this.enabled = this.enabled || false;
    }
}
