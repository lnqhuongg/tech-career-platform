import { useState } from 'react'
import { recruiterCompany } from './recruiterData.js'
import { FormField, inputClass, PageHeading } from './RecruiterShared.jsx'

export default function CompanyPage() {
  const [company, setCompany] = useState(recruiterCompany)
  const [form, setForm] = useState(recruiterCompany)
  const [editing, setEditing] = useState(false)
  const [notice, setNotice] = useState('')

  function saveCompany(event) {
    event.preventDefault()
    setCompany(form)
    setEditing(false)
    setNotice('Thông tin công ty đã được cập nhật.')
    window.setTimeout(() => setNotice(''), 3000)
  }

  return (
    <>
      <PageHeading eyebrow="Hồ sơ doanh nghiệp" title="Thông tin công ty" description="Cập nhật thông tin doanh nghiệp hiển thị trên trang tuyển dụng." action={!editing && <button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white hover:bg-[#20375c]" onClick={() => { setForm(company); setEditing(true) }}>Chỉnh sửa thông tin</button>} />
      {notice && <p className="mb-4 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-xs font-medium text-emerald-800" role="status">{notice}</p>}
      {editing ? (
        <form className="mx-auto max-w-3xl rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm sm:p-7" onSubmit={saveCompany}>
          <div className="mb-6 flex items-center gap-4 border-b border-slate-100 pb-5"><span className="grid h-14 w-14 place-items-center overflow-hidden rounded-2xl bg-blue-50 text-lg font-bold text-blue-700">{form.logo ? <img alt="Logo công ty" className="h-full w-full object-cover" src={form.logo} /> : 'V'}</span><div><h2 className="text-sm font-bold text-slate-800">Logo công ty</h2><p className="mt-1 text-[10px] text-slate-400">Nhập URL ảnh logo và ảnh bìa của công ty.</p></div></div>
          <div className="grid gap-4 sm:grid-cols-2">
            <FormField label="Tên công ty"><input className={inputClass} required value={form.name} onChange={(event) => setForm({ ...form, name: event.target.value })} /></FormField>
            <FormField label="Lĩnh vực hoạt động"><input className={inputClass} value={form.industry} onChange={(event) => setForm({ ...form, industry: event.target.value })} /></FormField>
            <FormField label="Quy mô nhân sự"><input className={inputClass} value={form.size} onChange={(event) => setForm({ ...form, size: event.target.value })} /></FormField>
            <FormField label="Địa chỉ"><input className={inputClass} value={form.address} onChange={(event) => setForm({ ...form, address: event.target.value })} /></FormField>
            <FormField label="Email liên hệ"><input className={inputClass} type="email" value={form.email} onChange={(event) => setForm({ ...form, email: event.target.value })} /></FormField>
            <FormField label="Số điện thoại"><input className={inputClass} value={form.phone} onChange={(event) => setForm({ ...form, phone: event.target.value })} /></FormField>
            <FormField label="Website"><input className={inputClass} type="url" value={form.website} onChange={(event) => setForm({ ...form, website: event.target.value })} /></FormField>
            <FormField label="Logo URL"><input className={inputClass} type="url" value={form.logo ?? ''} onChange={(event) => setForm({ ...form, logo: event.target.value })} placeholder="https://" /></FormField>
            <FormField label="Ảnh bìa URL" className="sm:col-span-2"><input className={inputClass} type="url" value={form.cover ?? ''} onChange={(event) => setForm({ ...form, cover: event.target.value })} placeholder="https://" /></FormField>
            <FormField label="Giới thiệu công ty" className="sm:col-span-2"><textarea className={inputClass} rows={4} value={form.description} onChange={(event) => setForm({ ...form, description: event.target.value })} /></FormField>
            <FormField label="Văn hóa làm việc" className="sm:col-span-2"><textarea className={inputClass} rows={3} value={form.culture} onChange={(event) => setForm({ ...form, culture: event.target.value })} /></FormField>
            <FormField label="Phúc lợi" className="sm:col-span-2"><textarea className={inputClass} rows={3} value={form.benefits} onChange={(event) => setForm({ ...form, benefits: event.target.value })} /></FormField>
          </div>
          <div className="mt-6 flex justify-end gap-2"><button className="rounded-xl border border-slate-200 px-4 py-2.5 text-xs font-semibold text-slate-600 hover:bg-slate-50" onClick={() => setEditing(false)} type="button">Hủy</button><button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white hover:bg-[#20375c]" type="submit">Lưu thay đổi</button></div>
        </form>
      ) : (
        <div className="mx-auto max-w-3xl overflow-hidden rounded-2xl border border-slate-200/80 bg-white shadow-sm">
          <div className="h-36 bg-gradient-to-r from-cyan-700 via-blue-700 to-indigo-800" style={company.cover ? { backgroundImage: `linear-gradient(#0f172a33, #0f172a33), url("${company.cover}")`, backgroundPosition: 'center', backgroundSize: 'cover' } : undefined} />
          <div className="px-5 pb-6 sm:px-7">
            <div className="-mt-9 mb-4 grid h-[72px] w-[72px] place-items-center overflow-hidden rounded-2xl border-4 border-white bg-blue-50 text-2xl font-bold text-blue-700 shadow-sm">{company.logo ? <img alt="Logo công ty" className="h-full w-full object-cover" src={company.logo} /> : 'V'}</div>
            <div className="border-b border-slate-100 pb-5"><h2 className="text-xl font-bold text-slate-900">{company.name}</h2><p className="mt-1 text-xs text-slate-500">{company.industry} · {company.size}</p></div>
            <div className="grid gap-6 py-5 sm:grid-cols-2">
              <div className="sm:col-span-2"><h3 className="mb-2 text-xs font-bold text-slate-700">Giới thiệu</h3><p className="text-xs leading-5 text-slate-600">{company.description}</p></div>
              <div><h3 className="mb-2 text-xs font-bold text-slate-700">Địa chỉ</h3><p className="text-xs text-slate-600">{company.address}</p></div>
              <div><h3 className="mb-2 text-xs font-bold text-slate-700">Liên hệ</h3><p className="text-xs leading-5 text-slate-600">{company.email}<br />{company.phone}<br />{company.website}</p></div>
              <div><h3 className="mb-2 text-xs font-bold text-slate-700">Văn hóa</h3><p className="text-xs leading-5 text-slate-600">{company.culture}</p></div>
              <div><h3 className="mb-2 text-xs font-bold text-slate-700">Phúc lợi</h3><p className="text-xs leading-5 text-slate-600">{company.benefits}</p></div>
            </div>
          </div>
        </div>
      )}
    </>
  )
}
