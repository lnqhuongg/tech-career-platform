import { useState } from 'react'
import { FormField, inputClass, PageHeading } from './RecruiterShared.jsx'

const initialProfile = { name: 'Lê Hoàng Nam', email: 'nam.le@techcorp.vn', phone: '+84 912 345 678', title: 'Talent Acquisition Lead', contact: 'nam.le@techcorp.vn' }

export default function ProfilePage() {
  const [profile, setProfile] = useState(initialProfile)
  const [form, setForm] = useState(initialProfile)
  const [editing, setEditing] = useState(false)
  const [notice, setNotice] = useState('')
  const [password, setPassword] = useState({ current: '', next: '', confirm: '' })

  function saveProfile(event) {
    event.preventDefault()
    setProfile(form)
    setEditing(false)
    setNotice('Thông tin cá nhân đã được cập nhật.')
    window.setTimeout(() => setNotice(''), 3000)
  }

  function savePassword(event) {
    event.preventDefault()
    if (password.next !== password.confirm) {
      setNotice('Mật khẩu xác nhận chưa khớp.')
      return
    }
    setPassword({ current: '', next: '', confirm: '' })
    setNotice('Đổi mật khẩu cần kết nối dịch vụ xác thực.')
    window.setTimeout(() => setNotice(''), 3500)
  }

  return (
    <div className="mx-auto max-w-3xl">
      <PageHeading eyebrow="Tài khoản nhà tuyển dụng" title="Thông tin tài khoản" description="Quản lý thông tin cá nhân và thông tin liên hệ của bạn." action={!editing && <button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white hover:bg-[#20375c]" onClick={() => { setForm(profile); setEditing(true) }}>Chỉnh sửa</button>} />
      {notice && <p className="mb-4 rounded-xl border border-cyan-200 bg-cyan-50 px-4 py-3 text-xs font-medium text-cyan-800" role="status">{notice}</p>}
      <section className="rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40 sm:p-7">
        <div className="mb-5 flex items-center gap-4 border-b border-slate-100 pb-5">
          <div className="grid h-14 w-14 shrink-0 place-items-center rounded-full bg-gradient-to-br from-cyan-500 to-blue-600 text-sm font-bold text-white">HN</div>
          <div><h2 className="text-base font-semibold text-slate-900">{profile.name}</h2><p className="mt-1 text-xs text-slate-500">{profile.title}</p><button className="mt-2 text-[10px] font-semibold text-cyan-700 hover:text-cyan-900" onClick={() => setNotice('Tải ảnh đại diện sẽ khả dụng khi kết nối dịch vụ lưu trữ.')}>Thay ảnh đại diện</button></div>
        </div>
        {editing ? <form className="space-y-4" onSubmit={saveProfile}>
          <div className="grid gap-4 sm:grid-cols-2">
            <FormField label="Họ và tên"><input className={inputClass} required value={form.name} onChange={(event) => setForm({ ...form, name: event.target.value })} /></FormField>
            <FormField label="Chức vụ"><input className={inputClass} value={form.title} onChange={(event) => setForm({ ...form, title: event.target.value })} /></FormField>
            <FormField label="Email tài khoản"><input className={inputClass} type="email" disabled value={form.email} /></FormField>
            <FormField label="Số điện thoại"><input className={inputClass} value={form.phone} onChange={(event) => setForm({ ...form, phone: event.target.value })} /></FormField>
            <FormField label="Thông tin liên hệ" className="sm:col-span-2"><input className={inputClass} value={form.contact} onChange={(event) => setForm({ ...form, contact: event.target.value })} /></FormField>
          </div>
          <div className="flex justify-end gap-2"><button className="rounded-xl border border-slate-200 px-4 py-2.5 text-xs font-semibold text-slate-600 hover:bg-slate-50" onClick={() => setEditing(false)} type="button">Hủy</button><button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white hover:bg-[#20375c]" type="submit">Lưu thay đổi</button></div>
        </form> : <dl className="grid gap-x-8 sm:grid-cols-2">{[['Họ và tên', profile.name], ['Chức vụ', profile.title], ['Email', profile.email], ['Số điện thoại', profile.phone], ['Thông tin liên hệ', profile.contact], ['Vai trò', 'Nhà tuyển dụng']].map(([label, value]) => <div className="border-b border-slate-100 py-4" key={label}><dt className="text-[11px] text-slate-500">{label}</dt><dd className="mt-1 text-xs font-medium text-slate-800">{value}</dd></div>)}</dl>}
      </section>
      <section className="mt-5 rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40 sm:p-7">
        <div className="mb-5"><h2 className="text-sm font-bold text-slate-800">Đổi mật khẩu</h2><p className="mt-1 text-[11px] text-slate-400">Yêu cầu xác thực hiện tại để bảo vệ tài khoản.</p></div>
        <form className="grid gap-4 sm:grid-cols-2" onSubmit={savePassword}>
          <FormField label="Mật khẩu hiện tại"><input autoComplete="current-password" className={inputClass} required type="password" value={password.current} onChange={(event) => setPassword({ ...password, current: event.target.value })} /></FormField>
          <span className="hidden sm:block" />
          <FormField label="Mật khẩu mới"><input autoComplete="new-password" className={inputClass} minLength={8} required type="password" value={password.next} onChange={(event) => setPassword({ ...password, next: event.target.value })} /></FormField>
          <FormField label="Xác nhận mật khẩu mới"><input autoComplete="new-password" className={inputClass} minLength={8} required type="password" value={password.confirm} onChange={(event) => setPassword({ ...password, confirm: event.target.value })} /></FormField>
          <div className="sm:col-span-2"><button className="rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-xs font-semibold text-slate-700 hover:bg-slate-50" type="submit">Cập nhật mật khẩu</button></div>
        </form>
      </section>
    </div>
  )
}
