import { useEffect, useRef, useState } from 'react'
import { NavLink, Outlet, useLocation } from 'react-router-dom'

const navigation = [
  { path: '/dashboard', label: 'Tổng quan', icon: 'grid' },
  { path: '/users', label: 'Tất cả tài khoản', icon: 'users', badge: '1.2k' },
  { path: '/candidates', label: 'Ứng viên', icon: 'users' },
  { path: '/recruiters', label: 'Nhà tuyển dụng', icon: 'building' },
  { path: '/jobs', label: 'Tin tuyển dụng', icon: 'briefcase' },
  { path: '/applications', label: 'Đơn ứng tuyển', icon: 'file' },
  { path: '/companies', label: 'Công ty', icon: 'building' },
  { path: '/job-category', label: 'Danh mục công việc', icon: 'tag' },
  { path: '/skills', label: 'Kỹ năng', icon: 'sparkles' },
  { path: '/statistics', label: 'Báo cáo & thống kê', icon: 'chart' },
]

const iconPaths = {
  grid: <><rect x="3" y="3" width="7" height="7" rx="1.5" /><rect x="14" y="3" width="7" height="7" rx="1.5" /><rect x="3" y="14" width="7" height="7" rx="1.5" /><rect x="14" y="14" width="7" height="7" rx="1.5" /></>,
  users: <><path d="M16 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" /><circle cx="10" cy="7" r="4" /><path d="M20 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75" /></>,
  briefcase: <><rect x="3" y="7" width="18" height="14" rx="2" /><path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M3 12h18M10 12v2h4v-2" /></>,
  building: <><rect x="4" y="3" width="16" height="18" rx="2" /><path d="M9 21v-4h6v4M8 7h1m6 0h1m-8 4h1m6 0h1" /></>,
  tag: <><path d="M20.59 13.41 11 3.83A2 2 0 0 0 9.59 3H4a1 1 0 0 0-1 1v5.59A2 2 0 0 0 3.59 11l9.58 9.59a2 2 0 0 0 2.83 0l4.59-4.59a2 2 0 0 0 0-2.59Z" /><circle cx="7.5" cy="7.5" r=".5" /></>,
  chart: <><path d="M3 3v18h18M18 17V9m-5 8V5m-5 12v-5" /></>,
  file: <><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" /><path d="M14 2v6h6M8 13h8m-8 4h8" /></>,
  sparkles: <><path d="m12 3 1.9 5.8L20 11l-6.1 2.2L12 19l-1.9-5.8L4 11l6.1-2.2L12 3Z" /><path d="m19 14 1.1 2.9L23 18l-2.9 1.1L19 22l-1.1-2.9L15 18l2.9-1.1L19 14Z" /></>,
  search: <><circle cx="11" cy="11" r="7" /><path d="m20 20-4-4" /></>,
  bell: <><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4" /></>,
  menu: <><path d="M4 6h16M4 12h16M4 18h16" /></>,
  close: <><path d="m18 6-12 12M6 6l12 12" /></>,
  chevron: <path d="m9 18 6-6-6-6" />,
  user: <><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" /><circle cx="12" cy="7" r="4" /></>,
  logout: <><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" /><path d="m16 17 5-5-5-5M21 12H9" /></>,
  arrowUp: <><path d="m7 14 5-5 5 5" /><path d="M12 19V9" /></>,
  dots: <><circle cx="5" cy="12" r="1" /><circle cx="12" cy="12" r="1" /><circle cx="19" cy="12" r="1" /></>,
}

export function Icon({ name, size = 20, className = '' }) {
  return (
    <svg
      aria-hidden="true"
      className={className}
      width={size}
      height={size}
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="1.8"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      {iconPaths[name]}
    </svg>
  )
}

export default function AdminLayout({ children }) {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false)
  const [search, setSearch] = useState('')
  const [accountMenuOpen, setAccountMenuOpen] = useState(false)
  const accountMenuRef = useRef(null)
  const location = useLocation()
  const activeItem = navigation.find((item) => item.path === location.pathname)
    ?? (location.pathname === '/profile' ? { label: 'Thông tin tài khoản' } : undefined)

  useEffect(() => {
    function handlePointerDown(event) {
      if (!accountMenuRef.current?.contains(event.target)) {
        setAccountMenuOpen(false)
      }
    }

    function handleKeyDown(event) {
      if (event.key === 'Escape') {
        setAccountMenuOpen(false)
      }
    }

    document.addEventListener('pointerdown', handlePointerDown)
    document.addEventListener('keydown', handleKeyDown)
    return () => {
      document.removeEventListener('pointerdown', handlePointerDown)
      document.removeEventListener('keydown', handleKeyDown)
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
          <NavLink className="flex items-center gap-2.5" to="/dashboard" onClick={() => setMobileMenuOpen(false)}>
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-cyan-50 text-cyan-700">
              <Icon name="briefcase" size={20} />
            </span>
            <span>
              <span className="block text-[16px] font-bold tracking-tight text-slate-800">TechCareer</span>
              <span className="block text-[9px] font-semibold uppercase tracking-[0.18em] text-slate-400">Admin portal</span>
            </span>
          </NavLink>
          <button aria-label="Đóng menu" className="rounded-lg p-2 text-slate-500 hover:bg-slate-100 lg:hidden" onClick={() => setMobileMenuOpen(false)}>
            <Icon name="close" size={19} />
          </button>
        </div>

        <div className="px-3 pt-6">
          <p className="px-3 pb-3 text-[9px] font-bold uppercase tracking-[0.16em] text-slate-400">Không gian quản trị</p>
          <nav aria-label="Điều hướng quản trị" className="space-y-1">
            {navigation.map((item) => {
              return (
                <NavLink
                  key={item.path}
                  to={item.path}
                  end
                  className={({ isActive }) => `flex w-full items-center gap-2.5 rounded-lg px-3 py-2.5 text-left text-[12px] font-medium transition ${isActive ? 'bg-cyan-50 text-cyan-800' : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'}`}
                  onClick={() => setMobileMenuOpen(false)}
                >
                  {({ isActive }) => (
                    <>
                      <Icon name={item.icon} size={18} />
                  <span className="flex-1">{item.label}</span>
                      {item.badge && <span className={`rounded-md px-1.5 py-0.5 text-[9px] font-bold ${isActive ? 'bg-cyan-100 text-cyan-800' : 'bg-slate-100 text-slate-500'}`}>{item.badge}</span>}
                    </>
                  )}
                </NavLink>
              )
            })}
          </nav>
        </div>

      </aside>

      <div className="min-h-screen lg:pl-[240px]">
        <header className="sticky top-0 z-20 flex h-[76px] items-center justify-between border-b border-slate-200/80 bg-white/90 px-4 backdrop-blur-md sm:px-7 lg:px-10">
          <div className="flex min-w-0 items-center gap-3">
            <button aria-label="Mở menu" className="rounded-xl border border-slate-200 p-2 text-slate-600 hover:bg-slate-50 lg:hidden" onClick={() => setMobileMenuOpen(true)}>
              <Icon name="menu" size={20} />
            </button>
            <div className="min-w-0">
              <p className="truncate text-[15px] font-medium text-slate-400">Quản trị hệ thống <span className="px-1">/</span> <span className="text-slate-600">{activeItem?.label}</span></p>
            </div>
          </div>
          <div className="flex items-center gap-2 sm:gap-4">
            <button aria-label="Thông báo" className="relative grid h-10 w-10 place-items-center rounded-xl border border-slate-200 text-slate-500 transition hover:bg-slate-50">
              <Icon name="bell" size={19} />
              <span className="absolute right-2 top-2 h-2 w-2 rounded-full border-2 border-white bg-rose-500" />
            </button>
            <div className="hidden h-8 w-px bg-slate-200 sm:block" />
            <div className="relative" ref={accountMenuRef}>
              <button
                aria-expanded={accountMenuOpen}
                aria-haspopup="menu"
                className="flex items-center gap-2.5 rounded-xl py-1 pl-1 pr-1 transition hover:bg-slate-50 sm:pr-2"
                onClick={() => setAccountMenuOpen((isOpen) => !isOpen)}
              >
                <span className="grid h-9 w-9 place-items-center rounded-xl bg-gradient-to-br from-cyan-500 to-blue-600 text-xs font-bold text-white">AD</span>
                <span className="hidden text-left sm:block">
                  <span className="block text-xs font-semibold text-slate-800">Admin</span>
                  <span className="block text-[10px] text-slate-400">Quản trị viên</span>
                </span>
                <Icon name="chevron" size={15} className={`hidden text-slate-400 transition sm:block ${accountMenuOpen ? '-rotate-90' : 'rotate-90'}`} />
              </button>

              {accountMenuOpen && (
                <div
                  aria-label="Menu tài khoản"
                  className="absolute right-0 top-full z-30 mt-3 w-64 overflow-hidden rounded-2xl border border-slate-200 bg-white p-2 shadow-xl shadow-slate-900/10"
                  role="menu"
                >
                  <div className="flex items-center gap-3 rounded-xl bg-slate-50 p-3">
                    <span className="grid h-10 w-10 place-items-center rounded-xl bg-gradient-to-br from-cyan-500 to-blue-600 text-xs font-bold text-white">AD</span>
                    <span className="min-w-0">
                      <span className="block text-xs font-semibold text-slate-800">Admin</span>
                      <span className="block truncate text-[10px] text-slate-500">admin@techcareer.vn</span>
                    </span>
                  </div>
                  <NavLink
                    className="mt-1 flex w-full items-center gap-2.5 rounded-xl px-3 py-2 text-left text-[14px] font-medium text-slate-700 transition hover:bg-slate-50"
                    onClick={() => setAccountMenuOpen(false)}
                    to="/profile"
                    role="menuitem"
                  >
                    <Icon name="user" size={16} className="text-slate-500" />
                    Thông tin tài khoản
                  </NavLink>
                  <div className="my-1 border-t border-slate-100" />
                  <button
                    className="flex w-full cursor-not-allowed items-center gap-2.5 rounded-xl px-3 py-2 text-left text-[11px] font-medium text-slate-400"
                    disabled
                    role="menuitem"
                    title="Chưa tích hợp xác thực đăng nhập"
                  >
                    <Icon name="logout" size={16} />
                    Đăng xuất
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>
        <main className="mx-auto max-w-[1500px] px-4 py-6 sm:px-7 sm:py-8 lg:px-10">
          {children ?? <Outlet context={{ search, setSearch }} />}
        </main>
      </div>
    </div>
  )
}
