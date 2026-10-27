const users = [
  { name: 'Nguyễn Minh Anh', email: 'minhanh.nguyen@gmail.com', role: 'Ứng viên', status: 'Hoạt động', joined: '24/05/2025', initials: 'MA', color: 'bg-violet-100 text-violet-700' },
  { name: 'Trần Quốc Bảo', email: 'quocbao.tran@gmail.com', role: 'Ứng viên', status: 'Hoạt động', joined: '23/05/2025', initials: 'QB', color: 'bg-amber-100 text-amber-700' },
  { name: 'Lê Hoàng Nam', email: 'nam.le@techcorp.vn', role: 'Nhà tuyển dụng', status: 'Hoạt động', joined: '22/05/2025', initials: 'HN', color: 'bg-cyan-100 text-cyan-700' },
  { name: 'Phạm Thu Hà', email: 'thuha.pham@gmail.com', role: 'Ứng viên', status: 'Chờ xác minh', joined: '21/05/2025', initials: 'TH', color: 'bg-rose-100 text-rose-700' },
  { name: 'Vũ Đức Long', email: 'long.vu@nexttech.io', role: 'Nhà tuyển dụng', status: 'Hoạt động', joined: '20/05/2025', initials: 'ĐL', color: 'bg-emerald-100 text-emerald-700' },
]

const candidates = [
  { name: 'Nguyễn Minh Anh', email: 'minhanh.nguyen@gmail.com', targetRole: 'Frontend Developer', experience: '3 năm', location: 'TP. Hồ Chí Minh', status: 'Đang tìm việc', joined: '24/05/2025', initials: 'MA', color: 'bg-violet-100 text-violet-700' },
  { name: 'Trần Quốc Bảo', email: 'quocbao.tran@gmail.com', targetRole: 'Backend Developer', experience: '2 năm', location: 'Hà Nội', status: 'Đang tìm việc', joined: '23/05/2025', initials: 'QB', color: 'bg-amber-100 text-amber-700' },
  { name: 'Phạm Thu Hà', email: 'thuha.pham@gmail.com', targetRole: 'UI/UX Designer', experience: '4 năm', location: 'Đà Nẵng', status: 'Chưa cập nhật hồ sơ', joined: '21/05/2025', initials: 'TH', color: 'bg-rose-100 text-rose-700' },
  { name: 'Đặng Gia Huy', email: 'huy.dang@gmail.com', targetRole: 'Data Analyst', experience: '1 năm', location: 'TP. Hồ Chí Minh', status: 'Đang tìm việc', joined: '19/05/2025', initials: 'GH', color: 'bg-cyan-100 text-cyan-700' },
  { name: 'Lưu Ngọc Linh', email: 'linh.luu@gmail.com', targetRole: 'Product Designer', experience: '5 năm', location: 'Hà Nội', status: 'Tạm ẩn hồ sơ', joined: '18/05/2025', initials: 'NL', color: 'bg-emerald-100 text-emerald-700' },
]

const recruiters = [
  { name: 'Lê Hoàng Nam', email: 'nam.le@techcorp.vn', company: 'VNG Corporation', title: 'Talent Acquisition Lead', jobs: 12, status: 'Hoạt động', joined: '22/05/2025', initials: 'HN', color: 'bg-cyan-100 text-cyan-700' },
  { name: 'Vũ Đức Long', email: 'long.vu@nexttech.io', company: 'NextTech Group', title: 'Recruitment Manager', jobs: 8, status: 'Hoạt động', joined: '20/05/2025', initials: 'ĐL', color: 'bg-emerald-100 text-emerald-700' },
  { name: 'Ngô Khánh Vy', email: 'vy.ngo@fpt.com', company: 'FPT Software', title: 'IT Recruiter', jobs: 15, status: 'Hoạt động', joined: '17/05/2025', initials: 'KV', color: 'bg-violet-100 text-violet-700' },
  { name: 'Đỗ Minh Tuấn', email: 'tuan.do@startup.vn', company: 'Startup Việt', title: 'Co-founder', jobs: 2, status: 'Chờ xác minh', joined: '16/05/2025', initials: 'MT', color: 'bg-amber-100 text-amber-700' },
  { name: 'Bùi Thanh Tâm', email: 'tam.bui@momo.vn', company: 'Momo', title: 'Senior Recruiter', jobs: 6, status: 'Hoạt động', joined: '14/05/2025', initials: 'TT', color: 'bg-pink-100 text-pink-700' },
]

const applications = [
  { candidate: 'Nguyễn Minh Anh', candidateEmail: 'minhanh.nguyen@gmail.com', job: 'Senior Frontend Developer', company: 'VNG Corporation', status: 'REVIEWING', appliedAt: '27/10/2026', updatedAt: '27/10/2026', initials: 'MA', color: 'bg-violet-100 text-violet-700', history: ['Ứng tuyển', 'Nhà tuyển dụng đang xem hồ sơ'] },
  { candidate: 'Trần Quốc Bảo', candidateEmail: 'quocbao.tran@gmail.com', job: 'Backend Engineer - Java', company: 'FPT Software', status: 'INTERVIEW', appliedAt: '26/10/2026', updatedAt: '27/10/2026', initials: 'QB', color: 'bg-amber-100 text-amber-700', history: ['Ứng tuyển', 'Đang xem xét', 'Mời phỏng vấn'] },
  { candidate: 'Phạm Thu Hà', candidateEmail: 'thuha.pham@gmail.com', job: 'Product Designer (UI/UX)', company: 'Momo', status: 'PENDING', appliedAt: '26/10/2026', updatedAt: '26/10/2026', initials: 'TH', color: 'bg-rose-100 text-rose-700', history: ['Ứng tuyển'] },
  { candidate: 'Đặng Gia Huy', candidateEmail: 'huy.dang@gmail.com', job: 'Data Analyst', company: 'Tiki', status: 'ACCEPTED', appliedAt: '24/10/2026', updatedAt: '26/10/2026', initials: 'GH', color: 'bg-cyan-100 text-cyan-700', history: ['Ứng tuyển', 'Đang xem xét', 'Đã nhận'] },
  { candidate: 'Lưu Ngọc Linh', candidateEmail: 'linh.luu@gmail.com', job: 'DevOps Engineer', company: 'NVIDIA Vietnam', status: 'REJECTED', appliedAt: '22/10/2026', updatedAt: '25/10/2026', initials: 'NL', color: 'bg-emerald-100 text-emerald-700', history: ['Ứng tuyển', 'Đang xem xét', 'Đã từ chối'] },
]

const candidateSkills = [
  { candidate: 'Nguyễn Minh Anh', email: 'minhanh.nguyen@gmail.com', skill: 'React', level: 'ADVANCED', experience: '3 năm', category: 'Frontend', initials: 'MA', color: 'bg-violet-100 text-violet-700' },
  { candidate: 'Nguyễn Minh Anh', email: 'minhanh.nguyen@gmail.com', skill: 'JavaScript', level: 'EXPERT', experience: '4 năm', category: 'Frontend', initials: 'MA', color: 'bg-violet-100 text-violet-700' },
  { candidate: 'Trần Quốc Bảo', email: 'quocbao.tran@gmail.com', skill: 'Java', level: 'ADVANCED', experience: '2 năm', category: 'Backend', initials: 'QB', color: 'bg-amber-100 text-amber-700' },
  { candidate: 'Trần Quốc Bảo', email: 'quocbao.tran@gmail.com', skill: 'PostgreSQL', level: 'INTERMEDIATE', experience: '2 năm', category: 'Database', initials: 'QB', color: 'bg-amber-100 text-amber-700' },
  { candidate: 'Phạm Thu Hà', email: 'thuha.pham@gmail.com', skill: 'Figma', level: 'EXPERT', experience: '4 năm', category: 'Design', initials: 'TH', color: 'bg-rose-100 text-rose-700' },
  { candidate: 'Đặng Gia Huy', email: 'huy.dang@gmail.com', skill: 'Python', level: 'INTERMEDIATE', experience: '1 năm', category: 'AI & Data', initials: 'GH', color: 'bg-cyan-100 text-cyan-700' },
  { candidate: 'Lưu Ngọc Linh', email: 'linh.luu@gmail.com', skill: 'Docker', level: 'ADVANCED', experience: '3 năm', category: 'DevOps', initials: 'NL', color: 'bg-emerald-100 text-emerald-700' },
]

const jobs = [
  { title: 'Senior Frontend Developer', company: 'VNG Corporation', category: 'Phát triển phần mềm', location: 'TP. Hồ Chí Minh', salary: '30 - 45 triệu', status: 'Đang tuyển', posted: 'Hôm nay', initials: 'V', color: 'bg-blue-100 text-blue-700' },
  { title: 'Product Designer (UI/UX)', company: 'Momo', category: 'Thiết kế', location: 'TP. Hồ Chí Minh', salary: '20 - 35 triệu', status: 'Đang tuyển', posted: '2 giờ trước', initials: 'M', color: 'bg-pink-100 text-pink-700' },
  { title: 'Backend Engineer - Java', company: 'FPT Software', category: 'Phát triển phần mềm', location: 'Hà Nội', salary: '25 - 40 triệu', status: 'Chờ duyệt', posted: '4 giờ trước', initials: 'F', color: 'bg-orange-100 text-orange-700' },
  { title: 'Data Analyst', company: 'Tiki', category: 'Dữ liệu & AI', location: 'TP. Hồ Chí Minh', salary: '18 - 28 triệu', status: 'Đang tuyển', posted: 'Hôm qua', initials: 'T', color: 'bg-sky-100 text-sky-700' },
  { title: 'DevOps Engineer', company: 'NVIDIA Vietnam', category: 'Hạ tầng & DevOps', location: 'Đà Nẵng', salary: '35 - 55 triệu', status: 'Đã đóng', posted: '2 ngày trước', initials: 'N', color: 'bg-emerald-100 text-emerald-700' },
]

const companies = [
  { name: 'VNG Corporation', domain: 'vng.com.vn', industry: 'Công nghệ & Internet', size: '1.000+ nhân viên', jobs: 24, status: 'Đã xác minh', initials: 'V', color: 'bg-blue-100 text-blue-700' },
  { name: 'Momo', domain: 'momo.vn', industry: 'Fintech', size: '500 - 1.000 nhân viên', jobs: 18, status: 'Đã xác minh', initials: 'M', color: 'bg-pink-100 text-pink-700' },
  { name: 'FPT Software', domain: 'fpt-software.com', industry: 'Dịch vụ CNTT', size: '1.000+ nhân viên', jobs: 32, status: 'Đã xác minh', initials: 'F', color: 'bg-orange-100 text-orange-700' },
  { name: 'Tiki', domain: 'tiki.vn', industry: 'Thương mại điện tử', size: '500 - 1.000 nhân viên', jobs: 11, status: 'Chờ xác minh', initials: 'T', color: 'bg-sky-100 text-sky-700' },
  { name: 'NVIDIA Vietnam', domain: 'nvidia.com', industry: 'Công nghệ', size: '100 - 500 nhân viên', jobs: 7, status: 'Đã xác minh', initials: 'N', color: 'bg-emerald-100 text-emerald-700' },
]

const categories = [
  { name: 'Phát triển phần mềm', slug: 'software-development', jobs: 328, color: 'from-blue-500 to-indigo-500', icon: '</>' },
  { name: 'Dữ liệu & AI', slug: 'data-ai', jobs: 146, color: 'from-violet-500 to-purple-500', icon: '✳' },
  { name: 'Thiết kế', slug: 'design', jobs: 92, color: 'from-pink-500 to-rose-500', icon: '◈' },
  { name: 'Hạ tầng & DevOps', slug: 'infrastructure-devops', jobs: 84, color: 'from-cyan-500 to-teal-500', icon: '⌘' },
  { name: 'Sản phẩm & Quản lý', slug: 'product-management', jobs: 67, color: 'from-amber-500 to-orange-500', icon: '◎' },
  { name: 'Kiểm thử phần mềm', slug: 'quality-assurance', jobs: 54, color: 'from-emerald-500 to-green-500', icon: '✓' },
]

const statCards = [
  { label: 'Tổng người dùng', value: '12.846', change: '+12,8%', icon: 'users', color: 'bg-blue-50 text-blue-600', note: 'so với tháng trước' },
  { label: 'Tin tuyển dụng', value: '1.284', change: '+8,2%', icon: 'briefcase', color: 'bg-violet-50 text-violet-600', note: 'so với tháng trước' },
  { label: 'Công ty đang hoạt động', value: '486', change: '+6,4%', icon: 'building', color: 'bg-cyan-50 text-cyan-700', note: 'so với tháng trước' },
  { label: 'Lượt ứng tuyển', value: '8.592', change: '+18,6%', icon: 'chart', color: 'bg-emerald-50 text-emerald-600', note: 'so với tháng trước' },
]
export {
  users,
  candidates,
  recruiters,
  applications,
  candidateSkills,
  jobs,
  companies,
  categories,
  statCards,
}
