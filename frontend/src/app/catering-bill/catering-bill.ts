export interface CateringBill {
    billId: number
    childId: number
    month: string
    year: number
    correction: boolean
    childFullName: string
    totalDue: number
    dailyCateringOrders: DailyCateringOrder[]
}

export interface DailyCateringOrder {
    orderDate: string,
    cateringOptionName: string,
    cateringOptionPrice: number
}