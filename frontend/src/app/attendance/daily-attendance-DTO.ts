export class DailyAttendanceDTO {

    constructor(
        public date: Date,
        public presentChildrenIds: number[],
        public absentChildrenIds: number[]
    ) { }
}