export class MonthlyChildAttendance {

    constructor(
        public childId: number,
        public datesWhenPresent: string[] = [],
        public datesWhenAbsent: string[] = []
    ) { }
}