import { useEffect, useRef, useState } from 'react'
import { NavLink, Outlet, useLocation } from 'react-router-dom'
import { Icon } from './AdminLayout.jsx'

const navigation = [
  { path: '/recruiter/dashboard', label: 'Tổng quan', icon: 'grid' },
  { path: '/recruiter/jobs', label: 'Tin tuyển dụng', icon: 'briefcase' },
  { path: '/recruiter/applications', label: 'Đơn ứng tuyển', icon: 'file' },
  { path: '/recruiter/company', label: 'Thông tin công ty', icon: 'building' },
  { path: '/recruiter/statistics', label: 'Thống kê', icon: 'chart' },
]

export default function RecruiterLayout() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [accountMenuOpen, setAccountMenuOpen] = useState(false)
  const accountMenuRef = useRef(null)
  const location = useLocation()
  const activeItem = navigation.find((item) => item.path === location.pathname)
    ?? (location.pathname === '/recruiter/profile' ? { label: 'Thông tin tài khoản' } : undefined)

  useEffect(() => {
    const previousTitle = document.title
    document.title = 'Nhà tuyển dụng | TechCareer'

    function handlePointerDown(event) {
      if (!accountMenuRef.current?.contains(event.target)) setAccountMenuOpen(false)
    }

    function handleKeyDown(event) {
      if (event.key === 'Escape') setAccountMenuOpen(false)
    }

    document.addEventListener('pointerdown', handlePointerDown)
    document.addEventListener('keydown', handleKeyDown)
    return () => {
      document.removeEventListener('pointerdown', handlePointerDown)
      document.removeEventListener('keydown', handleKeyDown)
      document.title = previousTitle
    }
  }, [])

  return (
    <div className="min-h-screen bg-[#f5f7fb] text-slate-800">
      {mobileMenuOpen && (
        <button
          aria-label="Đóng menu"
          className="fixed inset-0 z-30 bg-slate-950/40 lg:hidden"
          onClick={() => setMobileMenuOpen(false)}
        />
      )}
      <aside className={`fixed inset-y-0 left-0 z-40 flex w-[240px] flex-col border-r border-slate-200 bg-white text-slate-600 transition-transform duration-300 lg:translate-x-0 ${mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'}`}>
        <div className="flex h-[76px] items-center justify-between border-b border-slate-100 px-5">
          <NavLink className="flex items-center gap-2.5" to="/recruiter/dashboard" onClick={() => setMobileMenuOpen(false)}>
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-cyan-50 text-cyan-700"><Icon name="briefcase" size={20} /></span>
            <span>
              <span className="block text-[16px] font-bold tracking-tight text-slate-800">TechCareer</span>
              <span className="block text-[9px] font-semibold uppercase tracking-[0.18em] text-slate-400">Recruiter portal</span>
            </span>
          </NavLink>
          <button aria-label="Đóng menu" className="rounded-lg p-2 text-slate-500 hover:bg-slate-100 lg:hidden" onClick={() => setMobileMenuOpen(false)}><Icon name="close" size={19} /></button>
        </div>
        <div className="px-3 pt-6">
          <p className="px-3 pb-3 text-[9px] font-bold uppercase tracking-[0.16em] text-slate-400">Không gian tuyển dụng</p>
          <nav aria-label="Điều hướng nhà tuyển dụng" className="space-y-1">
            {navigation.map((item) => (
              <NavLink
                key={item.path}
                to={item.path}
                end
                className={({ isActive }) => `flex w-full items-center gap-2.5 rounded-lg px-3 py-2.5 text-left text-[12px] font-medium transition ${isActive ? 'bg-cyan-50 text-cyan-800' : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'}`}
                onClick={() => setMobileMenuOpen(false)}
              >
                <Icon name={item.icon} size={18} />
                <span>{item.label}</span>
              </NavLink>
            ))}
          </nav>
        </div>
        <div className="mt-auto border-t border-slate-100 p-4">
          <div className="flex items-center gap-3 rounded-xl bg-slate-50 p-3">
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-cyan-100 text-xs font-bold text-cyan-800">HN</span>
            <span className="min-w-0"><span className="block truncate text-xs font-semibold text-slate-800">Lê Hoàng Nam</span><span className="block truncate text-[10px] text-slate-500">VNG Corporation</span></span>
          </div>
        </div>
      </aside>

      <div className="min-h-screen lg:pl-[240px]">
        <header className="sticky top-0 z-20 flex h-[76px] items-center justify-between border-b border-slate-200/80 bg-white/90 px-4 backdrop-blur-md sm:px-7 lg:px-10">
          <div className="flex min-w-0 items-center gap-3">
            <button aria-label="Mở menu" className="rounded-xl border border-slate-200 p-2 text-slate-600 hover:bg-slate-50 lg:hidden" onClick={() => setMobileMenuOpen(true)}><Icon name="menu" size={20} /></button>
            <p className="truncate text-[15px] font-medium text-slate-400">Không gian tuyển dụng <span className="px-1">/</span><span className="text-slate-600">{activeItem?.label}</span></p>
          </div>
          <div className="flex items-center gap-2 sm:gap-4">
            <button aria-label="Thông báo" className="relative grid h-10 w-10 place-items-center rounded-xl border border-slate-200 text-slate-500 transition hover:bg-slate-50"><Icon name="bell" size={19} /><span className="absolute right-2 top-2 h-2 w-2 rounded-full border-2 border-white bg-rose-500" /></button>
            <div className="hidden h-8 w-px bg-slate-200 sm:block" />
            <div className="relative" ref={accountMenuRef}>
              <button aria-expanded={accountMenuOpen} aria-haspopup="menu" className="flex items-center gap-2.5 rounded-xl py-1 pl-1 pr-1 transition hover:bg-slate-50 sm:pr-2" onClick={() => setAccountMenuOpen((isOpen) => !isOpen)}>
                <span className="grid h-9 w-9 place-items-center rounded-xl bg-gradient-to-br from-cyan-500 to-blue-600 text-xs font-bold text-white">HN</span>
                <span className="hidden text-left sm:block"><span className="block text-xs font-semibold text-slate-800">Lê Hoàng Nam</span><span className="block text-[10px] text-slate-400">Nhà tuyển dụng</span></span>
                <Icon name="chevron" size={15} className={`hidden text-slate-400 transition sm:block ${accountMenuOpen ? '-rotate-90' : 'rotate-90'}`} />
              </button>
              {accountMenuOpen && (
                <div aria-label="Menu tài khoản" className="absolute right-0 top-full z-30 mt-3 w-64 overflow-hidden rounded-2xl border border-slate-200 bg-white p-2 shadow-xl shadow-slate-900/10" role="menu">
                  <div className="flex items-center gap-3 rounded-xl bg-slate-50 p-3"><span className="grid h-10 w-10 place-items-center rounded-xl bg-gradient-to-br from-cyan-500 to-blue-600 text-xs font-bold text-white">HN</span><span className="min-w-0"><span className="block text-xs font-semibold text-slate-800">Lê Hoàng Nam</span><span className="block truncate text-[10px] text-slate-500">nam.le@techcorp.vn</span></span></div>
                  <NavLink className="mt-1 flex w-full items-center gap-2.5 rounded-xl px-3 py-2 text-left text-[12px] font-medium text-slate-700 transition hover:bg-slate-50" onClick={() => setAccountMenuOpen(false)} to="/recruiter/profile" role="menuitem"><Icon name="user" size={16} className="text-slate-500" />Thông tin tài khoản</NavLink>
                  <div className="my-1 border-t border-slate-100" />
                  <button className="flex w-full cursor-not-allowed items-center gap-2.5 rounded-xl px-3 py-2 text-left text-[11px] font-medium text-slate-400" disabled role="menuitem" title="Chưa tích hợp xác thực đăng nhập"><Icon name="logout" size={16} />Đăng xuất</button>
                </div>
              )}
            </div>
          </div>
        </header>
        <main className="mx-auto max-w-[1500px] px-4 py-6 sm:px-7 sm:py-8 lg:px-10"><Outlet /></main>
      </div>
    </div>
  )
}
