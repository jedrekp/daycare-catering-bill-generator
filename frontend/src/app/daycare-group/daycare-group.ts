import { Child } from '../child/child';
import { AppUser } from '../app-users/appUser';

export class DaycareGroup {

    constructor(
        public id: number,
        public groupName: string,
        public children: Child[] = [],
        public groupSupervisor: AppUser = null
    ) { }

}