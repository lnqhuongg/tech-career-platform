import { useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { Icon, SectionTitle } from './CandidateShared.jsx'

const MAX_FILE_SIZE = 5 * 1024 * 1024

export default function UploadCVPage() {
  const fileInput = useRef(null)
  const [file, setFile] = useState(null)
  const [error, setError] = useState('')
  const [uploaded, setUploaded] = useState(false)

  function acceptFile(nextFile) {
    setError('')
    setUploaded(false)
    if (!nextFile) return
    const isAllowedType = /\.(pdf|doc|docx)$/i.test(nextFile.name)
    if (!isAllowedType) {
      setFile(null)
      setError('Định dạng chưa được hỗ trợ. Vui lòng chọn file PDF, DOC hoặc DOCX.')
      return
    }
    if (nextFile.size > MAX_FILE_SIZE) {
      setFile(null)
      setError('File vượt quá 5MB. Vui lòng chọn CV có dung lượng nhỏ hơn.')
      return
    }
    setFile(nextFile)
  }

  function submitUpload(event) {
    event.preventDefault()
    if (!file) {
      setError('Vui lòng chọn CV trước khi tiếp tục.')
      return
    }
    localStorage.setItem('techcareer-cv-name', file.name)
    setUploaded(true)
  }

  return <div className="candidate-page"><div className="candidate-container upload-cv-page"><div className="breadcrumbs"><Link to="/manage-cv">Quản lý CV</Link><span>/</span><span>Tải CV lên</span></div><SectionTitle eyebrow="HỒ SƠ CHUYÊN NGHIỆP" title="Để cơ hội tìm đến bạn" description="Tải CV lên để ứng tuyển nhanh và giúp nhà tuyển dụng hiểu hơn về bạn." />
    <form className="upload-cv-card" onSubmit={submitUpload}><div className="upload-card-banner"><span className="upload-banner-icon"><Icon name="file" size={24} /></span><div><span className="candidate-eyebrow">HỒ SƠ ỨNG VIÊN</span><h2>Tải CV của bạn lên TechCareer</h2><p>Chỉ mất vài giây để sẵn sàng cho cơ hội tiếp theo.</p></div></div><div className={`cv-dropzone ${file ? 'has-file' : ''}`} onDragOver={(event) => event.preventDefault()} onDrop={(event) => { event.preventDefault(); acceptFile(event.dataTransfer.files[0]) }}><input accept=".pdf,.doc,.docx" aria-label="Chọn CV" onChange={(event) => acceptFile(event.target.files[0])} ref={fileInput} type="file" /><span className="dropzone-icon"><Icon name={file ? 'check' : 'upload'} size={25} /></span><strong>{file ? file.name : 'Kéo thả CV vào đây'}</strong><span>{file ? `${(file.size / 1024 / 1024).toFixed(2)} MB · Sẵn sàng tải lên` : 'hoặc chọn file từ thiết bị của bạn'}</span><button className="candidate-button candidate-button-outline" onClick={() => fileInput.current?.click()} type="button">{file ? 'Chọn file khác' : 'Chọn CV'}</button><small>Hỗ trợ PDF, DOC, DOCX · Dung lượng tối đa 5MB</small></div>{error && <p className="form-error" role="alert">{error}</p>}{uploaded && <p className="form-feedback" role="status">CV đã được lưu vào hồ sơ của bạn trong bản demo.</p>}<div className="upload-form-actions"><Link className="candidate-button candidate-button-text" to="/manage-cv">Quay lại</Link><button className="candidate-button candidate-button-primary" type="submit">Tải CV lên <Icon name="arrow" size={16} /></button></div></form>
    <div className="upload-benefits"><article><span><Icon name="briefcase" size={20} /></span><h3>Ứng tuyển thuận tiện</h3><p>Dùng hồ sơ đã tải lên để sẵn sàng cho những cơ hội phù hợp.</p></article><article><span><Icon name="spark" size={20} /></span><h3>Gợi ý nghề nghiệp</h3><p>Nhận gợi ý việc làm để tham khảo dựa trên thông tin CV.</p></article><article><span><Icon name="user" size={20} /></span><h3>Chủ động quyền riêng tư</h3><p>Bạn có thể quản lý trạng thái hồ sơ bất cứ lúc nào.</p></article></div><p className="cv-privacy-note">Thông tin cá nhân của bạn chỉ được sử dụng để hỗ trợ tìm việc. Đừng tải lên giấy tờ nhạy cảm ngoài CV.</p>
  </div></div>
}
