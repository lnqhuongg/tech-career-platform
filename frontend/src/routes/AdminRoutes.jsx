import { Navigate, Route, Routes } from 'react-router-dom'
import AdminLoginPage from '../pages/admin/AdminLoginPage.jsx'
import AdminLayout from '../layouts/AdminLayout.jsx'
import CategoriesPage from '../pages/admin/CategoriesPage.jsx'
import CandidatesPage from '../pages/admin/CandidatesPage.jsx'
import CompaniesPage from '../pages/admin/CompaniesPage.jsx'
import DashboardPage from '../pages/admin/DashboardPage.jsx'
import ApplicationsPage from '../pages/admin/ApplicationsPage.jsx'
import JobsPage from '../pages/admin/JobsPage.jsx'
import ProfilePage from '../pages/admin/ProfilePage.jsx'
import RecruitersPage from '../pages/admin/RecruitersPage.jsx'
import SkillsPage from '../pages/admin/SkillsPage.jsx'
import StatisticsPage from '../pages/admin/StatisticsPage.jsx'
import UsersPage from '../pages/admin/UsersPage.jsx'

export default function AdminRoutes() {
  return (
    <Routes>
      <Route path="admin/login" element={<AdminLoginPage />} />
      <Route element={<AdminLayout />}>
        <Route index element={<Navigate replace to="/dashboard" />} />
        <Route path="dashboard" element={<DashboardPage />} />
        <Route path="users" element={<UsersPage />} />
        <Route path="candidates" element={<CandidatesPage />} />
        <Route path="recruiters" element={<RecruitersPage />} />
        <Route path="jobs" element={<JobsPage />} />
        <Route path="applications" element={<ApplicationsPage />} />
        <Route path="companies" element={<CompaniesPage />} />
        <Route path="job-category" element={<CategoriesPage />} />
        <Route path="skills" element={<SkillsPage />} />
        <Route path="statistics" element={<StatisticsPage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="*" element={<Navigate replace to="/dashboard" />} />
      </Route>
    </Routes>
  )
}
