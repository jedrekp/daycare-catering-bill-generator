import { DaycareGroup } from "../daycare-group/daycare-group";

export interface User {
    id: number
    firstName: string
    lastName: string
    username: string
    daycareRole: string
    daycareGroup?: DaycareGroup
}