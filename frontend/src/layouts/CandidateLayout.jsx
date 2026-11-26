import { useEffect, useRef, useState } from 'react'
import { Link, NavLink, Outlet, useLocation } from 'react-router-dom'
import { Icon } from '../pages/candidate/CandidateShared.jsx'
import './candidate.css'

export default function CandidateLayout() {
  const [profileOpen, setProfileOpen] = useState(false)
  const [chatOpen, setChatOpen] = useState(false)
  const [message, setMessage] = useState('')
  const [chatMessages, setChatMessages] = useState([])
  const profileRef = useRef(null)
  const location = useLocation()
  const isAuthPage = ['/signin', '/login', '/signup', '/register'].includes(location.pathname)

  useEffect(() => {
    const titles = {
      '/signin': 'Đăng nhập',
      '/login': 'Đăng nhập',
      '/signup': 'Đăng ký',
      '/register': 'Đăng ký',
      '/': 'Trang chủ',
      '/jobs-searching': 'Tìm việc làm',
      '/job-details': 'Chi tiết việc làm',
      '/company-details': 'Thông tin công ty',
      '/manage-cv': 'Quản lý CV',
      '/upload-cv': 'Tải CV lên',
    }
    document.title = `${titles[location.pathname] ?? 'Ứng viên'} | TechCareer`
  }, [location.pathname])

  useEffect(() => {
    function closeProfile(event) {
      if (!profileRef.current?.contains(event.target)) setProfileOpen(false)
    }
    function closeOnEscape(event) {
      if (event.key === 'Escape') {
        setProfileOpen(false)
        setChatOpen(false)
      }
    }
    document.addEventListener('pointerdown', closeProfile)
    document.addEventListener('keydown', closeOnEscape)
    return () => {
      document.removeEventListener('pointerdown', closeProfile)
      document.removeEventListener('keydown', closeOnEscape)
    }
  }, [])

  return (
    <div className="candidate-app">
      <header className="candidate-header">
        <div className="candidate-header-inner">
          <Link className="candidate-brand" to="/"><span className="brand-symbol">T</span><span>Tech<span>Career</span></span></Link>
          <nav aria-label="Điều hướng chính" className="candidate-main-nav">
            <div className="nav-dropdown">
              <NavLink className={({ isActive }) => `candidate-nav-link ${isActive ? 'nav-active' : ''}`} to="/jobs-searching">Việc làm <span className="nav-caret">⌄</span></NavLink>
              <div className="nav-dropdown-menu"><span className="dropdown-caption">Khám phá việc làm</span><Link to="/jobs-searching?category=frontend">Frontend &amp; Thiết kế</Link><Link to="/jobs-searching?category=backend">Backend &amp; Phần mềm</Link><Link to="/jobs-searching?category=data">Dữ liệu &amp; AI</Link><Link to="/jobs-searching?category=devops">DevOps &amp; Hạ tầng</Link><Link className="dropdown-all" to="/jobs-searching">Xem tất cả việc làm <Icon name="arrow" size={14} /></Link></div>
            </div>
            <Link className="candidate-nav-link" to="/company-details">Công ty</Link>
            <div className="nav-dropdown">
              <NavLink className={({ isActive }) => `candidate-nav-link ${isActive ? 'nav-active' : ''}`} to="/manage-cv">Hồ sơ &amp; CV <span className="nav-caret">⌄</span></NavLink>
              <div className="nav-dropdown-menu cv-dropdown"><span className="dropdown-caption">Hồ sơ của bạn</span><Link to="/manage-cv"><Icon name="file" size={16} />Quản lý CV</Link><Link to="/upload-cv"><Icon name="upload" size={16} />Tải CV lên</Link></div>
            </div>
          </nav>
          <div className="candidate-header-actions">
            {!isAuthPage && <><Link className="header-action-link" to="/jobs-searching">Khám phá việc làm</Link>            <Link className="header-action-link header-action-secondary" to="/signin">Dành cho nhà tuyển dụng</Link><button aria-label="Thông báo" className="header-icon-button notification-button"><Icon name="bell" size={19} /><i /></button>
            <div className="profile-menu-wrap" onMouseEnter={() => setProfileOpen(true)} onMouseLeave={() => setProfileOpen(false)} ref={profileRef}>
                <button aria-expanded={profileOpen} aria-haspopup="menu" aria-label="Tài khoản của bạn" className="profile-trigger" onClick={() => setProfileOpen((open) => !open)}><span className="candidate-avatar">LH</span><span className="profile-trigger-name">Lê Hương</span><span className="nav-caret">⌄</span></button>
                {profileOpen && <div className="profile-dropdown" role="menu"><div className="profile-summary"><span className="candidate-avatar avatar-large">LH</span><span><strong>Lê Hương</strong><small>Tài khoản ứng viên</small><small>huong.le@email.com</small></span></div><div className="profile-menu-group"><b>Quản lý tìm việc</b><Link to="/jobs-searching">Việc làm đã lưu</Link><Link to="/jobs-searching">Việc làm đã ứng tuyển</Link><Link to="/jobs-searching">Việc làm phù hợp với bạn</Link></div><div className="profile-menu-group"><b>Quản lý CV &amp; hồ sơ</b><Link to="/manage-cv">CV của tôi</Link><Link to="/upload-cv">Tải CV lên</Link><Link to="/manage-cv">Cài đặt hồ sơ</Link></div><Link className="profile-signout" to="/signin">Đăng xuất</Link></div>}
              </div></>}
            {isAuthPage && <Link className="candidate-auth-back" to="/jobs-searching">Quay lại trang chủ</Link>}
          </div>
        </div>
      </header>
      <main className="candidate-main"><Outlet /></main>
      {!isAuthPage && <footer className="candidate-footer"><div className="footer-main"><div className="footer-brand-block"><Link className="candidate-brand footer-brand" to="/"><span className="brand-symbol">T</span><span>Tech<span>Career</span></span></Link><p>Kết nối tài năng công nghệ<br />với những cơ hội xứng tầm.</p><div className="footer-socials"><a aria-label="LinkedIn" href="#linkedin">in</a><a aria-label="Facebook" href="#facebook">f</a><a aria-label="YouTube" href="#youtube">▶</a></div></div><div className="footer-column"><h3>Khám phá</h3><Link to="/jobs-searching">Tất cả việc làm</Link><Link to="/company-details">Công ty công nghệ</Link><Link to="/manage-cv">Quản lý CV</Link><Link to="/upload-cv">Tải CV lên</Link></div><div className="footer-column"><h3>Dành cho ứng viên</h3><Link to="/signup">Tạo tài khoản</Link><Link to="/jobs-searching">Tìm việc làm IT</Link><Link to="/manage-cv">Hồ sơ của tôi</Link><a href="#career-guide">Cẩm nang nghề nghiệp</a></div><div className="footer-column"><h3>Hỗ trợ</h3><a href="mailto:hello@techcareer.vn">Liên hệ với chúng tôi</a><a href="#privacy">Chính sách bảo mật</a><a href="#terms">Điều khoản sử dụng</a><a href="#faq">Câu hỏi thường gặp</a></div><div className="footer-contact"><h3>Bạn cần hỗ trợ?</h3><p>Đội ngũ TechCareer luôn sẵn sàng giúp bạn.</p><a href="mailto:hello@techcareer.vn">hello@techcareer.vn</a></div></div><div className="footer-bottom"><span>© 2026 TechCareer. Kết nối hành trình sự nghiệp.</span><span>Được xây dựng cho cộng đồng công nghệ Việt Nam</span></div></footer>}
      <><button aria-label={chatOpen ? 'Đóng trợ lý nghề nghiệp' : 'Mở trợ lý nghề nghiệp'} className={`chat-launcher ${chatOpen ? 'chat-launcher-open' : ''}`} onClick={() => setChatOpen((open) => !open)}><Icon name={chatOpen ? 'close' : 'chat'} size={21} /><span>{chatOpen ? 'Đóng' : 'Trợ lý AI'}</span></button>
        {chatOpen && <section aria-label="Trợ lý nghề nghiệp" className="chat-panel"><div className="chat-panel-header"><span className="chat-avatar"><Icon name="spark" size={19} /></span><div><strong>Trợ lý nghề nghiệp</strong><small><i /> Sẵn sàng hỗ trợ bạn</small></div><button aria-label="Đóng trò chuyện" onClick={() => setChatOpen(false)}><Icon name="close" size={17} /></button></div><div className="chat-messages"><div className="chat-bubble chat-bubble-assistant">Xin chào! Mình có thể giúp bạn định hướng kỹ năng IT, tối ưu CV hoặc chuẩn bị phỏng vấn. Bạn đang quan tâm điều gì?</div>{chatMessages.map((chatMessage, index) => <div className={`chat-bubble chat-bubble-${chatMessage.author}`} key={`${chatMessage.author}-${index}`}>{chatMessage.text}</div>)}<div className="chat-suggestions"><button onClick={() => setMessage('Mình cần học gì để trở thành Full-stack Developer?')}>Lộ trình Full-stack</button><button onClick={() => setMessage('Làm sao để CV của mình nổi bật hơn?')}>Tối ưu CV</button><button onClick={() => setMessage('Mình nên chuẩn bị gì cho phỏng vấn IT?')}>Chuẩn bị phỏng vấn</button></div></div><form className="chat-compose" onSubmit={(event) => { event.preventDefault(); const text = message.trim(); if (text) { setChatMessages((current) => [...current, { author: 'candidate', text }, { author: 'assistant', text: 'Cảm ơn bạn đã chia sẻ! Câu trả lời của trợ lý AI sẽ được tích hợp trong phiên bản tiếp theo.' }]); setMessage('') } }}><input aria-label="Tin nhắn" onChange={(event) => setMessage(event.target.value)} placeholder="Nhập câu hỏi của bạn..." value={message} /><button aria-label="Gửi tin nhắn" type="submit"><Icon name="arrow" size={17} /></button></form><p className="chat-disclaimer">Câu trả lời chỉ mang tính tham khảo.</p></section>}</>
    </div>
  )
}
