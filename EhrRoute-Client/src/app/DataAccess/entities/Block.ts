import { Entity, PrimaryGeneratedColumn, Column, OneToOne } from "typeorm";


@Entity()
export class Block
{
   // Block Header Contents

   @PrimaryGeneratedColumn()
   index:number;

   @Column()
   hash:string;

   @Column()
   previousHash:string;

   @Column()
   timeStamp:string;

   @Column()
   merkleRoot:string;

   @Column()
   networkUUID:string;

   // Transaction Contents

   @Column()
   transactionId:string;

   @Column()
   senderPubKey:string;

   @Column()
   senderAddress:string;

   @Column()
   recipientAddress:string;

   @Column()
   signature:string;
}
