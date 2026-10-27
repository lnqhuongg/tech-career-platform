import { useOutletContext } from 'react-router-dom'
import { applications } from './adminData.js'
import {
  PageHeading,
  Pagination,
  SearchEmpty,
  StatusBadge,
  TableCard,
} from './components/AdminShared.jsx'

const statusLabels = {
  PENDING: 'Chờ xem',
  REVIEWING: 'Đang xem xét',
  INTERVIEW: 'Phỏng vấn',
  ACCEPTED: 'Đã nhận',
  REJECTED: 'Đã từ chối',
  WITHDRAWN: 'Đã rút',
}

export default function ApplicationsPage() {
  const { search } = useOutletContext()
  const filteredApplications = applications.filter((application) =>
    `${application.candidate} ${application.candidateEmail} ${application.job} ${application.company} ${statusLabels[application.status]}`
      .toLowerCase()
      .includes(search.toLowerCase()),
  )

  return (
    <>
      <PageHeading
        eyebrow="Theo dõi tuyển dụng"
        title="Đơn ứng tuyển"
        description="Theo dõi trạng thái ứng tuyển và lịch sử xử lý hồ sơ trên nền tảng."
        action={
          <button className="rounded-xl border border-slate-200 bg-white px-4 py-2.5 text-xs font-semibold text-slate-700 shadow-sm hover:bg-slate-50">
            Xuất báo cáo
          </button>
        }
      />

      <div className="mb-5 grid grid-cols-2 gap-3 xl:grid-cols-4">
        {[
          ['Tổng đơn ứng tuyển', '8.592'],
          ['Chờ xem xét', '1.284'],
          ['Đang phỏng vấn', '426'],
          ['Đã nhận việc', '318'],
        ].map(([label, value]) => (
          <article key={label} className="rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm">
            <p className="text-[11px] text-slate-500">{label}</p>
            <p className="mt-1 text-xl font-bold text-slate-800">{value}</p>
          </article>
        ))}
      </div>

      <TableCard
        title="Danh sách đơn ứng tuyển"
        subtitle="Trạng thái được cập nhật bởi nhà tuyển dụng"
        action={
          <button className="rounded-lg border border-slate-200 px-3 py-2 text-[10px] font-semibold text-slate-600 hover:bg-slate-50">
            Lọc trạng thái
          </button>
        }
      >
        <div className="overflow-x-auto">
          <table className="w-full min-w-[920px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
              <tr>
                <th className="px-6 py-3">Ứng viên</th>
                <th className="px-4 py-3">Vị trí ứng tuyển</th>
                <th className="px-4 py-3">Công ty</th>
                <th className="px-4 py-3">Trạng thái</th>
                <th className="px-4 py-3">Ngày ứng tuyển</th>
                <th className="px-4 py-3">Lịch sử</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredApplications.map((application) => (
                <tr key={`${application.candidateEmail}-${application.job}`} className="hover:bg-slate-50/70">
                  <td className="px-6 py-4">
                    <p className="text-xs font-semibold text-slate-800">{application.candidate}</p>
                    <p className="mt-1 text-[10px] text-slate-400">{application.candidateEmail}</p>
                  </td>
                  <td className="px-4 py-4 text-[11px] font-medium text-slate-700">{application.job}</td>
                  <td className="px-4 py-4 text-[11px] text-slate-500">{application.company}</td>
                  <td className="px-4 py-4"><StatusBadge>{statusLabels[application.status]}</StatusBadge></td>
                  <td className="whitespace-nowrap px-4 py-4 text-[11px] text-slate-500">{application.appliedAt}</td>
                  <td className="px-4 py-4">
                    <div className="flex max-w-48 flex-wrap gap-1">
                      {application.history.map((event) => (
                        <span key={event} className="rounded-md bg-slate-100 px-2 py-1 text-[9px] text-slate-500">{event}</span>
                      ))}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {filteredApplications.length === 0 && <SearchEmpty query={search} />}
        </div>
        <Pagination totalItems={filteredApplications.length} />
      </TableCard>
    </>
  )
}
