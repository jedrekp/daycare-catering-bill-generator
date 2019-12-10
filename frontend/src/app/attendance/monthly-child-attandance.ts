export class MonthlyChildAttendance {

    constructor(
        public childId: number,
        public daysWhenPresent: string[] = [],
        public daysWhenAbsent: string[] = []
    ) { }
}