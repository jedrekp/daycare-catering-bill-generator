export interface IDailyGroupAttendance {
    daycareGroupId: number
    date: Date
    presentChildrenIds: number[]
    absentChildrenIds: number[]
}


export class DailyGroupAttendance implements IDailyGroupAttendance {

    constructor(
        public daycareGroupId: number,
        public date: Date,
        public presentChildrenIds: number[],
        public absentChildrenIds: number[]
    ) { }
}