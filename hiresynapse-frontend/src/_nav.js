import React from 'react'
import CIcon from '@coreui/icons-react'
import {cilBell, cilCalendar, cilChartLine, cilFactory, cilSpeedometer, cilUser,} from '@coreui/icons'
import {CNavItem, CNavTitle} from '@coreui/react'

const _nav = [
  {
    component: CNavItem,
    name: 'Dashboard',
    to: '/dashboard/index',
    icon: <CIcon icon={cilSpeedometer} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: 'Recruitment',
  },
  {
    component: CNavItem,
    name: 'Candidates',
    to: '/dashboard/recruitment/candidates',
    icon: <CIcon icon={cilUser} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Interviews',
    to: '/dashboard/recruitment/interviews',
    icon: <CIcon icon={cilCalendar} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: 'Management',
  },
  {
    component: CNavItem,
    name: 'Jobs',
    to: '/dashboard/management/jobs',
    icon: <CIcon icon={cilFactory} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Staff',
    to: '/dashboard/management/staff',
    icon: <CIcon icon={cilUser} customClassName="nav-icon" />,
  },
  {
    component: CNavTitle,
    name: 'Logs & Reports',
  },
  {
    component: CNavItem,
    name: 'Activity Logs',
    to: '/dashboard/logs-reports/activity-logs',
    icon: <CIcon icon={cilBell} customClassName="nav-icon" />,
  },
  {
    component: CNavItem,
    name: 'Reports',
    to: '/dashboard/logs-reports/reports',
    icon: <CIcon icon={cilChartLine} customClassName="nav-icon" />,
  }
]

export default _nav
