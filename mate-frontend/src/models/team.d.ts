import {UserType} from "./user";

export type TeamType = {
    id: number,
    name: string,
    description: string,
    memberCnt: number,
    leaderId: number,
    createTime: Date,
    updateTime: Date,
    members?: UserType[],
}
