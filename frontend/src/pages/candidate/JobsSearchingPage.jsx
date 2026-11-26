import { useMemo, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'
import { candidateJobs, getCandidateJob } from './candidateData.js'
import { CompanyMark, Icon, JobCard, SectionTitle } from './CandidateShared.jsx'
import CandidateJobSearch from './CandidateJobSearch.jsx'

const initialFilters = { posted: '', type: '', salary: '', level: '', experience: '' }

export default function JobsSearchingPage() {
  const [searchParams] = useSearchParams()
  const [keyword, setKeyword] = useState(() => searchParams.get('keyword') ?? '')
  const [city, setCity] = useState(() => searchParams.get('city') ?? '')
  const [filters, setFilters] = useState(initialFilters)
  const [selectedId, setSelectedId] = useState(candidateJobs[0].id)
  const [savedIds, setSavedIds] = useState([])
  const [searched, setSearched] = useState(false)
  const [sortOrder, setSortOrder] = useState('relevant')
  const filteredJobs = useMemo(() => candidateJobs.filter((job) => {
    const searchText = `${job.title} ${job.company} ${job.summary} ${job.tags.join(' ')}`.toLocaleLowerCase('vi')
    const salaryFloor = Number(job.salary.split('–')[0].trim())
    const salaryMatches = !filters.salary
      || (filters.salary === 'under10' && salaryFloor < 10)
      || (filters.salary === '10-20' && salaryFloor >= 10 && salaryFloor < 20)
      || (filters.salary === '20-30' && salaryFloor >= 20 && salaryFloor <= 30)
      || (filters.salary === 'over30' && salaryFloor > 30)
    const postedMatches = !filters.posted
      || (filters.posted === 'day' && job.postedDays <= 1)
      || (filters.posted === 'week' && job.postedDays <= 7)
      || (filters.posted === 'month' && job.postedDays <= 30)
    return (!keyword.trim() || searchText.includes(keyword.trim().toLocaleLowerCase('vi')))
      && (!city || job.location.toLocaleLowerCase('vi').includes(city.toLocaleLowerCase('vi')))
      && (!filters.type || job.type === filters.type)
      && (!filters.level || job.level === filters.level)
      && (!filters.experience || job.experience === filters.experience)
      && salaryMatches
      && postedMatches
  }).sort((a, b) => sortOrder === 'recent' ? a.postedDays - b.postedDays : 0), [city, filters, keyword, sortOrder])
  const selectedJob = filteredJobs.find((job) => job.id === selectedId) ?? filteredJobs[0] ?? getCandidateJob(selectedId)

  function updateFilter(key, value) {
    setFilters((current) => ({ ...current, [key]: value }))
  }
  function toggleSaved(id) {
    setSavedIds((current) => current.includes(id) ? current.filter((savedId) => savedId !== id) : [...current, id])
  }

  return <div className="candidate-page jobs-page">
    <section className="jobs-search-hero"><div className="candidate-container"><span className="candidate-eyebrow">CƠ HỘI CÔNG NGHỆ DÀNH CHO BẠN</span><h1>Tìm công việc <em>xứng tầm</em> tiếp theo</h1><p>Khám phá hàng trăm cơ hội nghề nghiệp từ các công ty công nghệ hàng đầu.</p><CandidateJobSearch city={city} keyword={keyword} onCityChange={setCity} onKeywordChange={setKeyword} onSearch={({ city: nextCity, keyword: nextKeyword }) => { setCity(nextCity); setKeyword(nextKeyword); setSearched(true) }} /></div></section>
    <div aria-label="Thông điệp nghề nghiệp" className="motivation-ticker"><div className="ticker-track"><span>Đam mê tạo nên khác biệt — hãy tìm nơi để tài năng của bạn tỏa sáng.</span><i>✦</i><span>Mỗi cơ hội mới là một bước tiến trên hành trình của bạn.</span><i>✦</i><span>Đam mê tạo nên khác biệt — hãy tìm nơi để tài năng của bạn tỏa sáng.</span><i>✦</i><span>Mỗi cơ hội mới là một bước tiến trên hành trình của bạn.</span></div></div>
    <section className="candidate-container job-results-section"><SectionTitle eyebrow="CƠ HỘI MỚI MỖI NGÀY" title="Việc làm IT đang tuyển" description={searched ? `Tìm thấy ${filteredJobs.length} cơ hội phù hợp với tiêu chí của bạn.` : 'Lọc theo điều bạn quan tâm và tìm bước tiếp theo trong sự nghiệp.'} action={<span className="results-count">{filteredJobs.length} vị trí phù hợp</span>} />
      <div className="job-filter-row"><label><span>Thời gian đăng</span><select onChange={(event) => updateFilter('posted', event.target.value)} value={filters.posted}><option value="">Tất cả thời gian</option><option value="day">24 giờ qua</option><option value="week">7 ngày qua</option><option value="month">30 ngày qua</option></select></label><label><span>Loại hình</span><select onChange={(event) => updateFilter('type', event.target.value)} value={filters.type}><option value="">Tất cả loại hình</option><option>Toàn thời gian</option><option>Bán thời gian</option><option>Thực tập</option><option>Hợp đồng</option></select></label><label><span>Mức lương</span><select onChange={(event) => updateFilter('salary', event.target.value)} value={filters.salary}><option value="">Mọi mức lương</option><option value="under10">Dưới 10 triệu</option><option value="10-20">10 – 20 triệu</option><option value="20-30">20 – 30 triệu</option><option value="over30">Trên 30 triệu</option></select></label><label><span>Cấp bậc</span><select onChange={(event) => updateFilter('level', event.target.value)} value={filters.level}><option value="">Tất cả cấp bậc</option><option>Thực tập sinh</option><option>Fresher</option><option>Junior</option><option>Middle</option><option>Senior</option><option>Trưởng nhóm</option><option>Quản lý</option><option>Giám đốc</option></select></label><label><span>Kinh nghiệm</span><select onChange={(event) => updateFilter('experience', event.target.value)} value={filters.experience}><option value="">Mọi kinh nghiệm</option><option>Không yêu cầu</option><option>1 năm</option><option>2 năm</option><option>3 năm</option></select></label><button className="clear-filters" onClick={() => { setFilters(initialFilters); setCity(''); setKeyword('') }} type="button">Xóa bộ lọc</button></div>
      <div className="job-results-layout"><div className="job-list-column"><div className="job-list-caption"><span>{filteredJobs.length} việc làm</span><label>Sắp xếp: <select onChange={(event) => setSortOrder(event.target.value)} value={sortOrder}><option value="relevant">Phù hợp nhất</option><option value="recent">Mới nhất</option></select></label></div>{filteredJobs.length > 0 ? filteredJobs.map((job) => <JobCard active={job.id === selectedJob.id} job={job} key={job.id} onSelect={() => setSelectedId(job.id)} />) : <div className="empty-results"><Icon name="search" size={25} /><h3>Chưa tìm thấy việc làm phù hợp</h3><p>Thử đổi từ khóa hoặc xóa bớt bộ lọc nhé.</p><button className="candidate-button candidate-button-outline" onClick={() => { setFilters(initialFilters); setCity(''); setKeyword('') }} type="button">Xóa bộ lọc</button></div>}</div>
        {filteredJobs.length > 0 && <aside className="job-detail-panel"><div className="detail-panel-top"><div className="detail-company-row"><CompanyMark company={selectedJob} size="large" /><span><small>NHÀ TUYỂN DỤNG</small><Link to={`/company-details?name=${encodeURIComponent(selectedJob.company)}`}>{selectedJob.company}</Link></span><button aria-label={savedIds.includes(selectedJob.id) ? 'Bỏ lưu việc làm' : 'Lưu việc làm'} className={`save-job-button ${savedIds.includes(selectedJob.id) ? 'is-saved' : ''}`} onClick={() => toggleSaved(selectedJob.id)}><Icon name="heart" size={19} /></button></div><h2>{selectedJob.title}</h2><p className="detail-location"><Icon name="pin" size={15} />{selectedJob.location}</p><div className="detail-cta-row"><Link className="candidate-button candidate-button-primary" to="/signin">Ứng tuyển ngay <Icon name="arrow" size={16} /></Link><Link className="candidate-button candidate-button-outline" to={`/job-details?id=${selectedJob.id}`}>Chi tiết công việc</Link></div></div><div className="detail-panel-body"><div className="job-quick-facts"><div><small>MỨC LƯƠNG</small><strong>{selectedJob.salary} <small>VNĐ</small></strong></div><div><small>LOẠI HÌNH</small><strong>{selectedJob.type}</strong></div><div><small>CẤP BẬC</small><strong>{selectedJob.level}</strong></div><div><small>HẠN ỨNG TUYỂN</small><strong>{selectedJob.deadline}</strong></div></div><section className="detail-preview-section"><h3>Mô tả công việc</h3><p>{selectedJob.description}</p></section><section className="detail-preview-section"><h3>Yêu cầu</h3><ul>{selectedJob.requirements.slice(0, 3).map((item) => <li key={item}>{item}</li>)}</ul></section><section className="detail-preview-section"><h3>Quyền lợi</h3><ul>{selectedJob.benefits.slice(0, 2).map((item) => <li key={item}>{item}</li>)}</ul></section><Link className="detail-see-more" to={`/job-details?id=${selectedJob.id}`}>Xem đầy đủ mô tả và thông tin <Icon name="arrow" size={15} /></Link></div><Link className="company-preview" to={`/company-details?name=${encodeURIComponent(selectedJob.company)}`}><span>KHÁM PHÁ NHÀ TUYỂN DỤNG</span><strong>{selectedJob.company}</strong><span>{selectedJob.location} <Icon name="arrow" size={14} /></span></Link></aside>}
      </div></section>
  </div>
}
