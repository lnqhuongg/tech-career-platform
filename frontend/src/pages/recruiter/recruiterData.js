export const recruiterJobs = [
  { id: 'job-101', title: 'Senior Frontend Developer', category: 'Phát triển phần mềm', location: 'TP. Hồ Chí Minh', salary: '30 - 45 triệu', status: 'Đang tuyển', posted: '20/11/2026', applications: 18, description: 'Phát triển các sản phẩm web có quy mô lớn cùng đội ngũ sản phẩm và thiết kế.', requirements: 'Từ 3 năm kinh nghiệm với React, JavaScript và REST API.' },
  { id: 'job-102', title: 'Backend Engineer - Java', category: 'Phát triển phần mềm', location: 'TP. Hồ Chí Minh', salary: '28 - 42 triệu', status: 'Đang tuyển', posted: '18/11/2026', applications: 24, description: 'Thiết kế, xây dựng và vận hành các dịch vụ backend có khả năng mở rộng.', requirements: 'Có kinh nghiệm Java, Spring Boot, SQL và hệ thống phân tán.' },
  { id: 'job-103', title: 'Product Designer (UI/UX)', category: 'Thiết kế', location: 'Hybrid', salary: '22 - 35 triệu', status: 'Đã đóng', posted: '05/11/2026', applications: 31, description: 'Thiết kế trải nghiệm người dùng xuyên suốt trên nền tảng số.', requirements: 'Thành thạo Figma, tư duy thiết kế lấy người dùng làm trung tâm.' },
  { id: 'job-104', title: 'Data Analyst', category: 'Dữ liệu & AI', location: 'TP. Hồ Chí Minh', salary: '20 - 32 triệu', status: 'Đang tuyển', posted: '12/11/2026', applications: 12, description: 'Phân tích dữ liệu sản phẩm và cung cấp insight cho các nhóm kinh doanh.', requirements: 'Có kinh nghiệm SQL, công cụ BI và phân tích định lượng.' },
  { id: 'job-105', title: 'DevOps Engineer', category: 'Hạ tầng & DevOps', location: 'Remote', salary: '35 - 50 triệu', status: 'Đang tuyển', posted: '09/11/2026', applications: 9, description: 'Tự động hóa quy trình triển khai và nâng cao độ tin cậy của hệ thống.', requirements: 'Có kinh nghiệm với AWS, Docker, Kubernetes và CI/CD.' },
]

export const recruiterApplications = [
  { id: 'app-201', candidate: 'Nguyễn Minh Anh', email: 'minhanh.nguyen@gmail.com', jobId: 'job-101', job: 'Senior Frontend Developer', status: 'Đang xem xét', appliedAt: '26/11/2026', initials: 'MA', color: 'bg-violet-100 text-violet-700', cv: 'Nguyen-Minh-Anh-CV.pdf', coverLetter: 'Tôi có 4 năm kinh nghiệm phát triển sản phẩm web với React và mong muốn đóng góp cho đội ngũ.' , notes: '', history: ['26/11/2026 · Nộp hồ sơ'] },
  { id: 'app-202', candidate: 'Trần Quốc Bảo', email: 'quocbao.tran@gmail.com', jobId: 'job-102', job: 'Backend Engineer - Java', status: 'Phỏng vấn', appliedAt: '25/11/2026', initials: 'QB', color: 'bg-amber-100 text-amber-700', cv: 'Tran-Quoc-Bao-CV.pdf', coverLetter: 'Tôi tập trung xây dựng dịch vụ Java hiệu năng cao và có kinh nghiệm với Spring Boot.', notes: 'Đã gửi lịch phỏng vấn vòng kỹ thuật.', history: ['25/11/2026 · Nộp hồ sơ', '26/11/2026 · Chuyển sang Đang xem xét', '26/11/2026 · Mời phỏng vấn'] },
  { id: 'app-203', candidate: 'Phạm Thu Hà', email: 'thuha.pham@gmail.com', jobId: 'job-103', job: 'Product Designer (UI/UX)', status: 'Chờ duyệt', appliedAt: '24/11/2026', initials: 'TH', color: 'bg-rose-100 text-rose-700', cv: 'Pham-Thu-Ha-CV.pdf', coverLetter: 'Tôi yêu thích việc kết hợp nghiên cứu người dùng và thiết kế giao diện trực quan.', notes: '', history: ['24/11/2026 · Nộp hồ sơ'] },
  { id: 'app-204', candidate: 'Đặng Gia Huy', email: 'huy.dang@gmail.com', jobId: 'job-104', job: 'Data Analyst', status: 'Đã chấp nhận', appliedAt: '22/11/2026', initials: 'GH', color: 'bg-cyan-100 text-cyan-700', cv: 'Dang-Gia-Huy-CV.pdf', coverLetter: 'Tôi có kinh nghiệm chuyển hóa dữ liệu thành đề xuất có thể hành động cho sản phẩm.', notes: 'Phù hợp, đã gửi thư mời nhận việc.', history: ['22/11/2026 · Nộp hồ sơ', '24/11/2026 · Chuyển sang Đang xem xét', '25/11/2026 · Chấp nhận hồ sơ'] },
  { id: 'app-205', candidate: 'Lưu Ngọc Linh', email: 'linh.luu@gmail.com', jobId: 'job-105', job: 'DevOps Engineer', status: 'Từ chối', appliedAt: '20/11/2026', initials: 'NL', color: 'bg-emerald-100 text-emerald-700', cv: 'Luu-Ngoc-Linh-CV.pdf', coverLetter: 'Tôi muốn phát triển các nền tảng hạ tầng ổn định và tự động hóa.', notes: 'Kinh nghiệm hiện tại chưa phù hợp yêu cầu vị trí.', history: ['20/11/2026 · Nộp hồ sơ', '22/11/2026 · Từ chối hồ sơ'] },
  { id: 'app-206', candidate: 'Bùi Thanh Tâm', email: 'tam.bui@gmail.com', jobId: 'job-101', job: 'Senior Frontend Developer', status: 'Chờ duyệt', appliedAt: '19/11/2026', initials: 'TT', color: 'bg-pink-100 text-pink-700', cv: 'Bui-Thanh-Tam-CV.pdf', coverLetter: 'Tôi có nền tảng frontend vững và kinh nghiệm tối ưu hiệu suất ứng dụng.', notes: '', history: ['19/11/2026 · Nộp hồ sơ'] },
]

export const applicationStatuses = ['Chờ duyệt', 'Đang xem xét', 'Phỏng vấn', 'Đã chấp nhận', 'Từ chối']

export const recruiterCompany = {
  name: 'VNG Corporation',
  industry: 'Công nghệ & Internet',
  size: '1.000+ nhân viên',
  address: 'TP. Hồ Chí Minh, Việt Nam',
  email: 'careers@vng.com.vn',
  phone: '+84 28 3962 3888',
  website: 'https://www.vng.com.vn',
  description: 'VNG là công ty công nghệ Việt Nam với sứ mệnh kiến tạo công nghệ và phát triển con người. Chúng tôi xây dựng các sản phẩm và nền tảng phục vụ hàng triệu người dùng.',
  culture: 'Môi trường cởi mở, khuyến khích học hỏi, hợp tác và tạo ảnh hưởng tích cực.',
  benefits: 'Bảo hiểm sức khỏe, ngân sách học tập, hoạt động đội nhóm và chế độ làm việc linh hoạt.',
}
