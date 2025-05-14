import React from 'react'
import CandidateAnalysis from "./views/recruitment/CandidateAnalysis";

const Dashboard = React.lazy(() => import('./views/dashboard/Index'))

const Candidates = React.lazy(() => import('./views/recruitment/Candidates'))
const Interviews = React.lazy(() => import('./views/recruitment/Interviews'))

const Jobs = React.lazy(() => import('./views/management/Jobs'))
const UsersAndRoles = React.lazy(() => import('./views/management/Staff'))

const ActivityLogs = React.lazy(() => import('./views/activity/ActivityLogs'))
const Reports = React.lazy(() => import('./views/activity/Reports'))

const routes = [
  { path: '/index', name: 'Dashboard', element: Dashboard, private: true },
  { path: '/recruitment/candidates', name: 'Candidates', element: Candidates, private: true },
  { path: '/recruitment/candidates/:candidateId', name: 'Candidate Analysis', element: CandidateAnalysis, private: true },
  { path: '/recruitment/interviews', name: 'Interviews', element: Interviews, private: true },
  { path: '/management/jobs', name: 'Jobs', element: Jobs, private: true },
  { path: '/management/staff', name: 'Staff', element: UsersAndRoles, private: true },
  { path: '/logs-reports/activity-logs', name: 'Activity Logs', element: ActivityLogs, private: true },
  { path: '/logs-reports/reports', name: 'Reports', element: Reports, private: true }
]

export default routes
