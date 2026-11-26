import { useState } from 'react'
import { recruiterApplications, recruiterJobs } from './recruiterData.js'
import { PageHeading, SectionCard } from './RecruiterShared.jsx'

const periods = {
  Tuần: [{ label: 'T2', value: 8 }, { label: 'T3', value: 12 }, { label: 'T4', value: 6 }, { label: 'T5', value: 15 }, { label: 'T6', value: 10 }, { label: 'T7', value: 4 }, { label: 'CN', value: 7 }],
  Tháng: [{ label: 'T1', value: 22 }, { label: 'T2', value: 18 }, { label: 'T3', value: 30 }, { label: 'T4', value: 26 }, { label: 'T5', value: 41 }, { label: 'T6', value: 35 }, { label: 'T7', value: 48 }, { label: 'T8', value: 39 }, { label: 'T9', value: 54 }, { label: 'T10', value: 43 }, { label: 'T11', value: 62 }, { label: 'T12', value: 51 }],
  Năm: [{ label: '2021', value: 120 }, { label: '2022', value: 168 }, { label: '2023', value: 214 }, { label: '2024', value: 286 }, { label: '2025', value: 342 }, { label: '2026', value: 418 }],
}

const statusColors = [
  { name: 'Chờ duyệt', color: '#f59e0b' },
  { name: 'Đang xem xét', color: '#3b82f6' },
  { name: 'Phỏng vấn', color: '#8b5cf6' },
  { name: 'Đã chấp nhận', color: '#22c55e' },
  { name: 'Từ chối', color: '#ef4444' },
]

export default function StatisticsPage() {
  const [period, setPeriod] = useState('Tuần')
  const chartData = periods[period]
  const periodTotal = chartData.reduce((sum, point) => sum + point.value, 0)
  const total = recruiterApplications.length
  const statusCounts = statusColors.map((item) => ({ ...item, value: recruiterApplications.filter((application) => application.status === item.name).length }))
  const chartSegments = statusCounts.reduce((result, item) => {
    const start = result.at(-1)?.end ?? 0
    const end = start + (total ? item.value / total * 100 : 0)
    result.push({ ...item, start, end })
    return result
  }, [])
  const donut = `conic-gradient(${chartSegments.map((item) => `${item.color} ${item.start}% ${item.end}%`).join(', ')})`
  const accepted = statusCounts.find((item) => item.name === 'Đã chấp nhận')?.value ?? 0
  const pending = statusCounts.find((item) => item.name === 'Chờ duyệt')?.value ?? 0
  const maxValue = Math.max(...chartData.map((item) => item.value), 1)

  return (
    <>
      <PageHeading eyebrow="Phân tích dữ liệu" title="Thống kê tuyển dụng" description="Theo dõi số lượng hồ sơ, trạng thái ứng tuyển và hiệu quả tuyển dụng." />
      <SectionCard title="Hiệu quả tuyển dụng" subtitle="Các chỉ số tổng quan của quy trình tuyển dụng">
        <div className="grid gap-4 p-5 sm:grid-cols-2 sm:p-6">
          {[
            { label: 'Tổng đơn nhận được', value: total, color: 'text-slate-900' },
            { label: 'Số đơn được chấp nhận', value: accepted, color: 'text-slate-900' },
            { label: 'Tỷ lệ chấp nhận', value: `${total ? (accepted / total * 100).toFixed(1).replace('.', ',') : '0'}%`, color: 'text-emerald-700' },
            { label: 'Đơn đang chờ duyệt', value: pending, color: 'text-amber-700' },
          ].map((stat) => <article className="rounded-xl border border-slate-100 bg-slate-50/50 p-4" key={stat.label}><p className="text-[11px] text-slate-500">{stat.label}</p><p className={`mt-2 text-2xl font-bold ${stat.color}`}>{stat.value}</p></article>)}
          <p className="text-[10px] text-slate-400 sm:col-span-2">Đang theo dõi {recruiterJobs.length} tin tuyển dụng.</p>
        </div>
      </SectionCard>
      <div className="mb-5 grid gap-5 xl:grid-cols-[1.4fr_1fr]">
        <SectionCard title="Số lượng đơn ứng tuyển" subtitle="Số hồ sơ nhận được theo kỳ đã chọn">
          <div className="p-5 sm:p-6">
            <div className="mb-4 flex items-end justify-between gap-3"><div><p className="text-[10px] text-slate-400">Tổng đơn trong kỳ</p><p className="mt-1 text-2xl font-bold text-slate-900">{periodTotal}</p></div><div className="flex gap-1">{Object.keys(periods).map((item) => <button aria-pressed={period === item} className={`rounded-lg px-3 py-1.5 text-[10px] font-semibold ${period === item ? 'bg-slate-100 text-slate-800' : 'text-slate-500 hover:bg-slate-50'}`} key={item} onClick={() => setPeriod(item)}>{item}</button>)}</div></div>
            <div aria-label={`Biểu đồ số lượng đơn ứng tuyển theo ${period.toLowerCase()}`} className="relative flex h-60 items-end justify-around gap-1 border-b border-slate-100 px-1 pb-0 sm:gap-2">
              <div className="pointer-events-none absolute inset-0 flex flex-col justify-between pb-7 text-[10px] text-slate-400">{[maxValue, Math.round(maxValue * .75), Math.round(maxValue * .5), Math.round(maxValue * .25), 0].map((n, index) => <div className="flex items-center gap-2" key={`${n}-${index}`}><span className="w-7 text-right">{n}</span><span className="flex-1 border-t border-dashed border-slate-200" /></div>)}</div>
              {chartData.map((point) => <div className="z-[1] flex h-full min-w-0 flex-1 flex-col items-center justify-end gap-2" key={point.label}><div className="flex h-[calc(100%-28px)] w-full items-end justify-center"><span className="w-5 max-w-full rounded-t-md bg-cyan-600 transition hover:bg-cyan-700 sm:w-7" style={{ height: `${point.value / maxValue * 100}%` }} title={`${point.value} đơn`} /></div><span className="max-w-full truncate pb-2 text-[9px] text-slate-500 sm:text-[10px]">{point.label}</span></div>)}
            </div>
          </div>
        </SectionCard>
        <SectionCard title="Phân bổ trạng thái đơn ứng tuyển" subtitle={`${total} đơn ứng tuyển`}>
          <div className="flex flex-col items-center gap-6 p-5 sm:flex-row sm:justify-center sm:p-6">
            <div aria-label="Biểu đồ tròn phân bổ trạng thái" className="relative grid h-48 w-48 shrink-0 place-items-center rounded-full" role="img" style={{ background: donut }}>
              <div className="grid h-32 w-32 place-items-center rounded-full bg-white text-center"><div><p className="text-3xl font-bold text-slate-900">{total}</p><p className="text-[11px] text-slate-400">Tổng số đơn</p></div></div>
            </div>
            <div className="w-full space-y-3">{statusCounts.map((item) => <div className="flex items-center justify-between gap-4 text-[11px]" key={item.name}><span className="flex items-center gap-2 text-slate-600"><i className="h-2 w-2 rounded-full" style={{ backgroundColor: item.color }} />{item.name}</span><span className="font-semibold text-slate-700">{item.value}</span></div>)}</div>
          </div>
        </SectionCard>
      </div>
    </>
  )
}
