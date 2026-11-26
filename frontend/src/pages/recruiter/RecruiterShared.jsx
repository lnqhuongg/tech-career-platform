import { Icon } from '../../layouts/AdminLayout.jsx'

export function PageHeading({ eyebrow, title, description, action }) {
  return (
    <div className="mb-7 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
      <div>
        <p className="mb-1.5 text-[11px] font-bold uppercase tracking-[0.16em] text-cyan-700">{eyebrow}</p>
        <h1 className="text-[25px] font-bold tracking-tight text-slate-900 sm:text-[30px]">{title}</h1>
        <p className="mt-1.5 text-[13px] text-slate-500">{description}</p>
      </div>
      {action}
    </div>
  )
}

export function SectionCard({ title, subtitle, action, children, className = '' }) {
  return (
    <section className={`overflow-hidden rounded-2xl border border-slate-200/80 bg-white shadow-sm shadow-slate-200/40 ${className}`}>
      <div className="flex items-center justify-between gap-3 border-b border-slate-100 px-5 py-4 sm:px-6">
        <div><h2 className="text-sm font-bold text-slate-800">{title}</h2>{subtitle && <p className="mt-1 text-[11px] text-slate-400">{subtitle}</p>}</div>
        {action}
      </div>
      {children}
    </section>
  )
}

export function StatusPill({ status }) {
  const styles = {
    'Đang tuyển': 'bg-emerald-50 text-emerald-700',
    'Đã đóng': 'bg-slate-100 text-slate-600',
    'Chờ duyệt': 'bg-amber-50 text-amber-700',
    'Đang xem xét': 'bg-blue-50 text-blue-700',
    'Phỏng vấn': 'bg-violet-50 text-violet-700',
    'Đã chấp nhận': 'bg-emerald-50 text-emerald-700',
    'Từ chối': 'bg-rose-50 text-rose-700',
  }
  return <span className={`inline-flex whitespace-nowrap rounded-full px-2.5 py-1 text-[10px] font-semibold ${styles[status] ?? 'bg-slate-100 text-slate-600'}`}><span className="mr-1.5 mt-[3px] h-1.5 w-1.5 rounded-full bg-current" />{status}</span>
}

export function SearchField({ value, onChange, placeholder = 'Tìm kiếm...' }) {
  return (
    <label className="flex min-w-[210px] items-center gap-2 rounded-xl border border-slate-200 bg-white px-3 py-2.5 text-slate-400 focus-within:border-cyan-500">
      <Icon name="search" size={16} />
      <input className="w-full bg-transparent text-xs text-slate-700 outline-none placeholder:text-slate-400" value={value} onChange={(event) => onChange(event.target.value)} placeholder={placeholder} />
    </label>
  )
}

export function Modal({ title, onClose, children, wide = false }) {
  return (
    <div className="fixed inset-0 z-50 grid place-items-center overflow-y-auto bg-slate-950/40 p-4" onMouseDown={(event) => { if (event.target === event.currentTarget) onClose() }}>
      <section aria-labelledby="recruiter-modal-title" aria-modal="true" className={`my-auto w-full ${wide ? 'max-w-2xl' : 'max-w-lg'} rounded-2xl bg-white shadow-xl`} role="dialog">
        <div className="flex items-center justify-between border-b border-slate-100 px-5 py-4">
          <h2 className="text-sm font-bold text-slate-800" id="recruiter-modal-title">{title}</h2>
          <button aria-label="Đóng" className="rounded-lg px-2 py-1 text-lg text-slate-400 hover:bg-slate-100" onClick={onClose}>×</button>
        </div>
        <div className="p-5">{children}</div>
      </section>
    </div>
  )
}

export function FormField({ label, children, className = '' }) {
  return <label className={`block ${className}`}><span className="mb-1.5 block text-[11px] font-semibold text-slate-600">{label}</span>{children}</label>
}

export const inputClass = 'w-full rounded-xl border border-slate-200 bg-white px-3 py-2.5 text-xs text-slate-700 outline-none transition focus:border-cyan-500 focus:ring-2 focus:ring-cyan-100'
