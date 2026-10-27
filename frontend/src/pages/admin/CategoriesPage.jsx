import { useOutletContext } from 'react-router-dom'
import { categories } from './adminData.js'
import { Icon } from '../../layouts/AdminLayout.jsx'
import { PageHeading, SearchEmpty } from './components/AdminShared.jsx'

export default function CategoriesPage() {
  const { search } = useOutletContext()
  const filtered = categories.filter((category) => `${category.name} ${category.slug}`.toLowerCase().includes(search.toLowerCase()))
  return (
    <>
      <PageHeading eyebrow="Cấu hình nội dung" title="Danh mục công việc" description="Sắp xếp các cơ hội nghề nghiệp theo lĩnh vực chuyên môn." action={<button className="rounded-xl bg-[#142544] px-4 py-2.5 text-xs font-semibold text-white transition hover:bg-[#20375c]">+ Thêm danh mục</button>} />
      <div className="mb-5 flex flex-wrap gap-3"><div className="rounded-xl border border-slate-200/80 bg-white px-4 py-3 shadow-sm"><span className="text-[11px] text-slate-500">Tổng danh mục</span><span className="ml-3 text-lg font-bold text-slate-800">24</span></div><div className="rounded-xl border border-slate-200/80 bg-white px-4 py-3 shadow-sm"><span className="text-[11px] text-slate-500">Đang được sử dụng</span><span className="ml-3 text-lg font-bold text-slate-800">21</span></div></div>
      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-3">{filtered.map((category) => <article key={category.slug} className="group rounded-2xl border border-slate-200/80 bg-white p-5 shadow-sm shadow-slate-200/40 transition hover:-translate-y-0.5 hover:shadow-md"><div className="flex items-start justify-between"><span className={`grid h-12 w-12 place-items-center rounded-2xl bg-gradient-to-br ${category.color} text-sm font-bold text-white shadow-sm`}>{category.icon}</span><button aria-label={`Tuỳ chọn ${category.name}`} className="rounded-lg p-1.5 text-slate-400 opacity-70 hover:bg-slate-100 group-hover:opacity-100"><Icon name="dots" size={18} /></button></div><h2 className="mt-5 text-sm font-bold text-slate-800">{category.name}</h2><p className="mt-1 text-[10px] text-slate-400">/{category.slug}</p><div className="mt-5 flex items-center justify-between border-t border-slate-100 pt-4"><span className="text-[11px] text-slate-500">Tin tuyển dụng</span><span className="rounded-lg bg-slate-50 px-2.5 py-1 text-xs font-bold text-slate-700">{category.jobs}</span></div></article>)}</div>
      {filtered.length === 0 && <div className="rounded-2xl border border-slate-200 bg-white"><SearchEmpty query={search} /></div>}
    </>
  )
}
