import { useMemo, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import CandidateJobSearch from './CandidateJobSearch.jsx'
import { CompanyMark, Icon, SectionTitle } from './CandidateShared.jsx'
import { candidateJobs } from './candidateData.js'

const skillSuggestions = ['ReactJS', 'Java', 'Data & AI', 'UI/UX', 'DevOps']
const cityOptions = ['Hồ Chí Minh', 'Hà Nội', 'Đà Nẵng', 'Cần Thơ']

const expertiseAreas = [
  ['Phát triển ứng dụng web', 'Frontend, backend, full-stack và các nền tảng web hiện đại.'],
  ['Phát triển ứng dụng di động', 'Android, iOS, Flutter và trải nghiệm đa nền tảng.'],
  ['Dữ liệu & Trí tuệ nhân tạo', 'Data analytics, data engineering, machine learning và AI.'],
  ['Cloud & DevOps', 'Cloud computing, CI/CD, tự động hóa và độ tin cậy hệ thống.'],
  ['Kiểm thử phần mềm', 'Kiểm thử tự động, đảm bảo chất lượng và quy trình phát hành.'],
  ['An ninh mạng', 'Bảo mật ứng dụng, hạ tầng, dữ liệu và vận hành an toàn.'],
  ['Thiết kế sản phẩm số', 'UI/UX, nghiên cứu người dùng và thiết kế sản phẩm.'],
  ['Kiến trúc & hệ thống doanh nghiệp', 'Kiến trúc giải pháp, cơ sở dữ liệu và hệ thống quy mô lớn.'],
  ['Phát triển game & đồ họa', 'Game development, đồ họa máy tính và công nghệ tương tác.'],
  ['Quản lý & điều hành IT', 'Quản lý dự án, sản phẩm, đội ngũ và vận hành công nghệ.'],
]

const demandAreas = [
  { label: 'Phát triển phần mềm', value: 88 },
  { label: 'Dữ liệu & AI', value: 66 },
  { label: 'Cloud & DevOps', value: 52 },
  { label: 'Thiết kế sản phẩm', value: 39 },
]

function getHiringCompanies() {
  const grouped = new Map()
  candidateJobs.forEach((job) => {
    if (!grouped.has(job.company)) {
      grouped.set(job.company, { ...job, openingCount: 0 })
    }
    grouped.get(job.company).openingCount += 1
  })
  return [...grouped.values()]
}

function HomeJobCard({ job }) {
  return (
    <Link className="home-job-card" to={`/job-details?id=${job.id}`}>
      <div className="home-job-card-top"><span>Đăng {job.posted}</span><span className="job-label">ĐANG TUYỂN</span></div>
      <div className="home-job-card-company"><CompanyMark company={job} /><span><strong>{job.company}</strong><small>{job.location}</small></span></div>
      <h3>{job.title}</h3>
      <div className="home-job-meta"><span><Icon name="briefcase" size={15} />{job.type}</span><span><Icon name="pin" size={15} />{job.location.split(',').slice(-1)[0].trim()}</span></div>
      <div className="job-card-tags">{job.tags.slice(0, 3).map((tag) => <span key={tag}>{tag}</span>)}</div>
      <div className="home-job-card-bottom"><strong>{job.salary} <small>VNĐ</small></strong><span>{job.deadline}</span></div>
    </Link>
  )
}

export default function CandidateHomePage() {
  const navigate = useNavigate()
  const [keyword, setKeyword] = useState('')
  const [searchCity, setSearchCity] = useState('')
  const [jobsCity, setJobsCity] = useState('')
  const newestJobs = useMemo(() => candidateJobs
    .filter((job) => !jobsCity || job.location.toLocaleLowerCase('vi').includes(jobsCity.toLocaleLowerCase('vi')))
    .sort((a, b) => a.postedDays - b.postedDays), [jobsCity])
  const companies = getHiringCompanies()
  const recentlyPosted = candidateJobs.filter((job) => job.postedDays <= 1).length
  const metricCards = [
    { value: `${recentlyPosted}`, label: 'Việc làm mới trong 24 giờ', note: 'Được đăng gần đây', icon: 'clock' },
    { value: `${candidateJobs.length}`, label: 'Vị trí đang tuyển', note: 'Từ dữ liệu việc làm mẫu', icon: 'briefcase' },
    { value: `${companies.length}`, label: 'Công ty đang tuyển', note: 'Đang tìm kiếm ứng viên', icon: 'building' },
    { value: '1.280+', label: 'Hồ sơ ứng tuyển thành công', note: 'Con số minh họa', icon: 'check' },
  ]

  function searchJobs({ keyword: searchKeyword, city }) {
    const params = new URLSearchParams()
    if (searchKeyword.trim()) params.set('keyword', searchKeyword.trim())
    if (city) params.set('city', city)
    const query = params.toString()
    navigate(`/jobs-searching${query ? `?${query}` : ''}`)
  }

  return (
    <div className="candidate-page candidate-home">
      <section className="home-hero">
        <div className="candidate-container home-hero-inner">
          <div className="home-hero-copy">
            <span className="candidate-eyebrow">CƠ HỘI CÔNG NGHỆ DÀNH CHO BẠN</span>
            <h1>Tìm đúng cơ hội.<br /><em>Phát triển đúng hướng.</em></h1>
            <p>Khám phá việc làm IT mới nhất và tìm nơi phù hợp để bước tiếp trong sự nghiệp.</p>
            <CandidateJobSearch
              city={searchCity}
              className="home-search-form"
              keyword={keyword}
              onCityChange={setSearchCity}
              onKeywordChange={setKeyword}
              onSearch={searchJobs}
            />
            <div className="home-suggestions">
              <span>Đang được quan tâm</span>
              {skillSuggestions.map((skill) => <button key={skill} onClick={() => setKeyword(skill)} type="button">{skill}</button>)}
            </div>
          </div>
          <div aria-hidden="true" className="home-hero-decoration"><div className="hero-orbit hero-orbit-one" /><div className="hero-orbit hero-orbit-two" /><span className="hero-decoration-mark">T</span><span className="hero-decoration-caption">TƯƠNG LAI<br />BẮT ĐẦU TỪ ĐÂY</span></div>
        </div>
      </section>
      <div aria-label="Thông điệp nghề nghiệp" className="motivation-ticker"><div className="ticker-track"><span>Đam mê tạo nên khác biệt — hãy tìm nơi để tài năng của bạn tỏa sáng.</span><i>✦</i><span>Mỗi cơ hội mới là một bước tiến trên hành trình của bạn.</span><i>✦</i><span>Đam mê tạo nên khác biệt — hãy tìm nơi để tài năng của bạn tỏa sáng.</span><i>✦</i><span>Mỗi cơ hội mới là một bước tiến trên hành trình của bạn.</span></div></div>
      <nav aria-label="Lối tắt" className="home-shortcut-bar"><div className="candidate-container home-shortcut-list"><Link to="/jobs-searching"><span><Icon name="briefcase" size={18} /></span><strong>Khám phá việc làm</strong><Icon name="arrow" size={15} /></Link><Link to="/manage-cv"><span><Icon name="file" size={18} /></span><strong>Quản lý CV</strong><Icon name="arrow" size={15} /></Link><Link to="/company-details"><span><Icon name="building" size={18} /></span><strong>Công ty công nghệ</strong><Icon name="arrow" size={15} /></Link><a href="#it-expertise"><span><Icon name="spark" size={18} /></span><strong>Lĩnh vực IT</strong><Icon name="arrow" size={15} /></a></div></nav>

      <section className="candidate-container home-section home-latest-section">
        <SectionTitle eyebrow="CẬP NHẬT MỖI NGÀY" title="Việc làm mới nhất" description="Những cơ hội vừa được đăng từ các nhà tuyển dụng." action={<Link className="home-section-link" to="/jobs-searching">Xem tất cả việc làm <Icon name="arrow" size={15} /></Link>} level="h2" />
        <div className="home-listing-toolbar"><span>{newestJobs.length} cơ hội đang hiển thị</span><label>Lọc theo thành phố<select aria-label="Lọc việc làm theo thành phố" onChange={(event) => setJobsCity(event.target.value)} value={jobsCity}><option value="">Tất cả thành phố</option>{cityOptions.map((city) => <option key={city} value={city}>{city}</option>)}</select></label></div>
        {newestJobs.length > 0 ? <div className="home-jobs-grid">{newestJobs.slice(0, 6).map((job) => <HomeJobCard job={job} key={job.id} />)}</div> : <div className="home-empty-jobs"><h3>Chưa có việc làm tại thành phố này</h3><p>Chọn thành phố khác để xem thêm cơ hội mới.</p></div>}
      </section>

      <section className="home-companies-section">
        <div className="candidate-container">
          <SectionTitle eyebrow="DOANH NGHIỆP ĐANG TÌM NHÂN TÀI" title="Công ty nổi bật" description="Khám phá các nhà tuyển dụng đang mở vị trí trên TechCareer." action={<Link className="home-section-link" to="/company-details">Khám phá công ty <Icon name="arrow" size={15} /></Link>} level="h2" />
          <div className="home-companies-grid">{companies.map((company) => <Link className="home-company-card" key={company.company} to={`/company-details?name=${encodeURIComponent(company.company)}`}><div className="home-company-card-top"><CompanyMark company={company} size="large" /><span className="home-company-jobs"><Icon name="briefcase" size={14} />{company.openingCount} vị trí</span></div><h3>{company.company}</h3><p><Icon name="pin" size={14} />{company.location}</p><div className="home-company-card-bottom"><span>{company.tags.slice(0, 2).join(' · ')}</span><Icon name="arrow" size={16} /></div></Link>)}</div>
        </div>
      </section>

      <section className="candidate-container home-market-section">
        <div className="home-market-heading"><div><span className="candidate-eyebrow">TỔNG QUAN THỊ TRƯỜNG</span><h2>Thị trường việc làm IT</h2><p>Cập nhật hoạt động tuyển dụng trên TechCareer.</p></div><span className="market-date">SỐ LIỆU MINH HỌA</span></div>
        <div className="home-market-panel">
          <div className="market-metrics">{metricCards.map((metric) => <article className="market-metric-card" key={metric.label}><span className="market-metric-icon"><Icon name={metric.icon} size={19} /></span><strong>{metric.value}</strong><b>{metric.label}</b><small>{metric.note}</small></article>)}</div>
          <div className="market-visuals">
            <section className="market-demand-chart"><div className="market-chart-heading"><span className="market-chart-icon"><Icon name="chart" size={17} /></span><div><h3>Nhu cầu theo lĩnh vực</h3><p>Phân bổ minh họa theo nhóm ngành</p></div></div>{demandAreas.map((area) => <div className="demand-row" key={area.label}><div><span>{area.label}</span><b>{area.value}%</b></div><i><em style={{ width: `${area.value}%` }} /></i></div>)}</section>
            <section className="market-trend-chart"><div className="market-chart-heading"><span className="market-chart-icon"><Icon name="arrow" size={17} /></span><div><h3>Việc làm mới gần đây</h3><p>Xu hướng đăng tuyển trong tuần</p></div></div><div className="trend-chart-area"><div className="trend-y-labels"><span>40</span><span>30</span><span>20</span><span>10</span></div><svg aria-label="Biểu đồ xu hướng việc làm mới" className="trend-chart-svg" preserveAspectRatio="none" role="img" viewBox="0 0 360 125"><path className="trend-chart-fill" d="M0 99 C26 76 34 85 60 60 S95 76 120 48 S152 62 180 38 S212 53 239 34 S270 48 300 18 S333 42 360 10 L360 125 L0 125 Z" /><path className="trend-chart-line" d="M0 99 C26 76 34 85 60 60 S95 76 120 48 S152 62 180 38 S212 53 239 34 S270 48 300 18 S333 42 360 10" /></svg></div><div className="trend-x-labels"><span>T2</span><span>T3</span><span>T4</span><span>T5</span><span>T6</span><span>T7</span><span>CN</span></div></section>
          </div>
          <p className="market-disclaimer">Các số liệu trên chỉ để minh họa giao diện, chưa lấy từ dữ liệu tuyển dụng thực tế.</p>
        </div>
      </section>

      <section className="candidate-container home-expertise-section" id="it-expertise">
        <SectionTitle eyebrow="ĐỊNH HƯỚNG SỰ NGHIỆP" title="Khám phá lĩnh vực IT" description="Tìm hiểu các nhánh nghề nghiệp phổ biến và chọn hướng đi phù hợp với bạn." level="h2" />
        <div className="expertise-grid">{expertiseAreas.map(([title, description], index) => <details className="expertise-item" key={title}><summary><span className="expertise-number">{String(index + 1).padStart(2, '0')}</span><strong>{title}</strong><span className="expertise-plus">+</span></summary><p>{description}</p><Link to={`/jobs-searching?keyword=${encodeURIComponent(title)}`}>Tìm việc trong lĩnh vực này <Icon name="arrow" size={14} /></Link></details>)}</div>
      </section>
    </div>
  )
}
