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

export function StatusBadge({ children }) {
  const classes = children === 'Hoạt động' || children === 'Đang tuyển' || children === 'Đã xác minh'
    || children === 'Đã nhận'
    ? 'bg-emerald-50 text-emerald-700 ring-emerald-600/10'
    : children === 'Chờ duyệt' || children === 'Chờ xác minh' || children === 'Đang tìm việc' || children === 'Chờ xem'
      ? 'bg-amber-50 text-amber-700 ring-amber-600/10'
      : children === 'Đang xem xét' || children === 'Phỏng vấn'
        ? 'bg-blue-50 text-blue-700 ring-blue-600/10'
      : 'bg-slate-100 text-slate-600 ring-slate-500/10'
  return <span className={`inline-flex whitespace-nowrap rounded-full px-2.5 py-1 text-[10px] font-semibold ring-1 ring-inset ${classes}`}><span className="mr-1.5 mt-[3px] h-1.5 w-1.5 rounded-full bg-current" />{children}</span>
}

export function TableCard({ title, subtitle, children, action }) {
  return (
    <section className="overflow-hidden rounded-2xl border border-slate-200/80 bg-white shadow-sm shadow-slate-200/40">
      <div className="flex items-center justify-between border-b border-slate-100 px-5 py-4 sm:px-6">
        <div><h2 className="text-sm font-bold text-slate-800">{title}</h2>{subtitle && <p className="mt-1 text-[11px] text-slate-400">{subtitle}</p>}</div>
        {action}
      </div>
      {children}
    </section>
  )
}

export function SearchEmpty({ query }) {
  return <div className="px-6 py-12 text-center text-sm text-slate-500">Không tìm thấy kết quả cho “{query}”.</div>
}

export function JobRow({ job, showSalary = false }) {
  return <tr className="transition hover:bg-slate-50/70"><td className="px-6 py-4"><div className="flex items-center gap-3"><span className={`grid h-9 w-9 shrink-0 place-items-center rounded-xl text-xs font-bold ${job.color}`}>{job.initials}</span><div><p className="whitespace-nowrap text-xs font-semibold text-slate-800">{job.title}</p><p className="mt-1 text-[10px] text-slate-400">{job.company}</p></div></div></td><td className="px-4 py-4 text-[11px] text-slate-500">{job.category}</td>{showSalary && <td className="whitespace-nowrap px-4 py-4 text-[11px] font-medium text-slate-600">{job.salary}</td>}<td className="px-4 py-4 text-[11px] text-slate-500">{job.location}</td><td className="px-4 py-4"><StatusBadge>{job.status}</StatusBadge></td><td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{job.posted}</td></tr>
}

export function Pagination({ totalItems = 0 }) {
  const pageSize = 10
  const totalPages = Math.max(1, Math.ceil(totalItems / pageSize))
  const visibleItems = Math.min(totalItems, pageSize)

  return (
    <div className="flex flex-col gap-3 border-t border-slate-100 bg-white px-5 py-4 sm:flex-row sm:items-center sm:justify-between sm:px-6">
      <p className="text-xs text-slate-500">
        Hiển thị <span className="font-semibold text-slate-700">{totalItems ? `1–${visibleItems}` : '0'}</span>
        {' '}trên <span className="font-semibold text-slate-700">{totalItems}</span> kết quả
      </p>
      <nav aria-label="Phân trang" className="flex items-center gap-1.5">
        <button
          aria-label="Trang trước"
          className="rounded-lg border border-slate-200 px-3 py-2 text-xs font-medium text-slate-400 disabled:cursor-not-allowed disabled:opacity-60"
          disabled
        >
          Trước
        </button>
        <span aria-current="page" className="grid h-8 min-w-8 place-items-center rounded-lg bg-cyan-700 px-2 text-xs font-semibold text-white">
          1
        </span>
        <span className="px-1 text-xs text-slate-500">/ {totalPages}</span>
        <button
          aria-label="Trang tiếp theo"
          className="rounded-lg border border-slate-200 px-3 py-2 text-xs font-medium text-slate-400 disabled:cursor-not-allowed disabled:opacity-60"
          disabled
        >
          Tiếp
        </button>
      </nav>
    </div>
  )
}
