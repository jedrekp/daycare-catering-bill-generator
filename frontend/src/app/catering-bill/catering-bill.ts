export class CateringBill {

    constructor(
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