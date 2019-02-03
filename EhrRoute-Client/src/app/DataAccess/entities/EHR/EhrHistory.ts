import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from "typeorm";
import { MedicalRecord } from "./MedicalRecord";


/*
*  Patients history, each history table has one condition and a boolean that indicates
*  its occurrence, true for untreated, and false for previously treated conditions.
*/

@Entity()
export class EhrHistory
{
   @PrimaryGeneratedColumn()
   id: number;

   @Column()
   condition: string;

   @Column()
   occurrence: boolean;

   @ManyToOne(type => MedicalRecord, medicalRecord => medicalRecord.history)
   medicalRecord: MedicalRecord;
}
