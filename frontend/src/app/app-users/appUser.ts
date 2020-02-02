import { IDaycareGroup } from '../daycare-group/daycare-group';

export interface IAppUser{
        id: number
        firstName: string
        username: string
        daycareRole: string
        daycareGroup: IDaycareGroup
}

export class AppUser {

    constructor(
        public id: number,
        public firstName: string,
        public username: string,
        public daycareRole: string,
        public daycareGroup: IDaycareGroup = null,
    ) { }
}
