export interface Transaction {
  id: number;
  amount: number;
  merchantName: string;
  tenpistName: string;
  transactionDate: string;
  createdAt: string;
}

export interface TransactionRequest {
  amount: number;
  merchantName: string;
  tenpistName: string;
  transactionDate: string;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}
