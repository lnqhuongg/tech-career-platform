import { useState } from 'react'
import { users } from './adminData.js'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { PageHeading, Pagination, SearchEmpty, StatusBadge, TableCard } from './components/AdminShared.jsx'

export default function UsersPage() {
  const [nameSearch, setNameSearch] = useState('')
  const [selectedRole, setSelectedRole] = useState('')
  const [selectedStatus, setSelectedStatus] = useState('')
  const filtered = users.filter((user) => (
    user.name.toLowerCase().includes(nameSearch.trim().toLowerCase())
    && (!selectedRole || user.role === selectedRole)
    && (!selectedStatus || user.status === selectedStatus)
  ))
  return (
    <>
      <PageHeading eyebrow="Quản lý tài khoản" title="Tất cả tài khoản" description="Theo dõi toàn bộ tài khoản ứng viên, nhà tuyển dụng và quản trị viên." action={<button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]">+ Thêm tài khoản</button>} />
      <div className="mb-5 grid grid-cols-2 gap-3 lg:grid-cols-4">{[['Tổng tài khoản', '12.846'], ['Ứng viên', '10.284'], ['Nhà tuyển dụng', '2.516'], ['Chờ xác minh', '46']].map(([label, value]) => <div key={label} className="rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm"><p className="text-[11px] text-slate-500">{label}</p><p className="mt-1 text-xl font-bold text-slate-800">{value}</p></div>)}</div>
      <TableCard title="Danh sách người dùng" subtitle={`${filtered.length} tài khoản được hiển thị`}>
        <div className="grid gap-3 border-b border-slate-100 bg-slate-50/50 p-4 sm:grid-cols-2 lg:grid-cols-[minmax(220px,1fr)_200px_200px]">
          <label className="flex h-10 items-center gap-2 rounded-lg border border-slate-200 bg-white px-3 text-slate-400 focus-within:border-cyan-500 focus-within:ring-2 focus-within:ring-cyan-100">
            <Icon name="search" size={16} />
            <input
              aria-label="Tìm kiếm tên người dùng"
              className="w-full bg-transparent text-xs text-slate-700 outline-none placeholder:text-slate-400"
              onChange={(event) => setNameSearch(event.target.value)}
              placeholder="Tìm kiếm tên người dùng"
              value={nameSearch}
            />
          </label>
          <select
            aria-label="Lọc theo trạng thái"
            className="h-10 rounded-lg border border-slate-200 bg-white px-3 text-xs text-slate-600 outline-none focus:border-cyan-500 focus:ring-2 focus:ring-cyan-100"
            onChange={(event) => setSelectedStatus(event.target.value)}
            value={selectedStatus}
          >
            <option value="">Tất cả trạng thái</option>
            <option value="Hoạt động">Hoạt động</option>
            <option value="Chờ xác minh">Chờ xác minh</option>
          </select>
          <select
            aria-label="Lọc theo vai trò"
            className="h-10 rounded-lg border border-slate-200 bg-white px-3 text-xs text-slate-600 outline-none focus:border-cyan-500 focus:ring-2 focus:ring-cyan-100"
            onChange={(event) => setSelectedRole(event.target.value)}
            value={selectedRole}
          >
            <option value="">Tất cả vai trò</option>
            <option value="Ứng viên">Ứng viên</option>
            <option value="Nhà tuyển dụng">Nhà tuyển dụng</option>
          </select>
        </div>
        <div className="overflow-x-auto"><table className="w-full min-w-[700px] text-left"><thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400"><tr><th className="px-6 py-3">Người dùng</th><th className="px-4 py-3">Vai trò</th><th className="px-4 py-3">Ngày tham gia</th><th className="px-4 py-3">Trạng thái</th><th className="px-4 py-3">Thao tác</th></tr></thead><tbody className="divide-y divide-slate-100">{filtered.map((user) => <tr key={user.email} className="hover:bg-slate-50/70"><td className="px-6 py-4"><div className="flex items-center gap-3"><span className={`grid h-9 w-9 place-items-center rounded-full text-[10px] font-bold ${user.color}`}>{user.initials}</span><div><p className="text-xs font-semibold text-slate-800">{user.name}</p><p className="mt-1 text-[10px] text-slate-400">{user.email}</p></div></div></td><td className="px-4 py-4 text-[11px] text-slate-600">{user.role}</td><td className="px-4 py-4 text-[11px] text-slate-500">{user.joined}</td><td className="px-4 py-4"><StatusBadge>{user.status}</StatusBadge></td><td className="px-4 py-4"><button aria-label={`Tuỳ chọn ${user.name}`} className="rounded-lg p-1.5 text-slate-400 hover:bg-slate-100"><Icon name="dots" size={17} /></button></td></tr>)}</tbody></table>{filtered.length === 0 && <SearchEmpty query={nameSearch || 'bộ lọc đã chọn'} />}</div>
        <Pagination totalItems={filtered.length} />
      </TableCard>
    </>
  )
}
