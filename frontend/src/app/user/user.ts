import { IDaycareGroup } from '../daycare-group/daycare-group';

export interface IUser {
    id: number
    firstName: string
    lastName: string
    username: string
    daycareRole: string
    daycareGroup: IDaycareGroup
}

export class User implements IUser {

    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public username: string,
        public password: string = null,
        public daycareRole: string,
        public daycareGroup: IDaycareGroup = null,
    ) { }
}
