import { useState } from 'react'

export default function AdminLoginPage() {
  const [passwordVisible, setPasswordVisible] = useState(false)
  const [notice, setNotice] = useState('')

  function handleSubmit(event) {
    event.preventDefault()
    setNotice('Giao diện đăng nhập chưa kết nối với dịch vụ xác thực.')
  }

  return (
    <main className="flex min-h-screen items-center justify-center bg-[#f5f7fb] px-4 py-10 sm:px-6">
      <section className="grid w-full max-w-5xl overflow-hidden rounded-3xl border border-slate-200/80 bg-white shadow-xl shadow-slate-200/60 md:grid-cols-[1fr_0.9fr]">
        <div className="hidden flex-col justify-between bg-[#142544] p-10 text-white md:flex lg:p-12">
          <div className="flex items-center gap-3">
            <span className="grid h-11 w-11 place-items-center rounded-xl bg-cyan-400 text-[#142544]">
              <svg aria-hidden="true" className="h-6 w-6" fill="none" stroke="currentColor" strokeWidth="1.8" viewBox="0 0 24 24">
                <rect height="14" rx="2" width="18" x="3" y="7" />
                <path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M3 12h18M10 12v2h4v-2" />
              </svg>
            </span>
            <span>
              <span className="block text-lg font-bold tracking-tight">TechCareer</span>
              <span className="block text-[10px] font-semibold uppercase tracking-[0.18em] text-slate-300">Admin portal</span>
            </span>
          </div>

          <div className="py-12">
            <div className="mb-6 flex h-48 items-end gap-3 rounded-2xl border border-white/10 bg-white/[0.04] p-5">
              {[36, 54, 43, 76, 62, 92, 72].map((height, index) => (
                <span
                  key={index}
                  className={`flex-1 rounded-t-md ${index === 5 ? 'bg-cyan-400' : 'bg-white/20'}`}
                  style={{ height: `${height}%` }}
                />
              ))}
            </div>
            <h1 className="text-2xl font-semibold leading-snug lg:text-[28px]">Quản lý nền tảng tuyển dụng của bạn</h1>
            <p className="mt-3 max-w-sm text-sm leading-relaxed text-slate-300">
              Đăng nhập vào không gian quản trị TechCareer để theo dõi hoạt động và quản lý hệ thống.
            </p>
          </div>

          <p className="text-[11px] text-slate-400">© 2026 TechCareer Platform</p>
        </div>

        <div className="flex items-center justify-center p-6 sm:p-10 lg:p-12">
          <div className="w-full max-w-sm">
            <div className="mb-8 md:hidden">
              <div className="flex items-center gap-3">
                <span className="grid h-10 w-10 place-items-center rounded-xl bg-cyan-50 text-cyan-700">
                  <svg aria-hidden="true" className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="1.8" viewBox="0 0 24 24">
                    <rect height="14" rx="2" width="18" x="3" y="7" />
                    <path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M3 12h18M10 12v2h4v-2" />
                  </svg>
                </span>
                <span>
                  <span className="block text-base font-bold text-slate-900">TechCareer</span>
                  <span className="block text-[9px] font-semibold uppercase tracking-[0.18em] text-slate-400">Admin portal</span>
                </span>
              </div>
            </div>

            <h2 className="mt-2 text-2xl font-bold tracking-tight text-slate-900">Đăng nhập</h2>
            <p className="mt-2 text-sm text-slate-500">Nhập thông tin tài khoản quản trị của bạn.</p>

            <form className="mt-8 space-y-5" onSubmit={handleSubmit}>
              <div>
                <label className="mb-2 block text-xs font-semibold text-slate-700" htmlFor="admin-email">Email</label>
                <input
                  autoComplete="username"
                  className="h-11 w-full rounded-xl border border-slate-200 bg-white px-3.5 text-sm text-slate-800 outline-none transition placeholder:text-slate-400 focus:border-cyan-600 focus:ring-4 focus:ring-cyan-600/10"
                  id="admin-email"
                  name="email"
                  placeholder="admin@techcareer.vn"
                  required
                  type="email"
                />
              </div>

              <div>
                <label className="mb-2 block text-xs font-semibold text-slate-700" htmlFor="admin-password">Mật khẩu</label>
                <div className="relative">
                  <input
                    autoComplete="current-password"
                    className="h-11 w-full rounded-xl border border-slate-200 bg-white px-3.5 pr-16 text-sm text-slate-800 outline-none transition placeholder:text-slate-400 focus:border-cyan-600 focus:ring-4 focus:ring-cyan-600/10"
                    id="admin-password"
                    name="password"
                    placeholder="Nhập mật khẩu"
                    required
                    type={passwordVisible ? 'text' : 'password'}
                  />
                  <button
                    aria-label={passwordVisible ? 'Ẩn mật khẩu' : 'Hiện mật khẩu'}
                    className="absolute inset-y-0 right-3 text-[11px] font-medium text-slate-500 hover:text-slate-700"
                    onClick={() => setPasswordVisible((visible) => !visible)}
                    type="button"
                  >
                    {passwordVisible ? 'Ẩn' : 'Hiện'}
                  </button>
                </div>
              </div>

              <label className="flex items-center gap-2 text-xs text-slate-600">
                <input className="h-4 w-4 rounded border-slate-300 accent-cyan-700" type="checkbox" />
                Ghi nhớ đăng nhập
              </label>

              {notice && (
                <p className="rounded-lg border border-amber-200 bg-amber-50 px-3 py-2.5 text-xs leading-relaxed text-amber-800" role="status">
                  {notice}
                </p>
              )}

              <button
                className="h-11 w-full rounded-xl bg-[#142544] text-sm font-semibold text-white shadow-sm transition hover:bg-[#20375c] focus:outline-none focus:ring-4 focus:ring-slate-900/10"
                type="submit"
              >
                Đăng nhập hệ thống
              </button>
            </form>

            <p className="mt-7 text-center text-[11px] leading-relaxed text-slate-400">
              Đây là trang đăng nhập dành riêng cho quản trị viên.
            </p>
          </div>
        </div>
      </section>
    </main>
  )
}
