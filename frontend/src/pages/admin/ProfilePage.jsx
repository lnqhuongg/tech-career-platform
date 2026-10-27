import { PageHeading } from './components/AdminShared.jsx'

const profileDetails = [
  { label: 'Họ và tên', value: 'Admin' },
  { label: 'Email', value: 'admin@techcareer.vn' },
  { label: 'Vai trò', value: 'Quản trị viên' },
  { label: 'Trạng thái', value: 'Đang hoạt động' },
]

export default function ProfilePage() {
  return (
    <div className="mx-auto max-w-2xl">
      <PageHeading
        eyebrow="Tài khoản quản trị"
        title="Thông tin tài khoản"
        description="Thông tin hồ sơ quản trị viên hiện tại."
      />

      <section className="rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40 sm:p-7">
        <div className="flex items-center gap-4 border-b border-slate-100 pb-5">
          <div className="grid h-14 w-14 shrink-0 place-items-center rounded-full bg-slate-100 text-sm font-semibold text-slate-600">
            AD
          </div>
          <div>
            <h2 className="text-base font-semibold text-slate-900">Admin</h2>
            <p className="mt-1 text-xs text-slate-500">Quản trị viên</p>
          </div>
        </div>

        <dl className="divide-y divide-slate-100">
          {profileDetails.map((detail) => (
            <div key={detail.label} className="grid gap-1 py-4 sm:grid-cols-[140px_1fr] sm:gap-4">
              <dt className="text-xs text-slate-500">{detail.label}</dt>
              <dd className="text-sm font-medium text-slate-800">{detail.value}</dd>
            </div>
          ))}
        </dl>
      </section>
    </div>
  )
}
