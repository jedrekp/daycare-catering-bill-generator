import { PreschoolGroup } from '../preschool-group/preschool-group';

export class Child {

    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public preschoolGroup: PreschoolGroup = null
    ) { }
}