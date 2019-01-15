import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from "typeorm";
import { MedicalRecord } from './MedicalRecord';


/*
*  A disease, an issue, or a medical condition that the user has or had.
*/

@Entity()
export class EhrCondition
{
   @PrimaryGeneratedColumn()
   id: number;

   @Column()
   condition: string;

   @ManyToOne(type => MedicalRecord, medicalRecord => medicalRecord.conditions)
   medicalRecord: MedicalRecord;
}
