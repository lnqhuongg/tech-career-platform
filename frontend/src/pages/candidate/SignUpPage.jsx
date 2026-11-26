import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Icon } from './CandidateShared.jsx'

export default function SignUpPage() {
  const [submitted, setSubmitted] = useState(false)
  return (
    <div className="auth-page">
      <section className="auth-aside">
        <Link className="candidate-brand auth-brand" to="/jobs-searching"><span className="brand-symbol">T</span><span>Tech<span>Career</span></span></Link>
        <div className="auth-aside-copy"><span className="candidate-eyebrow">BẮT ĐẦU HÀNH TRÌNH MỚI</span><h1>Tìm đúng việc.<br />Đi đúng hướng.</h1><p>Tạo hồ sơ cá nhân để khám phá những cơ hội nghề nghiệp công nghệ phù hợp với bạn.</p><div className="auth-quote"><span>“</span><p>Sự nghiệp tốt bắt đầu từ cơ hội phù hợp.</p></div></div>
        <div className="auth-aside-footer">TechCareer · Kết nối tài năng công nghệ</div>
      </section>
      <section className="auth-content"><div className="auth-form-wrap signup-form-wrap"><span className="auth-step">TẠO TÀI KHOẢN MIỄN PHÍ</span><h2>Đăng ký ứng viên</h2><p className="auth-subtitle">Chỉ mất một phút để bắt đầu.</p>
        {submitted && <p className="form-feedback" role="status">Thông tin đã được ghi nhận trong bản demo. Chào mừng bạn đến với TechCareer!</p>}
        <form className="candidate-form" onSubmit={(event) => { event.preventDefault(); setSubmitted(true) }}><div className="form-two-columns"><label>Họ và tên<input autoComplete="name" placeholder="Nguyễn Văn An" required /></label><label>Số điện thoại<input autoComplete="tel" inputMode="tel" placeholder="090 123 4567" required /></label></div><label>Email<input autoComplete="email" placeholder="ten@email.com" required type="email" /></label><label>Mật khẩu<input autoComplete="new-password" minLength="8" placeholder="Tối thiểu 8 ký tự" required type="password" /></label><label>Lĩnh vực quan tâm<select defaultValue=""><option disabled value="">Chọn lĩnh vực bạn quan tâm</option><option>Phát triển phần mềm</option><option>Dữ liệu &amp; Trí tuệ nhân tạo</option><option>Thiết kế UI/UX</option><option>DevOps &amp; Hạ tầng</option><option>An ninh mạng</option></select></label><label className="checkbox-label signup-terms"><input required type="checkbox" /> Tôi đồng ý với <a href="#terms">Điều khoản sử dụng</a> và <a href="#privacy">Chính sách bảo mật</a>.</label><button className="candidate-button candidate-button-primary auth-submit" type="submit">Tạo tài khoản <Icon name="arrow" size={17} /></button></form><p className="auth-switch">Đã có tài khoản? <Link to="/signin">Đăng nhập</Link></p>
      </div></section>
    </div>
  )
}
