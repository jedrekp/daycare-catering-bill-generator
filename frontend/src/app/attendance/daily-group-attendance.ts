export class DailyGroupAttendance {

    constructor(
        public date: Date,
        public presentChildrenIds: number[],
        public absentChildrenIds: number[]
    ) { }
}