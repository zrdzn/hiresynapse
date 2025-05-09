import React, {useEffect, useState} from 'react'

import {CCard, CCardBody, CCardFooter, CCardHeader, CCol, CProgress, CRow, CSpinner,} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilCalendar, cilLocationPin} from '@coreui/icons'
import Widgets from '../widgets/Widgets'
import MainChart from './MainChart'
import {statisticService} from "../../services/statisticService";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

const Dashboard = () => {
  const [stats, setStats] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    statisticService.getStats()
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

  return (
    <>
      <Widgets stats={stats} className="mb-4" />
      <CCard className="mb-4">
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="candidates" className="card-title mb-0">
                Analytics flow
              </h4>
            </CCol>
          </CRow>
          <MainChart stats={stats} />
        </CCardBody>
        <CCardFooter>
          <CRow
            xs={{ cols: 1, gutter: 4 }}
            sm={{ cols: 2 }}
            lg={{ cols: 4 }}
            xl={{ cols: 5 }}
            className="mb-2 text-center"
          >
            <CCol>
              <div className="text-body-secondary">Accepted candidates</div>
              <div className="fw-semibold text-truncate">
                {stats.acceptedCandidatesFromLastSixMonths.total}
              </div>
              <CProgress
                thin
                className="mt-2"
                color="success"
                value={stats.acceptedCandidatesFromLastSixMonths.total / stats.candidateCount * 100}
              />
            </CCol>
            <CCol>
              <div className="text-body-secondary">Rejected candidates</div>
              <div className="fw-semibold text-truncate">
                {stats.rejectedCandidatesFromLastSixMonths.total}
              </div>
              <CProgress
                thin
                className="mt-2"
                color="danger"
                value={stats.rejectedCandidatesFromLastSixMonths.total / stats.candidateCount * 100}
              />
            </CCol>
            <CCol>
              <div className="text-body-secondary">Pending candidates</div>
              <div className="fw-semibold text-truncate">
                {stats.pendingCandidatesFromLastSixMonths.total}
              </div>
              <CProgress
                thin
                className="mt-2"
                color="info"
                value={stats.pendingCandidatesFromLastSixMonths.total / stats.candidateCount * 100}
              />
            </CCol>
            <CCol>
              <div className="text-body-secondary">Interviews scheduled</div>
              <div className="fw-semibold text-truncate">
                {stats.scheduledInterviewsFromLastSixMonths.total}
              </div>
              <CProgress
                thin
                className="mt-2"
                color="warning"
                value={stats.scheduledInterviewsFromLastSixMonths.total / stats.interviewCount * 100}
              />
            </CCol>
            <CCol>
              <div className="text-body-secondary">Interviews completed</div>
              <div className="fw-semibold text-truncate">
                {stats.completedInterviewsFromLastSixMonths.total}
              </div>
              <CProgress
                thin
                className="mt-2"
                color="primary"
                value={stats.completedInterviewsFromLastSixMonths.total / stats.interviewCount * 100}
              />
            </CCol>
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
