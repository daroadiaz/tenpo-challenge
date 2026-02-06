import { useState, useEffect, useCallback } from 'react';
import { Transaction, TransactionRequest } from './types/transaction';
import { transactionApi } from './api/transactionApi';
import Header from './components/layout/Header';
import Footer from './components/layout/Footer';
import TransactionList from './components/transactions/TransactionList';
import TransactionForm from './components/transactions/TransactionForm';
import Modal from './components/common/Modal';

function App() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [editingTransaction, setEditingTransaction] = useState<Transaction | null>(null);

  const fetchTransactions = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await transactionApi.getAll();
      setTransactions(data);
    } catch {
      setError('Error al cargar las transacciones');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchTransactions();
  }, [fetchTransactions]);

  const handleCreate = async (data: TransactionRequest) => {
    try {
      setSubmitting(true);
      await transactionApi.create(data);
      await fetchTransactions();
      handleCloseModal();
    } catch {
      setError('Error al crear la transaccion');
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdate = async (data: TransactionRequest) => {
    if (!editingTransaction) return;
    try {
      setSubmitting(true);
      await transactionApi.update(editingTransaction.id, data);
      await fetchTransactions();
      handleCloseModal();
    } catch {
      setError('Error al actualizar la transaccion');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await transactionApi.delete(id);
      await fetchTransactions();
    } catch {
      setError('Error al eliminar la transaccion');
    }
  };

  const handleEdit = (transaction: Transaction) => {
    setEditingTransaction(transaction);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingTransaction(null);
  };

  const handleOpenCreateModal = () => {
    setEditingTransaction(null);
    setIsModalOpen(true);
  };

  return (
    <div className="app">
      <Header />
      <main className="main-content">
        <div className="container">
          <div className="page-header">
            <div>
              <h1 className="page-title">Transacciones</h1>
              <p className="page-subtitle">
                Administra las transacciones de los Tenpistas
              </p>
            </div>
            <button
              className="btn btn-primary"
              onClick={handleOpenCreateModal}
            >
              Nueva Transaccion
            </button>
          </div>

          {error && (
            <div className="alert alert-error">
              {error}
              <button
                onClick={() => setError(null)}
                style={{ marginLeft: '12px', background: 'none', border: 'none', color: 'inherit', cursor: 'pointer' }}
              >
                ×
              </button>
            </div>
          )}

          <TransactionList
            transactions={transactions}
            loading={loading}
            onDelete={handleDelete}
            onEdit={handleEdit}
          />
        </div>
      </main>
      <Footer />

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingTransaction ? 'Editar Transaccion' : 'Nueva Transaccion'}
      >
        <TransactionForm
          onSubmit={editingTransaction ? handleUpdate : handleCreate}
          onCancel={handleCloseModal}
          loading={submitting}
          initialData={editingTransaction}
        />
      </Modal>
    </div>
  );
}

export default App;
