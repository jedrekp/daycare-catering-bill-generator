export interface Child {
    id: number,
    firstName: string,
    lastName: string,
    parentEmail: string,
    archived: boolean,
    daycareGroup?: any,
    assignedOptions?: AssignedCateringOption[]
}

export interface AssignedCateringOption {
    effectiveDate: string,
    cateringOption: any
}
