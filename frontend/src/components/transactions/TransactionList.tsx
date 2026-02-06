import { useState } from 'react';
import { ClipboardList, Pencil, Trash2, Loader2 } from 'lucide-react';
import { Transaction } from '../../types/transaction';
import { formatAmount, formatDate } from '../../utils/formatters';

interface TransactionListProps {
  transactions: Transaction[];
  loading: boolean;
  onDelete: (id: number) => void;
  onEdit: (transaction: Transaction) => void;
}

function TransactionList({ transactions, loading, onDelete, onEdit }: TransactionListProps) {
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const handleDelete = async (id: number) => {
    if (window.confirm('¿Estas seguro de eliminar esta transaccion?')) {
      setDeletingId(id);
      await onDelete(id);
      setDeletingId(null);
    }
  };

  if (loading) {
    return (
      <div className="card">
        <div className="loading">
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  if (transactions.length === 0) {
    return (
      <div className="card">
        <div className="empty-state">
          <div className="empty-state-icon">
            <ClipboardList size={48} strokeWidth={1.5} />
          </div>
          <h3 className="empty-state-title">No hay transacciones</h3>
          <p className="empty-state-text">
            Comienza agregando una nueva transaccion
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="table-container">
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tenpista</th>
              <th>Comercio</th>
              <th>Monto</th>
              <th>Fecha</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td>#{transaction.id}</td>
                <td>{transaction.tenpistName}</td>
                <td>{transaction.merchantName}</td>
                <td className="amount">{formatAmount(transaction.amount)}</td>
                <td>{formatDate(transaction.transactionDate)}</td>
                <td className="actions-cell">
                  <button
                    className="btn btn-secondary btn-icon"
                    onClick={() => onEdit(transaction)}
                    aria-label="Editar transaccion"
                  >
                    <Pencil size={16} />
                  </button>
                  <button
                    className="btn btn-danger btn-icon"
                    onClick={() => handleDelete(transaction.id)}
                    disabled={deletingId === transaction.id}
                    aria-label="Eliminar transaccion"
                  >
                    {deletingId === transaction.id ? (
                      <Loader2 size={16} className="spinning" />
                    ) : (
                      <Trash2 size={16} />
                    )}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default TransactionList;
