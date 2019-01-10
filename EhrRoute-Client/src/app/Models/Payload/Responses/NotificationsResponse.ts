import { Notification } from "./Notification";

export interface NotificationsPageResponse
{
   first:boolean;
   last:boolean;
   pageNumber:number;
   pageSize:number;
   resources:Notification[];
   totalElements:number;
   totalPages:number;
}
