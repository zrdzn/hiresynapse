import React, {useEffect, useState} from 'react'
import classNames from 'classnames'

import {
  CButton,
  CButtonGroup,
  CCard,
  CCardBody,
  CCardFooter,
  CCardHeader,
  CCol,
  CProgress,
  CRow,
  CSpinner,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilCalendar, cilLocationPin} from '@coreui/icons'
import Widgets from '../widgets/Widgets'
import MainChart from './MainChart'
import {statService} from "../../services/statService";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

const Dashboard = () => {
  const [stats, setStats] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    statService.getStats()
      .then(response => {
        setStats(response.data);
        setLoading(false);
      })
      .catch(err => console.error(err))
  }, []);

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

  const progressExample = [
    { title: 'Accepted', value: '2,703 Candidates', percent: 40, color: 'success' },
    { title: 'Rejected', value: '2,093 Candidates', percent: 78, color: 'danger' },
    { title: 'Interviews', value: '487 Candidates', percent: 18, color: 'warning' },
    { title: 'Offers Extended', value: '123 Candidates', percent: 4, color: 'danger' },
    { title: 'Acceptance Rate', value: 'Average Rate', percent: 82.5, color: 'primary' },
  ]

  return (
    <>
      <Widgets stats={stats} className="mb-4" />
      <CCard className="mb-4">
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="candidates" className="card-title mb-0">
                Candidate flow
              </h4>
              <div className="small text-body-secondary">January - July 2025</div>
            </CCol>
            <CCol sm={7} className="d-none d-md-block">
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
            <CCardHeader>Recruitment</CCardHeader>
            <CCardBody>
              <CRow>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-info py-1 px-3">
                        <div className="text-body-secondary text-truncate small">Jobs</div>
                        <div className="fs-5 fw-semibold">{stats.jobCount}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">
                          Candidates
                        </div>
                        <div className="fs-5 fw-semibold">{stats.candidateCount}</div>
                      </div>
                    </CCol>
                  </CRow>
                  <hr className="mt-0" />

                  <h6 className="mb-3">Application sources</h6>
                  {stats.utmSourceCount.length > 0 ? (
                    stats.utmSourceCount.map((item, index) => (
                      <div className="progress-group" key={index}>
                        <div className="progress-group-header">
                          <CIcon className="me-2" icon={cilLocationPin} size="lg" />
                          <span>{item.title}</span>
                          <span className="ms-auto fw-semibold">
                            {item.count} <span className="text-body-secondary small">({item.percentage.toFixed(2)}%)</span>
                          </span>
                        </div>
                        <div className="progress-group-bars">
                          <CProgress thin color="success" value={item.percentage} />
                        </div>
                      </div>
                    ))
                  ) : (
                    <p className="text-center text-body-secondary">No data yet</p>
                  )}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Interview statuses</h6>
                  {stats.interviewStatusCount.length > 0 ? (
                    stats.interviewStatusCount.map((item, index) => (
                      <div className="progress-group" key={index}>
                        <div className="progress-group-header">
                          <CIcon className="me-2" icon={cilCalendar} size="lg" />
                          <span>{capitalize(item.status)}</span>
                          <span className="ms-auto fw-semibold">
                            {item.count} <span className="text-body-secondary small">({item.percentage.toFixed(2)}%)</span>
                          </span>
                        </div>
                        <div className="progress-group-bars">
                          <CProgress thin color="success" value={item.percentage} />
                        </div>
                      </div>
                    ))
                  ) : (
                    <p className="text-center text-body-secondary">No data yet</p>
                  )}
                </CCol>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Interviews</div>
                        <div className="fs-5 fw-semibold">{stats.interviewCount}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-success py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Staff</div>
                        <div className="fs-5 fw-semibold">{stats.userCount}</div>
                      </div>
                    </CCol>
                  </CRow>

                  <hr className="mt-0" />

                  <h6 className="mb-3">Job applicants</h6>
                  {stats.jobTitleCount.length > 0 ? (
                    stats.jobTitleCount.map((item, index) => (
                      <div className="progress-group" key={index}>
                        <div className="progress-group-header">
                          <CIcon className="me-2" icon={cilCalendar} size="lg" />
                          <span>{item.title}</span>
                          <span className="ms-auto fw-semibold">
                            {item.count} <span className="text-body-secondary small">({item.percentage.toFixed(2)}%)</span>
                          </span>
                        </div>
                        <div className="progress-group-bars">
                          <CProgress thin color="success" value={item.percentage} />
                        </div>
                      </div>
                    ))
                  ) : (
                    <p className="text-center text-body-secondary">No data yet</p>
                  )}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Interview types</h6>
                  {stats.interviewTypeCount.length > 0 ? (
                    stats.interviewTypeCount.map((item, index) => (
                      <div className="progress-group" key={index}>
                        <div className="progress-group-header">
                          <CIcon className="me-2" icon={cilCalendar} size="lg" />
                          <span>{capitalize(item.interviewType)}</span>
                          <span className="ms-auto fw-semibold">
                            {item.count} <span className="text-body-secondary small">({item.percentage.toFixed(2)}%)</span>
                          </span>
                        </div>
                        <div className="progress-group-bars">
                          <CProgress thin color="success" value={item.percentage} />
                        </div>
                      </div>
                    ))
                  ) : (
                    <p className="text-center text-body-secondary">No data yet</p>
                  )}
                </CCol>
              </CRow>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </>
  )
}

export default Dashboard
