import { Child } from '../child/child';

export class PreschoolGroup {

    constructor(
        public id: number,
        public groupName: string,
        public children: Child[],
    ) { }

}