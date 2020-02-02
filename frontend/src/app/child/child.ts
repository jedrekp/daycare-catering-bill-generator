import { CateringOption } from '../catering-option/CateringOption';
import { IDaycareGroup } from '../daycare-group/daycare-group';

export interface IChild {
    id: number
    firstName: string
    lastName: string
    parentEmail: string
    archived: boolean
    daycareGroup: IDaycareGroup
    assignedOptions: AssignedOption[]
}

export class Child {

    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public parentEmail: string,
        public archived: boolean,
        public daycareGroup: IDaycareGroup = null,
        public assignedOptions: AssignedOption[] = []
    ) { }
}

export class AssignedOption {

    constructor(
        public effectiveDate: string,
        public cateringOption: CateringOption
    ) { }
}