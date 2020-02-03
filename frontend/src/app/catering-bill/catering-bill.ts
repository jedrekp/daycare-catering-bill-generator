export interface ICateringBill {
    billId: number
    childId: number
    month: string
    year: number
    correction: boolean
    childFullName: string
    totalDue: number
    dailyCateringOrders: DailyCateringOrder[]
}

export class CateringBill implements ICateringBill {

    constructor(
        public billId: number,
        public childId: number,
        public month: string,
        public year: number,
        public correction: boolean,
        public childFullName: string,
        public totalDue: number,
        public dailyCateringOrders: DailyCateringOrder[] = []
    ) { }
}

export class DailyCateringOrder {
    constructor(
        public orderDate: string,
        public cateringOptionName: string,
        public cateringOptionPrice: number
    ) { }
}