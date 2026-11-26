import { useMemo, useState } from 'react'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { recruiterJobs } from './recruiterData.js'
import { FormField, inputClass, Modal, PageHeading, SearchField, SectionCard, StatusPill } from './RecruiterShared.jsx'

const emptyJob = { title: '', category: '', location: '', salary: '', description: '', requirements: '' }

export default function JobsPage() {
  const [jobs, setJobs] = useState(recruiterJobs)
  const [query, setQuery] = useState('')
  const [statusFilter, setStatusFilter] = useState('Tất cả trạng thái')
  const [dateFilter, setDateFilter] = useState('Mọi thời gian')
  const [editingJob, setEditingJob] = useState(null)
  const [viewingJob, setViewingJob] = useState(null)
  const [form, setForm] = useState(emptyJob)
  const [notice, setNotice] = useState('')

  const filteredJobs = useMemo(() => jobs.filter((job) => {
    const matchesText = `${job.title} ${job.category} ${job.location}`.toLowerCase().includes(query.toLowerCase())
    const matchesStatus = statusFilter === 'Tất cả trạng thái' || job.status === statusFilter
    const [day, month, year] = job.posted.split('/').map(Number)
    const ageDays = Math.floor((new Date(2026, 10, 26) - new Date(year, month - 1, day)) / 86400000)
    const matchesDate = dateFilter === 'Mọi thời gian' || ageDays <= Number(dateFilter)
    return matchesText && matchesStatus && matchesDate
  }), [jobs, query, statusFilter, dateFilter])

  function openForm(job = null) {
    setEditingJob(job ?? {})
    setForm(job ? { ...job } : emptyJob)
  }

  function saveJob(event) {
    event.preventDefault()
    if (!form.title.trim() || !form.category.trim() || !form.location.trim()) return
    if (editingJob?.id) {
      setJobs((current) => current.map((job) => job.id === editingJob.id ? { ...job, ...form } : job))
      setNotice('Đã cập nhật tin tuyển dụng.')
    } else {
      setJobs((current) => [{ ...form, id: `job-${Date.now()}`, status: 'Đang tuyển', posted: '26/11/2026', applications: 0 }, ...current])
      setNotice('Đã tạo tin tuyển dụng mới.')
    }
    setEditingJob(null)
    window.setTimeout(() => setNotice(''), 3000)
  }

  function toggleJob(job) {
    const status = job.status === 'Đang tuyển' ? 'Đã đóng' : 'Đang tuyển'
    setJobs((current) => current.map((item) => item.id === job.id ? { ...item, status } : item))
    setNotice(status === 'Đã đóng' ? 'Tin tuyển dụng đã được đóng.' : 'Tin tuyển dụng đã được mở lại.')
    window.setTimeout(() => setNotice(''), 3000)
  }

  return (
    <>
      <PageHeading eyebrow="Quản lý tuyển dụng" title="Tin tuyển dụng" description="Quản lý các cơ hội việc làm và theo dõi hồ sơ ứng tuyển." action={<button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]" onClick={() => openForm()}>+ Đăng tin tuyển dụng</button>} />
      {notice && <p className="mb-4 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-xs font-medium text-emerald-800" role="status">{notice}</p>}
      <div className="mb-5 grid grid-cols-2 gap-3 sm:grid-cols-4">
        {[['Tất cả tin', jobs.length], ['Đang tuyển', jobs.filter((job) => job.status === 'Đang tuyển').length], ['Đã đóng', jobs.filter((job) => job.status === 'Đã đóng').length], ['Tổng hồ sơ', jobs.reduce((sum, job) => sum + job.applications, 0)]].map(([label, count]) => <article className="rounded-xl border border-slate-200/80 bg-white px-4 py-3 shadow-sm" key={label}><p className="text-[10px] text-slate-500">{label}</p><p className="mt-1 text-xl font-bold text-slate-800">{count}</p></article>)}
      </div>
      <SectionCard title="Danh sách tin tuyển dụng" subtitle={`${filteredJobs.length} tin trong danh sách`} action={<Icon name="briefcase" size={18} className="text-slate-400" />}>
        <div className="flex flex-col gap-3 border-b border-slate-100 p-4 sm:flex-row sm:flex-wrap sm:items-center">
          <SearchField value={query} onChange={setQuery} placeholder="Tìm theo tên công việc..." />
          <select aria-label="Lọc trạng thái" className={inputClass + ' sm:w-auto'} value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)}><option>Tất cả trạng thái</option><option>Đang tuyển</option><option>Đã đóng</option></select>
          <select aria-label="Lọc thời gian đăng" className={inputClass + ' sm:w-auto'} value={dateFilter} onChange={(event) => setDateFilter(event.target.value)}><option value="Mọi thời gian">Mọi thời gian</option><option value="7">7 ngày gần đây</option><option value="30">30 ngày gần đây</option></select>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full min-w-[880px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Vị trí tuyển dụng</th><th className="px-4 py-3">Mức lương</th><th className="px-4 py-3">Địa điểm</th><th className="px-4 py-3">Trạng thái</th><th className="px-4 py-3">Ngày đăng</th><th className="px-4 py-3">Đơn ứng tuyển</th><th className="px-4 py-3">Thao tác</th></tr></thead>
            <tbody className="divide-y divide-slate-100">{filteredJobs.map((job) => <tr className="hover:bg-slate-50/70" key={job.id}>
              <td className="px-6 py-4"><p className="text-xs font-semibold text-slate-800">{job.title}</p><p className="mt-1 text-[10px] text-slate-400">{job.category}</p></td>
              <td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-600">{job.salary || 'Thỏa thuận'}</td><td className="px-4 py-4 text-[11px] text-slate-500">{job.location}</td><td className="px-4 py-4"><StatusPill status={job.status} /></td><td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{job.posted}</td>
              <td className="px-4 py-4"><a className="text-[11px] font-semibold text-cyan-700 hover:text-cyan-900" href={`/recruiter/applications?job=${encodeURIComponent(job.id)}`}>{job.applications} hồ sơ</a></td>
              <td className="px-4 py-4"><div className="flex items-center gap-1"><button className="rounded-lg px-2 py-1.5 text-[10px] font-semibold text-cyan-700 hover:bg-cyan-50" onClick={() => setViewingJob(job)}>Chi tiết</button><button className="rounded-lg px-2 py-1.5 text-[10px] font-semibold text-slate-600 hover:bg-slate-100" onClick={() => openForm(job)}>Sửa</button><button className="rounded-lg px-2 py-1.5 text-[10px] font-semibold text-slate-600 hover:bg-slate-100" onClick={() => toggleJob(job)}>{job.status === 'Đang tuyển' ? 'Đóng' : 'Mở lại'}</button></div></td>
            </tr>)}</tbody>
          </table>
          {filteredJobs.length === 0 && <p className="px-6 py-12 text-center text-sm text-slate-500">Không tìm thấy tin tuyển dụng phù hợp.</p>}
        </div>
      </SectionCard>
      {editingJob && <Modal title={editingJob.id ? 'Chỉnh sửa tin tuyển dụng' : 'Đăng tin tuyển dụng'} onClose={() => setEditingJob(null)} wide>
        <form className="space-y-4" onSubmit={saveJob}>
          <div className="grid gap-4 sm:grid-cols-2">
            <FormField label="Tên công việc *"><input className={inputClass} required value={form.title} onChange={(event) => setForm({ ...form, title: event.target.value })} /></FormField>
            <FormField label="Danh mục *"><input className={inputClass} required value={form.category} onChange={(event) => setForm({ ...form, category: event.target.value })} placeholder="Ví dụ: Phát triển phần mềm" /></FormField>
            <FormField label="Địa điểm *"><input className={inputClass} required value={form.location} onChange={(event) => setForm({ ...form, location: event.target.value })} /></FormField>
            <FormField label="Mức lương"><input className={inputClass} value={form.salary} onChange={(event) => setForm({ ...form, salary: event.target.value })} placeholder="Ví dụ: 20 - 30 triệu" /></FormField>
          </div>
          <FormField label="Mô tả công việc"><textarea className={inputClass} rows={3} value={form.description} onChange={(event) => setForm({ ...form, description: event.target.value })} /></FormField>
          <FormField label="Yêu cầu ứng viên"><textarea className={inputClass} rows={3} value={form.requirements} onChange={(event) => setForm({ ...form, requirements: event.target.value })} /></FormField>
          <div className="flex justify-end gap-2 pt-2"><button className="rounded-xl border border-slate-200 px-4 py-2.5 text-xs font-semibold text-slate-600 hover:bg-slate-50" onClick={() => setEditingJob(null)} type="button">Hủy</button><button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white hover:bg-[#20375c]" type="submit">{editingJob.id ? 'Lưu thay đổi' : 'Đăng tin'}</button></div>
        </form>
      </Modal>}
      {viewingJob && <Modal title="Chi tiết tin tuyển dụng" onClose={() => setViewingJob(null)}>
        <div className="space-y-4"><div><h3 className="text-lg font-bold text-slate-900">{viewingJob.title}</h3><p className="mt-1 text-xs text-slate-500">{viewingJob.category} · {viewingJob.location}</p></div><div className="flex flex-wrap items-center gap-3"><StatusPill status={viewingJob.status} /><span className="text-xs text-slate-500">{viewingJob.salary || 'Thỏa thuận'}</span><span className="text-xs text-slate-500">{viewingJob.applications} hồ sơ</span></div><div><h4 className="mb-1 text-xs font-bold text-slate-700">Mô tả</h4><p className="text-xs leading-5 text-slate-600">{viewingJob.description || 'Chưa có mô tả.'}</p></div><div><h4 className="mb-1 text-xs font-bold text-slate-700">Yêu cầu</h4><p className="text-xs leading-5 text-slate-600">{viewingJob.requirements || 'Chưa có yêu cầu.'}</p></div></div>
      </Modal>}
    </>
  )
}
