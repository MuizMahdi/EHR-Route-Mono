import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";


@Entity()
export class Address
{
   @PrimaryGeneratedColumn()
   id: number;

   @Column()
   address: string;

   @Column()
   publicKey: string;

   @Column()
   privateKey: string;
}