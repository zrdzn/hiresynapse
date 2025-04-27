import React from 'react'
import CIcon from '@coreui/icons-react'
import {cilBell, cilCalendar, cilChartLine, cilFactory, cilSpeedometer, cilUser,} from '@coreui/icons'
import {CNavItem, CNavTitle} from '@coreui/react'

const _nav = [
  {
    component: CNavItem,
    name: 'Dashboard',
    to: '/dashboard',
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: 'Recruitment',
  },
  {
    component: CNavItem,
    name: 'Candidates',
    to: '/recruitment/candidates',
    icon: <CIcon icon={cilUser} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Interviews',
    to: '/recruitment/interviews',
    icon: <CIcon icon={cilCalendar} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: 'Management',
  },
  {
    component: CNavItem,
    name: 'Jobs',
    to: '/management/jobs',
    icon: <CIcon icon={cilFactory} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Users & Roles',
    to: '/management/users-roles',
    icon: <CIcon icon={cilUser} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: 'Logs & Reports',
  },
  {
    component: CNavItem,
    name: 'Activity Logs',
    to: '/logs-reports/activity-logs',
    icon: <CIcon icon={cilBell} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Reports',
    to: '/logs-reports/reports',
    icon: <CIcon icon={cilChartLine} customClassName="nav-icon" />,
  }
]

export default _nav
