export class CateringOption {
    constructor(
        public id: number,
        public optionName: string,
        public dailyCost: number,
        public disabled: boolean
    ) { }
}