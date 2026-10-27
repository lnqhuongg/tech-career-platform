import { statCards } from './adminData.js'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { PageHeading, TableCard } from './components/AdminShared.jsx'

export default function StatisticsPage() {
  return (
    <>
      <PageHeading eyebrow="Phân tích dữ liệu" title="Báo cáo & thống kê" description="Tổng hợp chỉ số hoạt động và hiệu quả tuyển dụng." action={<button className="rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-xs font-semibold text-slate-700 shadow-sm hover:bg-slate-50">↓ Xuất báo cáo</button>} />
      <div className="mb-6 flex gap-2 overflow-x-auto pb-1">{['7 ngày qua', '30 ngày qua', '90 ngày qua', 'Tuỳ chỉnh'].map((period, index) => <button key={period} className={`whitespace-nowrap rounded-xl border px-4 py-2.5 text-[11px] font-semibold ${index === 1 ? 'border-[#142544] bg-[#142544] text-white' : 'border-slate-200 bg-white text-slate-500 hover:bg-slate-50'}`}>{period}</button>)}</div>
      <div className="mb-6 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">{statCards.map((stat) => <article key={stat.label} className="rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm"><div className="flex items-center justify-between"><p className="text-xs text-slate-500">{stat.label}</p><span className={`grid h-9 w-9 place-items-center rounded-xl ${stat.color}`}><Icon name={stat.icon} size={18} /></span></div><p className="mt-4 text-[27px] font-bold tracking-tight text-slate-900">{stat.value}</p><p className="mt-1 text-[10px]"><span className="font-bold text-emerald-600">↑ {stat.change}</span><span className="ml-1 text-slate-400">so với tháng trước</span></p></article>)}</div>
      <div className="grid gap-5 xl:grid-cols-[1.5fr_1fr]">
        <TableCard title="Hiệu quả tuyển dụng" subtitle="Số lượng tin đăng và lượt ứng tuyển theo tháng">
          <div className="p-5 sm:p-6"><div className="mb-5 flex gap-5 text-[10px] text-slate-500"><span className="flex items-center gap-2"><i className="h-2 w-2 rounded-full bg-cyan-500" />Tin tuyển dụng</span><span className="flex items-center gap-2"><i className="h-2 w-2 rounded-full bg-indigo-400" />Lượt ứng tuyển</span></div><div className="flex h-[260px] items-end justify-around gap-3 border-b border-slate-100 px-2">{[['T12', 45, 70], ['T1', 55, 62], ['T2', 48, 82], ['T3', 68, 72], ['T4', 60, 92], ['T5', 78, 86]].map(([label, first, second]) => <div key={label} className="flex h-full flex-1 flex-col items-center justify-end gap-3"><div className="flex h-[88%] w-full items-end justify-center gap-1 sm:gap-2"><span className="w-3 rounded-t-md bg-cyan-500 sm:w-5" style={{ height: `${first}%` }} /><span className="w-3 rounded-t-md bg-indigo-300 sm:w-5" style={{ height: `${second}%` }} /></div><span className="mb-2 text-[10px] text-slate-400">{label}</span></div>)}</div></div>
        </TableCard>
        <TableCard title="Danh mục được quan tâm" subtitle="Tỷ lệ tin tuyển dụng theo lĩnh vực">
          <div className="space-y-5 p-5 sm:p-6">{[['Phát triển phần mềm', 82, 'bg-blue-500'], ['Dữ liệu & AI', 64, 'bg-violet-500'], ['Thiết kế', 48, 'bg-pink-500'], ['Hạ tầng & DevOps', 39, 'bg-cyan-500'], ['Sản phẩm & Quản lý', 27, 'bg-amber-500']].map(([name, value, color]) => <div key={name}><div className="mb-2 flex justify-between text-[11px]"><span className="font-medium text-slate-600">{name}</span><span className="font-semibold text-slate-700">{value}%</span></div><div className="h-2 overflow-hidden rounded-full bg-slate-100"><div className={`h-full rounded-full ${color}`} style={{ width: `${value}%` }} /></div></div>)}</div>
        </TableCard>
      </div>
    </>
  )
}
