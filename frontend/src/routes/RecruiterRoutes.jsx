import { Navigate, Route, Routes } from 'react-router-dom'
import RecruiterLayout from '../layouts/RecruiterLayout.jsx'
import ApplicationsPage from '../pages/recruiter/ApplicationsPage.jsx'
import CompanyPage from '../pages/recruiter/CompanyPage.jsx'
import DashboardPage from '../pages/recruiter/DashboardPage.jsx'
import JobsPage from '../pages/recruiter/JobsPage.jsx'
import ProfilePage from '../pages/recruiter/ProfilePage.jsx'
import StatisticsPage from '../pages/recruiter/StatisticsPage.jsx'

export default function RecruiterRoutes() {
  return (
    <Routes>
      <Route element={<RecruiterLayout />}>
        <Route index element={<Navigate replace to="/recruiter/dashboard" />} />
        <Route path="dashboard" element={<DashboardPage />} />
        <Route path="jobs" element={<JobsPage />} />
        <Route path="applications" element={<ApplicationsPage />} />
        <Route path="company" element={<CompanyPage />} />
        <Route path="statistics" element={<StatisticsPage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="*" element={<Navigate replace to="/recruiter/dashboard" />} />
      </Route>
    </Routes>
  )
}
