import { PreschoolGroup } from '../preschool-group/preschool-group';
import { CateringOption } from '../catering-option/CateringOption';

export class Child {

    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public preschoolGroup: PreschoolGroup = null,
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