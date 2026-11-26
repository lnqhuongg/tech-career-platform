import { Link } from 'react-router-dom'

const paths = {
  search: <><circle cx="11" cy="11" r="7" /><path d="m20 20-4-4" /></>,
  pin: <><path d="M20 10c0 5-8 12-8 12S4 15 4 10a8 8 0 1 1 16 0Z" /><circle cx="12" cy="10" r="2.5" /></>,
  briefcase: <><rect x="3" y="7" width="18" height="14" rx="1" /><path d="M8 7V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M3 12h18" /></>,
  building: <><rect x="4" y="3" width="16" height="18" /><path d="M8 7h2m4 0h2M8 11h2m4 0h2M8 15h2m4 0h2M10 21v-3h4v3" /></>,
  chart: <><path d="M3 3v18h18M8 16v-4m5 4V7m5 9v-7" /></>,
  clock: <><circle cx="12" cy="12" r="9" /><path d="M12 7v5l3 2" /></>,
  heart: <path d="M20.8 8.8c0 4.2-8.8 10-8.8 10s-8.8-5.8-8.8-10a4.8 4.8 0 0 1 8.8-2.6 4.8 4.8 0 0 1 8.8 2.6Z" />,
  bell: <><path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4" /></>,
  user: <><circle cx="12" cy="8" r="4" /><path d="M4 21a8 8 0 0 1 16 0" /></>,
  file: <><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" /><path d="M14 2v6h6M8 13h8m-8 4h6" /></>,
  upload: <><path d="M12 16V4m-5 5 5-5 5 5" /><path d="M4 16v4h16v-4" /></>,
  arrow: <><path d="M5 12h14m-6-6 6 6-6 6" /></>,
  chat: <><path d="M21 11.5a8.4 8.4 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.4 8.4 0 0 1-3.8-.9L3 21l1.9-5.7a8.4 8.4 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.4 8.4 0 0 1 3.8-.9h.5a8.5 8.5 0 0 1 8 8v.5Z" /></>,
  close: <path d="m18 6-12 12M6 6l12 12" />,
  check: <path d="m5 12 4 4L19 6" />,
  spark: <><path d="m12 3 1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8L12 3Z" /><path d="m19 15 .9 2.1L22 18l-2.1.9L19 21l-.9-2.1L16 18l2.1-.9L19 15Z" /></>,
}

export function Icon({ name, size = 18, className = '' }) {
  return <svg aria-hidden="true" className={className} width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">{paths[name]}</svg>
}

export function CompanyMark({ company, size = 'normal' }) {
  return <span aria-hidden="true" className={`company-mark ${company.logoClass} ${size === 'large' ? 'company-mark-large' : ''}`}>{company.logo}</span>
}

export function SectionTitle({ eyebrow, title, description, action, level = 'h1' }) {
  const Heading = level
  return <div className="candidate-section-title"><div>{eyebrow && <span className="candidate-eyebrow">{eyebrow}</span>}<Heading>{title}</Heading>{description && <p>{description}</p>}</div>{action}</div>
}

export function JobCard({ job, active = false, onSelect }) {
  const content = (
    <>
      <div className="job-card-top"><span className="job-card-date">{job.posted}</span><span className="job-label">ĐANG TUYỂN</span></div>
      <div className="job-card-heading"><CompanyMark company={job} /><div><h3>{job.title}</h3><p>{job.company}</p></div></div>
      <div className="job-card-meta"><span><Icon name="pin" size={15} />{job.location}</span><span><Icon name="briefcase" size={15} />{job.level}</span></div>
      <p className="job-card-summary">{job.summary}</p>
      <div className="job-card-tags">{job.tags.map((tag) => <span key={tag}>{tag}</span>)}</div>
      <div className="job-card-bottom"><strong>{job.salary} <small>VNĐ</small></strong><span><Icon name="clock" size={14} />{job.deadline}</span></div>
    </>
  )

  if (onSelect) return <button aria-pressed={active} className={`job-card ${active ? 'is-active' : ''}`} onClick={onSelect} type="button">{content}</button>
  return <Link className="job-card" to={`/job-details?id=${job.id}`}>{content}</Link>
}

export function StatusNotice({ children }) {
  return <p className="candidate-notice"><span>i</span>{children}</p>
}
