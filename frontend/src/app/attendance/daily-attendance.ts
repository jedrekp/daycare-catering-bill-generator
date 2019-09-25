export class DailyAttendance {

    constructor(
        public date: Date,
        public presentChildrenIds: number[],
        public absentChildrenIds: number[]
    ) { }
}