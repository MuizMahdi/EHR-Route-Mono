import { Entity, PrimaryGeneratedColumn, OneToMany, OneToOne, JoinColumn } from "typeorm";
import { EhrHistory } from "./EhrHistory";
import { EhrCondition } from "./EhrCondition";
import { EhrAllergyAndReaction } from "./EhrAllergyAndReaction";
import { EhrPatientInfo } from "./EhrPatientInfo";


@Entity()
export class MedicalRecord
{
   @PrimaryGeneratedColumn()
   id: number;

   @OneToMany(type => EhrHistory, history => history.medicalRecord, {cascade:true})
   history: EhrHistory[];

   @OneToMany(type => EhrCondition, condition => condition.medicalRecord, {cascade:true})
   conditions: EhrCondition[];

   @OneToMany(type => EhrAllergyAndReaction, allergy => allergy.medicalRecord, {cascade:true})
   allergies: EhrAllergyAndReaction[];

   @OneToOne(type => EhrPatientInfo, {cascade:true})
   @JoinColumn()
   patientData: EhrPatientInfo;
}
