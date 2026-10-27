import { useOutletContext } from 'react-router-dom'
import { candidateSkills } from './adminData.js'
import {
  PageHeading,
  Pagination,
  SearchEmpty,
  TableCard,
} from './components/AdminShared.jsx'

const proficiencyLabels = {
  BEGINNER: 'Cơ bản',
  INTERMEDIATE: 'Trung bình',
  ADVANCED: 'Nâng cao',
  EXPERT: 'Chuyên gia',
}

export default function SkillsPage() {
  const { search } = useOutletContext()
  const filteredSkills = candidateSkills.filter((item) =>
    `${item.candidate} ${item.email} ${item.skill} ${item.category} ${proficiencyLabels[item.level]}`
      .toLowerCase()
      .includes(search.toLowerCase()),
  )

  return (
    <>
      <PageHeading
        eyebrow="Hồ sơ chuyên môn"
        title="Kỹ năng ứng viên"
        description="Chỉ xem thông tin kỹ năng do ứng viên khai báo trong hồ sơ; admin không chỉnh sửa nội dung này."
      />

      <div className="mb-5 rounded-xl border border-cyan-100 bg-cyan-50/70 px-4 py-3 text-[11px] leading-relaxed text-cyan-900">
        Kỹ năng thuộc hồ sơ cá nhân của ứng viên. Trang này chỉ phục vụ tra cứu và theo dõi, không có thao tác sửa hoặc xoá.
      </div>

      <div className="mb-5 grid grid-cols-1 gap-3 sm:grid-cols-3">
        {[
          ['Ứng viên có kỹ năng', '8.716'],
          ['Kỹ năng đang được khai báo', '164'],
          ['Nhóm chuyên môn', '9'],
        ].map(([label, value]) => (
          <article key={label} className="rounded-xl border border-slate-200/80 bg-white px-4 py-4 shadow-sm">
            <p className="text-[11px] text-slate-500">{label}</p>
            <p className="mt-1 text-xl font-bold text-slate-800">{value}</p>
          </article>
        ))}
      </div>

      <TableCard
        title="Kỹ năng trong hồ sơ ứng viên"
        subtitle={`${filteredSkills.length} kỹ năng được hiển thị`}
      >
        <div className="overflow-x-auto">
          <table className="w-full min-w-[820px] text-left">
            <thead className="bg-slate-50/80 text-[10px] font-semibold uppercase tracking-wide text-slate-400">
              <tr>
                <th className="px-6 py-3">Ứng viên</th>
                <th className="px-4 py-3">Kỹ năng</th>
                <th className="px-4 py-3">Nhóm</th>
                <th className="px-4 py-3">Mức độ thành thạo</th>
                <th className="px-4 py-3">Kinh nghiệm</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {filteredSkills.map((item) => (
                <tr key={`${item.email}-${item.skill}`} className="hover:bg-slate-50/70">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <span className={`grid h-9 w-9 place-items-center rounded-full text-[10px] font-bold ${item.color}`}>
                        {item.initials}
                      </span>
                      <div>
                        <p className="text-xs font-semibold text-slate-800">{item.candidate}</p>
                        <p className="mt-1 text-[10px] text-slate-400">{item.email}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-4 py-4 text-[11px] font-semibold text-slate-700">{item.skill}</td>
                  <td className="px-4 py-4 text-[11px] text-slate-500">{item.category}</td>
                  <td className="px-4 py-4">
                    <span className="rounded-full bg-violet-50 px-2.5 py-1 text-[10px] font-semibold text-violet-700">
                      {proficiencyLabels[item.level]}
                    </span>
                  </td>
                  <td className="px-4 py-4 text-[11px] text-slate-500">{item.experience}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {filteredSkills.length === 0 && <SearchEmpty query={search} />}
        </div>
        <Pagination totalItems={filteredSkills.length} />
      </TableCard>
    </>
  )
}
