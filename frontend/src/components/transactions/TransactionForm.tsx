import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { TransactionRequest } from '../../types/transaction';

const transactionSchema = z.object({
  amount: z
    .number({ invalid_type_error: 'El monto debe ser un numero' })
    .min(0, 'El monto no puede ser negativo'),
  merchantName: z
    .string()
    .min(1, 'El comercio es obligatorio')
    .max(255, 'El comercio no puede exceder 255 caracteres'),
  tenpistName: z
    .string()
    .min(1, 'El nombre del Tenpista es obligatorio')
    .max(255, 'El nombre no puede exceder 255 caracteres'),
  transactionDate: z
    .string()
    .min(1, 'La fecha es obligatoria')
    .refine((date) => {
      const selectedDate = new Date(date);
      const now = new Date();
      return selectedDate <= now;
    }, 'La fecha no puede ser futura'),
});

type FormData = z.infer<typeof transactionSchema>;

interface TransactionFormProps {
  onSubmit: (data: TransactionRequest) => void;
  onCancel: () => void;
  loading: boolean;
}

function TransactionForm({ onSubmit, onCancel, loading }: TransactionFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(transactionSchema),
    defaultValues: {
      amount: 0,
      merchantName: '',
      tenpistName: '',
      transactionDate: new Date().toISOString().slice(0, 16),
    },
  });

  const onFormSubmit = (data: FormData) => {
    const request: TransactionRequest = {
      ...data,
      transactionDate: new Date(data.transactionDate).toISOString(),
    };
    onSubmit(request);
  };

  const maxDateTime = new Date().toISOString().slice(0, 16);

  return (
    <form onSubmit={handleSubmit(onFormSubmit)}>
      <div className="form-group">
        <label className="form-label" htmlFor="tenpistName">
          Nombre del Tenpista
        </label>
        <input
          id="tenpistName"
          type="text"
          className="form-input"
          placeholder="Ej: Juan Perez"
          {...register('tenpistName')}
        />
        {errors.tenpistName && (
          <span className="form-error">{errors.tenpistName.message}</span>
        )}
      </div>

      <div className="form-group">
        <label className="form-label" htmlFor="merchantName">
          Giro o Comercio
        </label>
        <input
          id="merchantName"
          type="text"
          className="form-input"
          placeholder="Ej: Supermercado Lider"
          {...register('merchantName')}
        />
        {errors.merchantName && (
          <span className="form-error">{errors.merchantName.message}</span>
        )}
      </div>

      <div className="form-group">
        <label className="form-label" htmlFor="amount">
          Monto (CLP)
        </label>
        <input
          id="amount"
          type="number"
          className="form-input"
          placeholder="Ej: 15000"
          min="0"
          {...register('amount', { valueAsNumber: true })}
        />
        {errors.amount && (
          <span className="form-error">{errors.amount.message}</span>
        )}
      </div>

      <div className="form-group">
        <label className="form-label" htmlFor="transactionDate">
          Fecha de Transaccion
        </label>
        <input
          id="transactionDate"
          type="datetime-local"
          className="form-input"
          max={maxDateTime}
          {...register('transactionDate')}
        />
        {errors.transactionDate && (
          <span className="form-error">{errors.transactionDate.message}</span>
        )}
      </div>

      <div className="modal-footer" style={{ padding: 0, border: 'none', marginTop: '24px' }}>
        <button
          type="button"
          className="btn btn-secondary"
          onClick={onCancel}
          disabled={loading}
        >
          Cancelar
        </button>
        <button
          type="submit"
          className="btn btn-primary"
          disabled={loading}
        >
          {loading ? 'Guardando...' : 'Guardar Transaccion'}
        </button>
      </div>
    </form>
  );
}

export default TransactionForm;
