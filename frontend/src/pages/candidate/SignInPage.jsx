import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Icon } from './CandidateShared.jsx'

export default function SignInPage() {
  const [submitted, setSubmitted] = useState(false)
  return (
    <div className="auth-page">
      <section className="auth-aside">
        <Link className="candidate-brand auth-brand" to="/jobs-searching"><span className="brand-symbol">T</span><span>Tech<span>Career</span></span></Link>
        <div className="auth-aside-copy"><span className="candidate-eyebrow">BƯỚC TIẾP THEO TRONG SỰ NGHIỆP</span><h1>Cơ hội phù hợp<br />đang chờ bạn.</h1><p>Tìm kiếm công việc công nghệ, khám phá doanh nghiệp và xây dựng hành trình sự nghiệp theo cách của bạn.</p><div className="auth-aside-stats"><span><strong>2.500+</strong><small>cơ hội IT</small></span><i /><span><strong>800+</strong><small>doanh nghiệp</small></span></div></div>
        <div className="auth-aside-footer">TechCareer · Kết nối tài năng công nghệ</div>
      </section>
      <section className="auth-content"><div className="auth-form-wrap"><span className="auth-step">CHÀO MỪNG TRỞ LẠI</span><h2>Đăng nhập</h2><p className="auth-subtitle">Đăng nhập để tiếp tục hành trình tìm việc của bạn.</p>
        {submitted && <p className="form-feedback" role="status">Đây là giao diện minh họa. Tính năng đăng nhập sẽ được kết nối sau.</p>}
        <form className="candidate-form" onSubmit={(event) => { event.preventDefault(); setSubmitted(true) }}><label>Email<input autoComplete="email" placeholder="ten@email.com" required type="email" /></label><label>Mật khẩu<input autoComplete="current-password" minLength="6" placeholder="Nhập mật khẩu" required type="password" /></label><div className="auth-form-row"><label className="checkbox-label"><input type="checkbox" /> Ghi nhớ đăng nhập</label><a href="#forgot-password">Quên mật khẩu?</a></div><button className="candidate-button candidate-button-primary auth-submit" type="submit">Đăng nhập <Icon name="arrow" size={17} /></button></form>
        <div className="auth-divider"><span>Hoặc đăng nhập bằng</span></div><div className="social-login"><button onClick={() => setSubmitted(true)} type="button"><b className="google-mark">G</b> Google</button><button onClick={() => setSubmitted(true)} type="button"><b className="facebook-mark">f</b> Facebook</button></div><p className="auth-switch">Chưa có tài khoản? <Link to="/signup">Đăng ký ngay</Link></p>
      </div></section>
    </div>
  )
}
