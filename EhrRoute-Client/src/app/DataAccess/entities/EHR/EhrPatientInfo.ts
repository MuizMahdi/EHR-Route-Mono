import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";


@Entity()
export class EhrPatientInfo
{
   @PrimaryGeneratedColumn()
   id: number;

   @Column()
   name:string;

   @Column()
   gender:string;

   @Column()
   country:string;

   @Column()
   city:string;

   @Column()
   address:string;

   @Column()
   phone:string;

   @Column()
   birthDate:number;

   @Column()
   email:string;

   @Column()
   userID:number;
}
