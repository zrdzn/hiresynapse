import React from 'react'
import CandidateAnalysis from "./views/recruitment/CandidateAnalysis";

const Dashboard = React.lazy(() => import('./views/dashboard/Dashboard'))

const Candidates = React.lazy(() => import('./views/recruitment/Candidates'))
const Criteria = React.lazy(() => import('./views/recruitment/Criteria'))

const Jobs = React.lazy(() => import('./views/management/Jobs'))
const UsersAndRoles = React.lazy(() => import('./views/management/UsersAndRoles'))

const ActivityLogs = React.lazy(() => import('./views/activity/ActivityLogs'))
const Reports = React.lazy(() => import('./views/activity/Reports'))

const routes = [
  { path: '/', exact: true, name: 'Home' },
  { path: '/dashboard', name: 'Dashboard', element: Dashboard },
  { path: '/recruitment/candidates', name: 'Candidates', element: Candidates },
  { path: '/recruitment/candidates/:candidateId', name: 'Candidate Analysis', element: CandidateAnalysis },
  { path: '/recruitment/criteria', name: 'Criteria', element: Criteria },
  { path: '/management/jobs', name: 'Jobs', element: Jobs },
  { path: '/management/users-roles', name: 'Users & Roles', element: UsersAndRoles },
  { path: '/logs-reports/activity-logs', name: 'Activity Logs', element: ActivityLogs },
  { path: '/logs-reports/reports', name: 'Reports', element: Reports }
]

export default routes
