import { Entity, PrimaryGeneratedColumn, Column, OneToOne, JoinColumn, ManyToOne } from "typeorm";
import { EhrCondition } from "./EhrCondition";
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

   @OneToOne(type => EhrCondition)
   @JoinColumn()
   condition: EhrCondition;

   @Column()
   occurrence: boolean;

   @ManyToOne(type => MedicalRecord, medicalRecord => medicalRecord.history)
   medicalRecord: MedicalRecord;
}
