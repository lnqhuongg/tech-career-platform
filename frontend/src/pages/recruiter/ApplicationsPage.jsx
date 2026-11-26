import { useMemo, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { recruiterApplications, recruiterJobs, applicationStatuses } from './recruiterData.js'
import { FormField, inputClass, Modal, PageHeading, SearchField, SectionCard, StatusPill } from './RecruiterShared.jsx'

export default function ApplicationsPage() {
  const [applications, setApplications] = useState(recruiterApplications)
  const [searchParams] = useSearchParams()
  const [query, setQuery] = useState('')
  const [statusFilter, setStatusFilter] = useState('Tất cả trạng thái')
  const [jobFilter, setJobFilter] = useState(searchParams.get('job') || 'Tất cả công việc')
  const [dateFilter, setDateFilter] = useState('Mọi thời gian')
  const [selected, setSelected] = useState(null)
  const [nextStatus, setNextStatus] = useState('')
  const [note, setNote] = useState('')
  const [notice, setNotice] = useState('')

  const filtered = useMemo(() => applications.filter((application) => {
    const matchesSearch = `${application.candidate} ${application.email} ${application.job}`.toLowerCase().includes(query.toLowerCase())
    const matchesStatus = statusFilter === 'Tất cả trạng thái' || application.status === statusFilter
    const matchesJob = jobFilter === 'Tất cả công việc' || application.jobId === jobFilter
    const [day, month, year] = application.appliedAt.split('/').map(Number)
    const ageDays = Math.floor((new Date(2026, 10, 26) - new Date(year, month - 1, day)) / 86400000)
    return matchesSearch && matchesStatus && matchesJob && (dateFilter === 'Mọi thời gian' || ageDays <= Number(dateFilter))
  }), [applications, query, statusFilter, jobFilter, dateFilter])

  function openApplication(application) {
    setSelected(application)
    setNextStatus(application.status)
    setNote(application.notes)
  }

  function saveApplication() {
    if (!selected) return
    const changed = selected.status !== nextStatus
    setApplications((current) => current.map((application) => application.id === selected.id ? {
      ...application,
      status: nextStatus,
      notes: note,
      history: changed ? [...application.history, `26/11/2026 · Chuyển sang ${nextStatus}`] : application.history,
    } : application))
    setSelected(null)
    setNotice(changed ? 'Đã cập nhật trạng thái và lịch sử hồ sơ.' : 'Đã lưu ghi chú hồ sơ.')
    window.setTimeout(() => setNotice(''), 3000)
  }

  return (
    <>
      <PageHeading eyebrow="Theo dõi tuyển dụng" title="Quản lý đơn ứng tuyển" description="Tìm kiếm, xem hồ sơ và theo dõi tiến trình tuyển dụng của ứng viên." />
      <div className="mb-5 grid grid-cols-2 gap-3 xl:grid-cols-4">{[['Tổng đơn ứng tuyển', applications.length], ['Chờ duyệt', applications.filter((a) => a.status === 'Chờ duyệt').length], ['Đang phỏng vấn', applications.filter((a) => a.status === 'Phỏng vấn').length], ['Đã chấp nhận', applications.filter((a) => a.status === 'Đã chấp nhận').length]].map(([label, value]) => <article className="rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm" key={label}><p className="text-[11px] text-slate-500">{label}</p><p className="mt-1 text-xl font-bold text-slate-800">{value}</p></article>)}</div>
      {notice && <p className="mb-4 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-xs font-medium text-emerald-800" role="status">{notice}</p>}
      <SectionCard title="Danh sách đơn ứng tuyển" subtitle={`${filtered.length} hồ sơ phù hợp`}>
        <div className="flex flex-col gap-3 border-b border-slate-100 p-4 lg:flex-row lg:flex-wrap">
          <SearchField value={query} onChange={setQuery} placeholder="Tìm ứng viên hoặc công việc..." />
          <select aria-label="Lọc trạng thái" className={inputClass + ' lg:w-auto'} value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)}><option>Tất cả trạng thái</option>{applicationStatuses.map((status) => <option key={status}>{status}</option>)}</select>
          <select aria-label="Lọc công việc" className={inputClass + ' lg:w-auto'} value={jobFilter} onChange={(event) => setJobFilter(event.target.value)}><option>Tất cả công việc</option>{recruiterJobs.map((job) => <option key={job.id} value={job.id}>{job.title}</option>)}</select>
          <select aria-label="Lọc thời gian nộp" className={inputClass + ' lg:w-auto'} value={dateFilter} onChange={(event) => setDateFilter(event.target.value)}><option value="Mọi thời gian">Mọi thời gian</option><option value="7">7 ngày gần đây</option><option value="30">30 ngày gần đây</option></select>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full min-w-[850px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Ứng viên</th><th className="px-4 py-3">Vị trí ứng tuyển</th><th className="px-4 py-3">Ngày nộp</th><th className="px-4 py-3">Trạng thái</th><th className="px-4 py-3">Lịch sử</th><th className="px-4 py-3">Thao tác</th></tr></thead>
            <tbody className="divide-y divide-slate-100">{filtered.map((application) => <tr className="hover:bg-slate-50/70" key={application.id}>
              <td className="px-6 py-4"><div className="flex items-center gap-3"><span className={`grid h-9 w-9 place-items-center rounded-xl text-[10px] font-bold ${application.color}`}>{application.initials}</span><div><p className="text-xs font-semibold text-slate-800">{application.candidate}</p><p className="mt-1 text-[10px] text-slate-400">{application.email}</p></div></div></td>
              <td className="px-4 py-4 text-[11px] text-slate-600">{application.job}</td><td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{application.appliedAt}</td><td className="px-4 py-4"><StatusPill status={application.status} /></td><td className="px-4 py-4 text-[11px] text-slate-500">{application.history.length} cập nhật</td>
              <td className="px-4 py-4"><button className="rounded-lg bg-cyan-50 px-3 py-2 text-[10px] font-semibold text-cyan-800 hover:bg-cyan-100" onClick={() => openApplication(application)}>Xem hồ sơ</button></td>
            </tr>)}</tbody>
          </table>
          {filtered.length === 0 && <p className="px-6 py-12 text-center text-sm text-slate-500">Không tìm thấy đơn ứng tuyển phù hợp.</p>}
        </div>
      </SectionCard>
      {selected && <Modal title="Hồ sơ ứng tuyển" onClose={() => setSelected(null)} wide>
        <div className="grid gap-6 md:grid-cols-[1fr_0.9fr]">
          <div className="space-y-4">
            <div className="flex items-center gap-3"><span className={`grid h-12 w-12 place-items-center rounded-xl text-sm font-bold ${selected.color}`}>{selected.initials}</span><div><h3 className="text-sm font-bold text-slate-900">{selected.candidate}</h3><p className="mt-1 text-[11px] text-slate-500">{selected.email}</p></div></div>
            <div className="rounded-xl bg-slate-50 p-3"><p className="text-[10px] font-semibold uppercase text-slate-400">Ứng tuyển vị trí</p><p className="mt-1 text-xs font-semibold text-slate-700">{selected.job}</p><p className="mt-1 text-[10px] text-slate-500">Ngày nộp: {selected.appliedAt}</p></div>
            <div><h4 className="mb-1.5 text-[11px] font-bold text-slate-700">CV</h4><p className="rounded-lg border border-slate-200 px-3 py-2 text-xs text-cyan-800">{selected.cv}</p><p className="mt-1 text-[10px] text-slate-400">Tệp CV sẽ được cung cấp khi kết nối API hồ sơ.</p></div>
            <div><h4 className="mb-1.5 text-[11px] font-bold text-slate-700">Cover letter</h4><p className="text-xs leading-5 text-slate-600">{selected.coverLetter}</p></div>
          </div>
          <div className="space-y-4">
            <FormField label="Cập nhật trạng thái"><select className={inputClass} value={nextStatus} onChange={(event) => setNextStatus(event.target.value)}>{applicationStatuses.map((status) => <option key={status}>{status}</option>)}</select></FormField>
            <FormField label="Ghi chú nội bộ"><textarea className={inputClass} rows={3} value={note} onChange={(event) => setNote(event.target.value)} placeholder="Ghi chú về hồ sơ..." /></FormField>
            <div><h4 className="mb-2 text-[11px] font-bold text-slate-700">Lịch sử trạng thái</h4><ol className="space-y-2 border-l border-slate-200 pl-4">{selected.history.map((event, index) => <li className="relative text-[11px] text-slate-600 before:absolute before:-left-[21px] before:top-1.5 before:h-2 before:w-2 before:rounded-full before:bg-cyan-500" key={`${event}-${index}`}>{event}</li>)}</ol></div>
          </div>
        </div>
        <div className="mt-6 flex justify-end gap-2 border-t border-slate-100 pt-4"><button className="rounded-xl border border-slate-200 px-4 py-2.5 text-xs font-semibold text-slate-600 hover:bg-slate-50" onClick={() => setSelected(null)}>Đóng</button><button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white hover:bg-[#20375c]" onClick={saveApplication}>Lưu cập nhật</button></div>
      </Modal>}
    </>
  )
}
