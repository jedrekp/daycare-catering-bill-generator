import { DaycareGroup } from '../daycare-group/daycare-group';

export class AppUser {

    constructor(
        public id: number,
        public firstName: string,
        public username: string,
        public daycareRole: string,
        public daycareGroup: DaycareGroup = null,
    ) { }
}
