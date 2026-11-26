import { Icon } from './CandidateShared.jsx'

const cities = ['Hồ Chí Minh', 'Hà Nội', 'Đà Nẵng', 'Cần Thơ']

export default function CandidateJobSearch({
  keyword,
  city,
  onKeywordChange,
  onCityChange,
  onSearch,
  className = '',
}) {
  function submitSearch(event) {
    event.preventDefault()
    onSearch({ keyword, city })
  }

  return (
    <form className={`job-search-form ${className}`} onSubmit={submitSearch}>
      <label className="city-select-wrap">
        <Icon name="pin" size={18} />
        <select aria-label="Thành phố" onChange={(event) => onCityChange(event.target.value)} value={city}>
          <option value="">Tất cả thành phố</option>
          {cities.map((cityName) => <option key={cityName} value={cityName}>{cityName}</option>)}
        </select>
      </label>
      <label className="keyword-input-wrap">
        <Icon name="search" size={19} />
        <input
          aria-label="Từ khóa tìm kiếm"
          onChange={(event) => onKeywordChange(event.target.value)}
          placeholder="Chức danh, kỹ năng hoặc công ty..."
          value={keyword}
        />
      </label>
      <button className="candidate-button candidate-button-primary" type="submit">
        Tìm việc làm <Icon name="arrow" size={17} />
      </button>
    </form>
  )
}
