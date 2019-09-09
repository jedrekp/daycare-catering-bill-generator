import { Child } from '../child/child';

export class DaycareGroup {

    constructor(
        public id: number,
        public groupName: string,
        public children: Child[] = []
    ) { }

}