import React from 'react'
import classNames from 'classnames'

import {CButton, CButtonGroup, CCard, CCardBody, CCardFooter, CCardHeader, CCol, CProgress, CRow,} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilBriefcase, cilCalendar, cilClock, cilCloudDownload, cilLocationPin} from '@coreui/icons'
import WidgetsDropdown from '../widgets/WidgetsDropdown'
import MainChart from './MainChart'

const Dashboard = () => {
  const progressExample = [
    { title: 'Applications', value: '2,703 Resumes', percent: 40, color: 'success' },
    { title: 'AI Analyzed', value: '2,093 Resumes', percent: 78, color: 'info' },
    { title: 'Interviews', value: '487 Candidates', percent: 18, color: 'warning' },
    { title: 'Offers Extended', value: '123 Candidates', percent: 4, color: 'danger' },
    { title: 'Acceptance Rate', value: 'Average Rate', percent: 82.5, color: 'primary' },
  ]

  // Top 4 locations by number of applicants
  const topLocations = [
    { title: 'Remote', icon: cilLocationPin, percent: 42, value: '1,136' },
    { title: 'New York', icon: cilLocationPin, percent: 28, value: '756' },
    { title: 'San Francisco', icon: cilLocationPin, percent: 18, value: '486' },
    { title: 'Other', icon: cilLocationPin, percent: 12, value: '325' },
  ]

  // Most popular job positions by number of applicants
  const topJobPositions = [
    { title: 'Software Engineer', icon: cilBriefcase, percent: 35, value: '946' },
    { title: 'Product Manager', icon: cilBriefcase, percent: 27, value: '730' },
    { title: 'UX Designer', icon: cilBriefcase, percent: 21, value: '568' },
    { title: 'Other', icon: cilBriefcase, percent: 17, value: '459' },
  ]

  // Time-to-hire for different departments
  const timeToHire = [
    { title: 'Engineering', icon: cilClock, percent: 75, value: '45 days' },
    { title: 'Design', icon: cilClock, percent: 58, value: '35 days' },
    { title: 'Marketing', icon: cilClock, percent: 42, value: '25 days' },
    { title: 'Sales', icon: cilClock, percent: 33, value: '20 days' },
  ]

  // Employee retention by tenure
  const employeeRetention = [
    { title: '< 1 year', icon: cilCalendar, percent: 15, value: '43 employees' },
    { title: '1-3 years', icon: cilCalendar, percent: 33, value: '95 employees' },
    { title: '3-5 years', icon: cilCalendar, percent: 30, value: '86 employees' },
    { title: '5+ years', icon: cilCalendar, percent: 22, value: '63 employees' },
  ]

  return (
    <>
      <WidgetsDropdown className="mb-4" />
      <CCard className="mb-4">
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="candidates" className="card-title mb-0">
                Candidate Flow
              </h4>
              <div className="small text-body-secondary">January - July 2025</div>
            </CCol>
            <CCol sm={7} className="d-none d-md-block">
              <CButton color="primary" className="float-end">
                <CIcon icon={cilCloudDownload} />
              </CButton>
              <CButtonGroup className="float-end me-3">
                {['Week', 'Month', 'Quarter'].map((value) => (
                  <CButton
                    color="outline-secondary"
                    key={value}
                    className="mx-0"
                    active={value === 'Month'}
                  >
                    {value}
                  </CButton>
                ))}
              </CButtonGroup>
            </CCol>
          </CRow>
          <MainChart />
        </CCardBody>
        <CCardFooter>
          <CRow
            xs={{ cols: 1, gutter: 4 }}
            sm={{ cols: 2 }}
            lg={{ cols: 4 }}
            xl={{ cols: 5 }}
            className="mb-2 text-center"
          >
            {progressExample.map((item, index, items) => (
              <CCol
                className={classNames({
                  'd-none d-xl-block': index + 1 === items.length,
                })}
                key={index}
              >
                <div className="text-body-secondary">{item.title}</div>
                <div className="fw-semibold text-truncate">
                  {item.value} ({item.percent}%)
                </div>
                <CProgress thin className="mt-2" color={item.color} value={item.percent} />
              </CCol>
            ))}
          </CRow>
        </CCardFooter>
      </CCard>
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
            <CCardHeader>Recruitment Analytics</CCardHeader>
            <CCardBody>
              <CRow>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-info py-1 px-3">
                        <div className="text-body-secondary text-truncate small">Reports Generated</div>
                        <div className="fs-5 fw-semibold">2,845</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">
                          AI Resume Analyses
                        </div>
                        <div className="fs-5 fw-semibold">1,763</div>
                      </div>
                    </CCol>
                  </CRow>
                  <hr className="mt-0" />

                  <h6 className="mb-3">Top Locations</h6>
                  {topLocations.map((item, index) => (
                    <div className="progress-group" key={index}>
                      <div className="progress-group-header">
                        <CIcon className="me-2" icon={item.icon} size="lg" />
                        <span>{item.title}</span>
                        <span className="ms-auto fw-semibold">
                          {item.value}{' '}
                          <span className="text-body-secondary small">({item.percent}%)</span>
                        </span>
                      </div>
                      <div className="progress-group-bars">
                        <CProgress thin color="info" value={item.percent} />
                      </div>
                    </div>
                  ))}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Time-to-Hire by Department</h6>
                  {timeToHire.map((item, index) => (
                    <div className="progress-group" key={index}>
                      <div className="progress-group-header">
                        <CIcon className="me-2" icon={item.icon} size="lg" />
                        <span>{item.title}</span>
                        <span className="ms-auto fw-semibold">
                          {item.value}{' '}
                          <span className="text-body-secondary small">({item.percent}%)</span>
                        </span>
                      </div>
                      <div className="progress-group-bars">
                        <CProgress thin color="danger" value={item.percent} />
                      </div>
                    </div>
                  ))}
                </CCol>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Active Job Posts</div>
                        <div className="fs-5 fw-semibold">43</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-success py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Total Employees</div>
                        <div className="fs-5 fw-semibold">287</div>
                      </div>
                    </CCol>
                  </CRow>

                  <hr className="mt-0" />

                  <h6 className="mb-3">Top Job Positions</h6>
                  {topJobPositions.map((item, index) => (
                    <div className="progress-group" key={index}>
                      <div className="progress-group-header">
                        <CIcon className="me-2" icon={item.icon} size="lg" />
                        <span>{item.title}</span>
                        <span className="ms-auto fw-semibold">
                          {item.value}{' '}
                          <span className="text-body-secondary small">({item.percent}%)</span>
                        </span>
                      </div>
                      <div className="progress-group-bars">
                        <CProgress thin color="warning" value={item.percent} />
                      </div>
                    </div>
                  ))}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Employee Retention by Tenure</h6>
                  {employeeRetention.map((item, index) => (
                    <div className="progress-group" key={index}>
                      <div className="progress-group-header">
                        <CIcon className="me-2" icon={item.icon} size="lg" />
                        <span>{item.title}</span>
                        <span className="ms-auto fw-semibold">
                          {item.value}{' '}
                          <span className="text-body-secondary small">({item.percent}%)</span>
                        </span>
                      </div>
                      <div className="progress-group-bars">
                        <CProgress thin color="success" value={item.percent} />
                      </div>
                    </div>
                  ))}
                </CCol>
              </CRow>

              <br />
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </>
  )
}

export default Dashboard
