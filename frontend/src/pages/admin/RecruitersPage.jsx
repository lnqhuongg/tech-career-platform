import { useOutletContext } from 'react-router-dom'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { recruiters } from './adminData.js'
import {
  PageHeading,
  Pagination,
  SearchEmpty,
  StatusBadge,
  TableCard,
} from './components/AdminShared.jsx'

export default function RecruitersPage() {
  const { search } = useOutletContext()
  const filteredRecruiters = recruiters.filter((recruiter) =>
    `${recruiter.name} ${recruiter.email} ${recruiter.company} ${recruiter.title}`
      .toLowerCase()
      .includes(search.toLowerCase()),
  )

  return (
    <>
      <PageHeading
        eyebrow="Quản lý nhà tuyển dụng"
        title="Nhà tuyển dụng"
        description="Quản lý tài khoản recruiter, doanh nghiệp và hoạt động đăng tuyển."
        action={
          <button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]">
            Xuất danh sách
          </button>
        }
      />

      <div className="mb-5 grid grid-cols-1 gap-3 sm:grid-cols-3">
        {[
          ['Tổng nhà tuyển dụng', '2.516', 'users'],
          ['Đang hoạt động', '2.184', 'chart'],
          ['Chờ xác minh', '44', 'building'],
        ].map(([label, value, icon]) => (
          <article key={label} className="flex items-center justify-between rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm">
            <div>
              <p className="text-[11px] text-slate-500">{label}</p>
              <p className="mt-1 text-xl font-bold text-slate-800">{value}</p>
            </div>
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-cyan-50 text-cyan-700">
              <Icon name={icon} size={18} />
            </span>
          </article>
        ))}
      </div>

      <TableCard
        title="Danh sách nhà tuyển dụng"
        subtitle={`${filteredRecruiters.length} tài khoản được hiển thị`}
        action={
          <button className="rounded-lg border border-slate-200 px-3 py-2 text-[10px] font-semibold text-slate-600 hover:bg-slate-50">
            Lọc danh sách
          </button>
        }
      >
        <div className="overflow-x-auto">
          <table className="w-full min-w-[900px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
              <tr>
                <th className="px-6 py-3">Người đăng tuyển</th>
                <th className="px-4 py-3">Công ty</th>
                <th className="px-4 py-3">Chức vụ</th>
                <th className="px-4 py-3">Tin đã đăng</th>
                <th className="px-4 py-3">Trạng thái</th>
                <th className="px-4 py-3">Ngày tham gia</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredRecruiters.map((recruiter) => (
                <tr key={recruiter.email} className="hover:bg-slate-50/70">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <span className={`grid h-9 w-9 place-items-center rounded-full text-[10px] font-bold ${recruiter.color}`}>
                        {recruiter.initials}
                      </span>
                      <div>
                        <p className="text-xs font-semibold text-slate-800">{recruiter.name}</p>
                        <p className="mt-1 text-[10px] text-slate-400">{recruiter.email}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-4 text-[11px] font-medium text-slate-600">{recruiter.company}</td>
                  <td className="px-4 py-4 text-[11px] text-slate-500">{recruiter.title}</td>
                  <td className="px-4 py-4 text-[11px] font-semibold text-slate-700">{recruiter.jobs}</td>
                  <td className="px-4 py-4"><StatusBadge>{recruiter.status}</StatusBadge></td>
                  <td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{recruiter.joined}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {filteredRecruiters.length === 0 && <SearchEmpty query={search} />}
        </div>
        <Pagination totalItems={filteredRecruiters.length} />
      </TableCard>
    </>
  )
}
