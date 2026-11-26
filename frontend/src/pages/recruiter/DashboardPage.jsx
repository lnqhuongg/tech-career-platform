import { Link } from 'react-router-dom'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { recruiterApplications, recruiterJobs } from './recruiterData.js'
import { PageHeading, SectionCard, StatusPill } from './RecruiterShared.jsx'

const recentChart = [
  { label: 'T2', value: 8 }, { label: 'T3', value: 12 }, { label: 'T4', value: 6 },
  { label: 'T5', value: 15 }, { label: 'T6', value: 10 }, { label: 'T7', value: 4 }, { label: 'CN', value: 7 },
]

export default function DashboardPage() {
  const openJobs = recruiterJobs.filter((job) => job.status === 'Đang tuyển').length
  const closedJobs = recruiterJobs.filter((job) => job.status === 'Đã đóng').length
  const pending = recruiterApplications.filter((application) => application.status === 'Chờ duyệt').length
  const interviewing = recruiterApplications.filter((application) => application.status === 'Phỏng vấn').length
  const accepted = recruiterApplications.filter((application) => application.status === 'Đã chấp nhận').length
  const stats = [
    { label: 'Tin tuyển dụng đã đăng', value: recruiterJobs.length, icon: 'briefcase', color: 'bg-blue-50 text-blue-600' },
    { label: 'Tin đang mở', value: openJobs, icon: 'grid', color: 'bg-emerald-50 text-emerald-600' },
    { label: 'Tin đã đóng', value: closedJobs, icon: 'file', color: 'bg-slate-100 text-slate-600' },
    { label: 'Tổng đơn ứng tuyển', value: recruiterApplications.length, icon: 'users', color: 'bg-violet-50 text-violet-600' },
    { label: 'Chờ xét duyệt', value: pending, icon: 'bell', color: 'bg-amber-50 text-amber-600' },
    { label: 'Đang phỏng vấn', value: interviewing, icon: 'user', color: 'bg-cyan-50 text-cyan-700' },
    { label: 'Được chấp nhận', value: accepted, icon: 'arrowUp', color: 'bg-emerald-50 text-emerald-600' },
  ]

  return (
    <>
      <PageHeading eyebrow="Không gian tuyển dụng" title="Tổng quan" description="Chào mừng trở lại, Nam. Đây là tình hình tuyển dụng của công ty bạn." action={<Link className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]" to="/recruiter/jobs">+ Đăng tin tuyển dụng</Link>} />
      <div className="mb-6 grid grid-cols-1 gap-3 sm:grid-cols-2 xl:grid-cols-4">
        {stats.map((stat) => <article key={stat.label} className="rounded-2xl border border-slate-200/80 bg-white p-4 shadow-sm shadow-slate-200/40"><div className="flex items-start justify-between"><p className="text-[11px] font-medium text-slate-500">{stat.label}</p><span className={`grid h-8 w-8 place-items-center rounded-lg ${stat.color}`}><Icon name={stat.icon} size={16} /></span></div><p className="mt-3 text-2xl font-bold tracking-tight text-slate-900">{stat.value}</p></article>)}
      </div>
      <div className="mb-6 grid gap-5 xl:grid-cols-[1.4fr_1fr]">
        <SectionCard title="Đơn ứng tuyển trong 7 ngày gần nhất" subtitle="Số lượng hồ sơ nhận được theo ngày">
          <div className="p-5 sm:p-6">
            <div className="mb-4 flex items-center gap-2"><span className="text-3xl font-bold text-slate-900">{recentChart.reduce((total, point) => total + point.value, 0)}</span><span className="text-[11px] text-slate-400">đơn ứng tuyển</span></div>
            <div aria-label="Biểu đồ cột đơn ứng tuyển trong 7 ngày gần nhất" className="relative flex h-48 items-end justify-around gap-2 border-b border-slate-100 px-2 pb-0">
              <div className="pointer-events-none absolute inset-0 flex flex-col justify-between pb-7 text-[10px] text-slate-400">{[20, 15, 10, 5, 0].map((n) => <div className="flex items-center gap-2" key={n}><span className="w-5 text-right">{n}</span><span className="flex-1 border-t border-dashed border-slate-200" /></div>)}</div>
              {recentChart.map((point) => <div className="z-[1] flex h-full flex-1 flex-col items-center justify-end gap-2" key={point.label}><div className="flex h-[calc(100%-28px)] w-full items-end justify-center"><span className="w-7 max-w-full rounded-t-md bg-cyan-600 transition hover:bg-cyan-700" style={{ height: `${point.value / 20 * 100}%` }} title={`${point.value} đơn`} /></div><span className="pb-2 text-[10px] text-slate-500">{point.label}</span></div>)}
            </div>
          </div>
        </SectionCard>
        <SectionCard title="Đơn cần theo dõi" subtitle="Tình trạng xử lý hồ sơ hiện tại" action={<Link className="text-[10px] font-semibold text-cyan-700 hover:text-cyan-800" to="/recruiter/applications">Quản lý đơn →</Link>}>
          <div className="space-y-4 p-5">
            {[['Chờ xét duyệt', pending, 'bg-amber-500'], ['Đang xem xét', recruiterApplications.filter((a) => a.status === 'Đang xem xét').length, 'bg-blue-500'], ['Phỏng vấn', interviewing, 'bg-violet-500']].map(([label, amount, color]) => <div className="flex items-center justify-between" key={label}><span className="flex items-center gap-2 text-xs text-slate-600"><i className={`h-2 w-2 rounded-full ${color}`} />{label}</span><span className="text-sm font-bold text-slate-800">{amount}</span></div>)}
            <div className="border-t border-slate-100 pt-4"><p className="mb-3 text-[10px] font-semibold uppercase tracking-wide text-slate-400">Tin đang tuyển</p>{recruiterJobs.filter((job) => job.status === 'Đang tuyển').slice(0, 3).map((job) => <div className="flex items-center justify-between gap-3 py-2" key={job.id}><span className="truncate text-[11px] font-medium text-slate-700">{job.title}</span><span className="shrink-0 text-[10px] text-slate-500">{job.applications} đơn</span></div>)}</div>
          </div>
        </SectionCard>
      </div>
      <SectionCard title="Đơn ứng tuyển gần đây" subtitle="Các hồ sơ mới nhận được" action={<Link className="text-[10px] font-semibold text-cyan-700 hover:text-cyan-800" to="/recruiter/applications">Xem tất cả →</Link>}>
        <div className="overflow-x-auto">
          <table className="w-full min-w-[680px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Ứng viên</th><th className="px-4 py-3">Vị trí</th><th className="px-4 py-3">Ngày nộp</th><th className="px-4 py-3">Trạng thái</th></tr></thead>
            <tbody className="divide-y divide-slate-100">{recruiterApplications.slice(0, 5).map((application) => <tr className="hover:bg-slate-50/70" key={application.id}><td className="px-6 py-3.5"><div className="flex items-center gap-3"><span className={`grid h-9 w-9 place-items-center rounded-xl text-[10px] font-bold ${application.color}`}>{application.initials}</span><div><p className="text-xs font-semibold text-slate-800">{application.candidate}</p><p className="mt-0.5 text-[10px] text-slate-400">{application.email}</p></div></div></td><td className="px-4 py-3.5 text-[11px] text-slate-600">{application.job}</td><td className="px-4 py-3.5 text-[11px] text-slate-500">{application.appliedAt}</td><td className="px-4 py-3.5"><StatusPill status={application.status} /></td></tr>)}</tbody>
          </table>
        </div>
      </SectionCard>
    </>
  )
}
