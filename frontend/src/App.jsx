import { BrowserRouter, Route, Routes } from 'react-router-dom'
import AdminRoutes from './routes/AdminRoutes.jsx'
import CandidateLayout from './layouts/CandidateLayout.jsx'
import { candidateRoutes } from './routes/CandidateRoutes.jsx'
import RecruiterRoutes from './routes/RecruiterRoutes.jsx'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/recruiter/*" element={<RecruiterRoutes />} />
        <Route element={<CandidateLayout />}>
          {candidateRoutes.map(({ path, element }) => (
            <Route key={path} path={path} element={element} />
          ))}
        </Route>
        <Route path="/*" element={<AdminRoutes />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
