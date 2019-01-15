import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from "typeorm";
import { MedicalRecord } from './MedicalRecord';


/*
*  An allergy or a reaction to a drug that the user has.
*/

@Entity()
export class EhrAllergyAndReaction
{
   @PrimaryGeneratedColumn()
   id: number;

   @Column()
   allergy: string;

   @ManyToOne(type => MedicalRecord, medicalRecord => medicalRecord.allergies)
   medicalRecord: MedicalRecord;
}
