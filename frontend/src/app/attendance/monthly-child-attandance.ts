export interface IMonthlyChildAttendance {
    childId: number
    datesWhenPresent: string[]
    datesWhenAbsent: string[]
}

export class MonthlyChildAttendance implements IMonthlyChildAttendance {

    constructor(
        public childId: number,
        public datesWhenPresent: string[] = [],
        public datesWhenAbsent: string[] = []
    ) { }
}