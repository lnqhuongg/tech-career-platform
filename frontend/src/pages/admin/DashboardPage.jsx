import { useOutletContext } from 'react-router-dom'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { jobs, statCards } from './adminData.js'
import { JobRow, PageHeading, SearchEmpty, TableCard } from './components/AdminShared.jsx'

export default function DashboardPage() {
  const { search } = useOutletContext()
  const filteredJobs = jobs.filter((job) => `${job.title} ${job.company} ${job.category}`.toLowerCase().includes(search.toLowerCase()))
  return (
    <>
      <PageHeading 
          title="Tổng quan hệ thống" 
          description="Theo dõi các hoạt động mới của nền tảng TechCareer." 
          // action={
          //     <button className="inline-flex items-center justify-center gap-2 rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white shadow-sm transition hover:bg-[#20375c]">
          //       <Icon name="chart" size={16} /> 
          //         Tải báo cáo
          //     </button>
          //   } 
      />
      <div className="mb-7 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {statCards.map((stat) => (
          <article key={stat.label} className="rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40">
            <div className="flex items-start justify-between"><p className="text-xs font-medium text-slate-500">{stat.label}</p><span className={`grid h-9 w-9 place-items-center rounded-xl ${stat.color}`}><Icon name={stat.icon} size={18} /></span></div>
            <div className="mt-4 flex items-end justify-between gap-2"><p className="text-[27px] font-bold tracking-tight text-slate-900">{stat.value}</p><span className="mb-1 inline-flex items-center gap-0.5 text-[10px] font-bold text-emerald-600"><Icon name="arrowUp" size={13} />{stat.change}</span></div>
            <p className="mt-1 text-[10px] text-slate-400">{stat.note}</p>
          </article>
        ))}
      </div>

      <div className="mb-7 grid gap-5 xl:grid-cols-[1.65fr_1fr]">
        <section className="rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40 sm:p-6">
          <div className="mb-6 flex items-start justify-between"><div><h2 className="text-sm font-bold text-slate-800">Tăng trưởng nền tảng</h2><p className="mt-1 text-[11px] text-slate-400">Người dùng mới và tin tuyển dụng</p></div><select aria-label="Khoảng thời gian" className="rounded-lg border border-slate-200 bg-white px-2.5 py-2 text-[10px] font-medium text-slate-600 outline-none"><option>6 tháng gần đây</option><option>12 tháng gần đây</option></select></div>
          <div className="mb-4 flex gap-5 text-[10px] text-slate-500"><span className="flex items-center gap-2"><i className="h-2 w-2 rounded-full bg-cyan-500" />Người dùng</span><span className="flex items-center gap-2"><i className="h-2 w-2 rounded-full bg-indigo-400" />Tin tuyển dụng</span></div>
          <div className="relative h-[210px] w-full">
            <div className="absolute inset-0 flex flex-col justify-between text-[9px] text-slate-400"><div className="flex items-center gap-2"><span className="w-6">1.2k</span><i className="h-px flex-1 border-t border-dashed border-slate-200" /></div><div className="flex items-center gap-2"><span className="w-6">900</span><i className="h-px flex-1 border-t border-dashed border-slate-200" /></div><div className="flex items-center gap-2"><span className="w-6">600</span><i className="h-px flex-1 border-t border-dashed border-slate-200" /></div><div className="flex items-center gap-2"><span className="w-6">300</span><i className="h-px flex-1 border-t border-dashed border-slate-200" /></div><div className="flex items-center gap-2"><span className="w-6">0</span><i className="h-px flex-1 border-t border-dashed border-slate-200" /></div></div>
            <svg aria-label="Biểu đồ tăng trưởng sáu tháng" className="absolute inset-y-3 left-9 h-[calc(100%-24px)] w-[calc(100%-40px)] overflow-visible" preserveAspectRatio="none" viewBox="0 0 600 180">
              <defs><linearGradient id="usersFill" x1="0" x2="0" y1="0" y2="1"><stop offset="0%" stopColor="#06b6d4" stopOpacity=".18" /><stop offset="100%" stopColor="#06b6d4" stopOpacity="0" /></linearGradient></defs>
              <path d="M0 145 C45 133 56 135 100 117 S155 119 200 95 S255 105 300 78 S355 85 400 58 S455 63 500 42 S555 48 600 18 V180 H0Z" fill="url(#usersFill)" />
              <path d="M0 145 C45 133 56 135 100 117 S155 119 200 95 S255 105 300 78 S355 85 400 58 S455 63 500 42 S555 48 600 18" fill="none" stroke="#06b6d4" strokeWidth="3" vectorEffect="non-scaling-stroke" />
              <path d="M0 163 C50 154 52 157 100 145 S155 151 200 129 S256 140 300 115 S355 126 400 101 S455 110 500 87 S555 91 600 74" fill="none" stroke="#818cf8" strokeDasharray="5 6" strokeWidth="2.5" vectorEffect="non-scaling-stroke" />
            </svg>
          </div>
          <div className="ml-9 mt-1 flex justify-between text-[9px] text-slate-400"><span>Tháng 12</span><span>Tháng 1</span><span>Tháng 2</span><span>Tháng 3</span><span>Tháng 4</span><span>Tháng 5</span></div>
        </section>

        <section className="rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40 sm:p-6">
          <div className="mb-5 flex items-center justify-between"><div><h2 className="text-sm font-bold text-slate-800">Phân bổ người dùng</h2><p className="mt-1 text-[11px] text-slate-400">Theo nhóm tài khoản</p></div><button aria-label="Tuỳ chọn" className="rounded-lg p-1.5 text-slate-400 hover:bg-slate-50"><Icon name="dots" size={18} /></button></div>
          <div className="flex items-center justify-center py-2">
            <div className="relative grid h-40 w-40 place-items-center rounded-full" style={{ background: 'conic-gradient(#0891b2 0% 68%, #818cf8 68% 91%, #fbbf24 91% 100%)' }}>
              <div className="grid h-[116px] w-[116px] place-items-center rounded-full bg-white text-center"><div><p className="text-2xl font-bold text-slate-900">12.8k</p><p className="text-[10px] text-slate-400">tài khoản</p></div></div>
            </div>
          </div>
          <div className="mt-4 space-y-3">
            {[['Ứng viên', '68%', 'bg-cyan-600'], ['Nhà tuyển dụng', '23%', 'bg-indigo-400'], ['Quản trị viên', '9%', 'bg-amber-400']].map(([label, percent, color]) => <div key={label} className="flex items-center justify-between text-[11px]"><span className="flex items-center gap-2 text-slate-500"><i className={`h-2 w-2 rounded-full ${color}`} />{label}</span><span className="font-semibold text-slate-700">{percent}</span></div>)}
          </div>
        </section>
      </div>

      <TableCard title="Tin tuyển dụng mới nhất" subtitle="Các tin vừa được đăng trên nền tảng" action={<button className="text-[11px] font-semibold text-cyan-700 hover:text-cyan-800">Xem tất cả <span aria-hidden="true">→</span></button>}>
        <div className="overflow-x-auto">
          <table className="w-full min-w-[720px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Vị trí tuyển dụng</th><th className="px-4 py-3">Danh mục</th><th className="px-4 py-3">Địa điểm</th><th className="px-4 py-3">Trạng thái</th><th className="px-4 py-3">Ngày đăng</th></tr></thead>
            <tbody className="divide-y divide-slate-100">{filteredJobs.slice(0, 4).map((job) => <JobRow key={job.title} job={job} />)}</tbody>
          </table>
          {filteredJobs.length === 0 && <SearchEmpty query={search} />}
        </div>
      </TableCard>
    </>
  )
}
