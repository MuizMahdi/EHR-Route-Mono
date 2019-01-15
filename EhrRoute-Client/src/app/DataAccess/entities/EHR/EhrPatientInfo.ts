import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";


@Entity()
export class EhrPatientInfo
{
   @PrimaryGeneratedColumn()
   id: number;

   @Column()
   name: string;

   @Column()
   gender: string;
   
   @Column()
   race: string;
   
   @Column()
   birthDate: string;

   @Column()
   language: string;

   @Column()
   address: string;

   @Column()
   phone: string;

   @Column()
   email: string;

   @Column()
   patientId: string;
}
