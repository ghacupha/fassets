export interface IBankAccount {
    id?: number;
    accountName?: string;
    accountNumber?: string;
    accountBalance?: number;
    categoryId?: number;
}

export class BankAccount implements IBankAccount {
    constructor(
        public id?: number,
        public accountName?: string,
        public accountNumber?: string,
        public accountBalance?: number,
        public categoryId?: number
    ) {}
}
