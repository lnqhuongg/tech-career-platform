import { useOutletContext } from 'react-router-dom'
import { jobs } from './adminData.js'
import { PageHeading, Pagination, SearchEmpty, TableCard, JobRow } from './components/AdminShared.jsx'

export default function JobsPage() {
  const { search } = useOutletContext()
  const filtered = jobs.filter((job) => `${job.title} ${job.company} ${job.category} ${job.location}`.toLowerCase().includes(search.toLowerCase()))
  return (
    <>
      <PageHeading eyebrow="Quản lý tuyển dụng" title="Tin tuyển dụng" description="Theo dõi, kiểm duyệt các cơ hội việc làm được đăng tải." action={<button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]">+ Thêm tin tuyển dụng</button>} />
      <div className="mb-5 flex gap-2 overflow-x-auto pb-1">{[['Tất cả tin', '1.284'], ['Đang tuyển', '926'], ['Chờ duyệt', '24'], ['Đã đóng', '334']].map(([label, count], index) => <button key={label} className={`whitespace-nowrap rounded-xl border px-4 py-2.5 text-[11px] font-semibold ${index === 0 ? 'border-[#142544] bg-[#142544] text-white' : 'border-slate-200 bg-white text-slate-500 hover:bg-slate-50'}`}>{label} <span className={`ml-1 ${index === 0 ? 'text-slate-300' : 'text-slate-400'}`}>{count}</span></button>)}</div>
      <TableCard title="Tất cả tin tuyển dụng" subtitle={`${filtered.length} tin trong danh sách`} action={<button className="rounded-lg border border-slate-200 px-3 py-2 text-[10px] font-semibold text-slate-600 hover:bg-slate-50">Bộ lọc <span className="ml-1">⌄</span></button>}>
        <div className="overflow-x-auto"><table className="w-full min-w-[850px] text-left"><thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Vị trí tuyển dụng</th><th className="px-4 py-3">Danh mục</th><th className="px-4 py-3">Mức lương</th><th className="px-4 py-3">Địa điểm</th><th className="px-4 py-3">Trạng thái</th><th className="px-4 py-3">Ngày đăng</th></tr></thead><tbody className="divide-y divide-slate-100">{filtered.map((job) => <JobRow key={job.title} job={job} showSalary />)}</tbody></table>{filtered.length === 0 && <SearchEmpty query={search} />}</div><Pagination totalItems={filtered.length} />
      </TableCard>
    </>
  )
}
