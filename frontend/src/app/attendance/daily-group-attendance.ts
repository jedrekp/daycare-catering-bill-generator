export interface DailyGroupAttendance {
    daycareGroupId: number,
    date: Date,
    presentChildrenIds: number[],
    absentChildrenIds: number[]
}