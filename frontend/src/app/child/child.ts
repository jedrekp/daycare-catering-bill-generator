import { CateringOption } from '../catering-option/CateringOption';
import { DaycareGroup } from '../daycare-group/daycare-group';

export class Child {

    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public parentEmail: string,
        public archived: boolean,
        public daycareGroup: DaycareGroup = null,
        public assignedOptions: AssignedOption[] = []
    ) { }
}

export class AssignedOption {

    constructor(
        public id: number,
        public effectiveDate: Date,
        public cateringOption: CateringOption
    ) { }
}