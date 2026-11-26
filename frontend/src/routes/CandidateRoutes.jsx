import SignInPage from '../pages/candidate/SignInPage.jsx'
import SignUpPage from '../pages/candidate/SignUpPage.jsx'
import JobsSearchingPage from '../pages/candidate/JobsSearchingPage.jsx'
import JobDetailsPage from '../pages/candidate/JobDetailsPage.jsx'
import CompanyDetailsPage from '../pages/candidate/CompanyDetailsPage.jsx'
import ManageCVPage from '../pages/candidate/ManageCVPage.jsx'
import UploadCVPage from '../pages/candidate/UploadCVPage.jsx'
import CandidateHomePage from '../pages/candidate/CandidateHomePage.jsx'

export const candidateRoutes = [
  { path: '/', element: <CandidateHomePage /> },
  { path: '/signin', element: <SignInPage /> },
  { path: '/login', element: <SignInPage /> },
  { path: '/signup', element: <SignUpPage /> },
  { path: '/register', element: <SignUpPage /> },
  { path: '/jobs-searching', element: <JobsSearchingPage /> },
  { path: '/job-details', element: <JobDetailsPage /> },
  { path: '/company-details', element: <CompanyDetailsPage /> },
  { path: '/manage-cv', element: <ManageCVPage /> },
  { path: '/upload-cv', element: <UploadCVPage /> },
]
