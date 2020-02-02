import { IChild } from '../child/child';
import { IAppUser } from '../app-users/appUser';

export interface IDaycareGroup {
    id: number
    groupName: string
    children: IChild[]
    groupSupervisor: IAppUser
}

export class DaycareGroup {

    constructor(
        public id: number,
        public groupName: string,
        public children: IChild[] = [],
        public groupSupervisor: IAppUser = null
    ) { }

}