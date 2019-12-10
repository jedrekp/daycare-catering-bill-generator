export class DailyGroupAttendance {

    constructor(
        public daycareGroupId: number,
        public date: Date,
        public presentChildrenIds: number[],
        public absentChildrenIds: number[]
    ) { }
}