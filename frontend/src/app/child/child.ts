import { PreschoolGroup } from '../preschool-group/preschool-group';
import { Diet } from '../diet/Diet';

export class Child {

    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public preschoolGroup: PreschoolGroup = null,
        public chosenDiets: ChosenDiet[] = []
    ) { }
}

export class ChosenDiet {

    constructor(
        public effectiveDate: Date,
        public diet: Diet
        ) { }
}