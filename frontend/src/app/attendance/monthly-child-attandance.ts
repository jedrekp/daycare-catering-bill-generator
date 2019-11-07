export class MonthlyChildAttendance {

    constructor(
        public month: string,
        public year: number,
        public childId: number,
        public daysWhenPresent: Date[],
        public daysWhenAbsent: Date[]
    ) { }
}