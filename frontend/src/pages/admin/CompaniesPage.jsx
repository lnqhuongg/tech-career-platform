import { useOutletContext } from 'react-router-dom'
import { companies } from './adminData.js'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { PageHeading, Pagination, SearchEmpty, StatusBadge, TableCard } from './components/AdminShared.jsx'

export default function CompaniesPage() {
  const { search } = useOutletContext()
  const filtered = companies.filter((company) => `${company.name} ${company.domain} ${company.industry}`.toLowerCase().includes(search.toLowerCase()))
  return (
    <>
      <PageHeading eyebrow="Đối tác doanh nghiệp" title="Công ty" description="Quản lý hồ sơ doanh nghiệp và trạng thái xác minh." action={<button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]">+ Thêm công ty</button>} />
      <div className="mb-5 grid grid-cols-2 gap-3 lg:grid-cols-3">{[['Tổng công ty', '486'], ['Đã xác minh', '442'], ['Chờ xác minh', '44']].map(([label, value]) => <div key={label} className="rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm"><p className="text-[11px] text-slate-500">{label}</p><p className="mt-1 text-xl font-bold text-slate-800">{value}</p></div>)}</div>
      <TableCard title="Danh sách công ty" subtitle={`${filtered.length} doanh nghiệp được hiển thị`} action={<button className="rounded-lg border border-slate-200 px-3 py-2 text-[10px] font-semibold text-slate-600 hover:bg-slate-50">Lọc danh sách</button>}>
        <div className="overflow-x-auto"><table className="w-full min-w-[780px] text-left"><thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Doanh nghiệp</th><th className="px-4 py-3">Lĩnh vực</th><th className="px-4 py-3">Quy mô</th><th className="px-4 py-3">Tin tuyển dụng</th><th className="px-4 py-3">Trạng thái</th><th className="px-4 py-3">Thao tác</th></tr></thead><tbody className="divide-y divide-slate-100">{filtered.map((company) => <tr key={company.name} className="hover:bg-slate-50/70"><td className="px-6 py-4"><div className="flex items-center gap-3"><span className={`grid h-9 w-9 place-items-center rounded-xl text-xs font-bold ${company.color}`}>{company.initials}</span><div><p className="text-xs font-semibold text-slate-800">{company.name}</p><p className="mt-1 text-[10px] text-slate-400">{company.domain}</p></div></div></td><td className="px-4 py-4 text-[11px] text-slate-500">{company.industry}</td><td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{company.size}</td><td className="px-4 py-4 text-[11px] font-semibold text-slate-700">{company.jobs}</td><td className="px-4 py-4"><StatusBadge>{company.status}</StatusBadge></td><td className="px-4 py-4"><button aria-label={`Tuỳ chọn ${company.name}`} className="rounded-lg p-1.5 text-slate-400 hover:bg-slate-100"><Icon name="dots" size={17} /></button></td></tr>)}</tbody></table>{filtered.length === 0 && <SearchEmpty query={search} />}</div><Pagination totalItems={filtered.length} />
      </TableCard>
    </>
  )
}
