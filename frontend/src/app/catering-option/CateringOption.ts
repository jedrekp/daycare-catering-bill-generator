export interface ICateringOption {
    id: number
    optionName: string
    dailyCost: number
    disabled: boolean
}

export class CateringOption implements ICateringOption{
    constructor(
        public id: number,
        public optionName: string,
        public dailyCost: number,
        public disabled: boolean
    ) { }
}