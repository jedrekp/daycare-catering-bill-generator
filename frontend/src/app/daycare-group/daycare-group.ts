import { Child } from "../child/child";
import { User } from "../user/user";

export interface DaycareGroup {
    id: number
    groupName: string
    children: Child[]
    groupSupervisor?: User
}