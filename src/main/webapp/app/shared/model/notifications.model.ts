import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface INotifications {
    id?: number;
    message?: string;
    checked?: boolean;
    createdAt?: Moment;
    updateAt?: Moment;
    senderUser?: IUser;
    receiverUser?: IUser;
}

export class Notifications implements INotifications {
    constructor(
        public id?: number,
        public message?: string,
        public checked?: boolean,
        public createdAt?: Moment,
        public updateAt?: Moment,
        public senderUser?: IUser,
        public receiverUser?: IUser
    ) {
        this.checked = this.checked || false;
    }
}
