import { useOutletContext } from 'react-router-dom'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { candidates } from './adminData.js'
import {
  PageHeading,
  Pagination,
  SearchEmpty,
  StatusBadge,
  TableCard,
} from './components/AdminShared.jsx'

export default function CandidatesPage() {
  const { search } = useOutletContext()
  const filteredCandidates = candidates.filter((candidate) =>
    `${candidate.name} ${candidate.email} ${candidate.targetRole} ${candidate.location}`
      .toLowerCase()
      .includes(search.toLowerCase()),
  )

  return (
    <>
      <PageHeading
        eyebrow="Quản lý ứng viên"
        title="Ứng viên"
        description="Theo dõi hồ sơ và trạng thái tìm việc của các ứng viên trên nền tảng."
        action={
          <button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]">
            Xuất danh sách
          </button>
        }
      />

      <div className="mb-5 grid grid-cols-1 gap-3 sm:grid-cols-3">
        {[
          ['Tổng ứng viên', '10.284', 'users'],
          ['Đang tìm việc', '7.492', 'search'],
          ['Hồ sơ cập nhật gần đây', '1.206', 'chart'],
        ].map(([label, value, icon]) => (
          <article key={label} className="flex items-center justify-between rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm">
            <div>
              <p className="text-[11px] text-slate-500">{label}</p>
              <p className="mt-1 text-xl font-bold text-slate-800">{value}</p>
            </div>
            <span className="grid h-9 w-9 place-items-center rounded-xl bg-violet-50 text-violet-600">
              <Icon name={icon} size={18} />
            </span>
          </article>
        ))}
      </div>

      <TableCard
        title="Danh sách ứng viên"
        subtitle={`${filteredCandidates.length} hồ sơ được hiển thị`}
        action={
          <button className="rounded-lg border border-slate-200 px-3 py-2 text-[10px] font-semibold text-slate-600 hover:bg-slate-50">
            Lọc danh sách
          </button>
        }
      >
        <div className="overflow-x-auto">
          <table className="w-full min-w-[880px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
              <tr>
                <th className="px-6 py-3">Ứng viên</th>
                <th className="px-4 py-3">Vị trí mong muốn</th>
                <th className="px-4 py-3">Kinh nghiệm</th>
                <th className="px-4 py-3">Địa điểm</th>
                <th className="px-4 py-3">Trạng thái hồ sơ</th>
                <th className="px-4 py-3">Ngày tham gia</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredCandidates.map((candidate) => (
                <tr key={candidate.email} className="hover:bg-slate-50/70">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <span className={`grid h-9 w-9 place-items-center rounded-full text-[10px] font-bold ${candidate.color}`}>
                        {candidate.initials}
                      </span>
                      <div>
                        <p className="text-xs font-semibold text-slate-800">{candidate.name}</p>
                        <p className="mt-1 text-[10px] text-slate-400">{candidate.email}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-4 text-[11px] font-medium text-slate-600">{candidate.targetRole}</td>
                  <td className="px-4 py-4 text-[11px] text-slate-500">{candidate.experience}</td>
                  <td className="px-4 py-4 text-[11px] text-slate-500">{candidate.location}</td>
                  <td className="px-4 py-4"><StatusBadge>{candidate.status}</StatusBadge></td>
                  <td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{candidate.joined}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {filteredCandidates.length === 0 && <SearchEmpty query={search} />}
        </div>
        <Pagination totalItems={filteredCandidates.length} />
      </TableCard>
    </>
  )
}
