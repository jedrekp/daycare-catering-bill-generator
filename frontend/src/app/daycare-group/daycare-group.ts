import { IChild } from '../child/child';
import { IUser } from '../user/user';


export interface IDaycareGroup {
    id: number
    groupName: string
    children: IChild[]
    groupSupervisor: IUser
}

export class DaycareGroup implements IDaycareGroup{

    constructor(
        public id: number,
        public groupName: string,
        public children: IChild[] = [],
        public groupSupervisor: IUser = null
    ) { }

}