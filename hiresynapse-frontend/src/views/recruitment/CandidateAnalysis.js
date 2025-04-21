import React, {useEffect, useState} from 'react'
import classNames from 'classnames'

import {
  CBadge,
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
import * as icons from '@coreui/icons'
import {cilBriefcase, cilCode, cilEducation} from '@coreui/icons'
import {useParams} from "react-router-dom";
import {candidateService} from "../../services/candidateService";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

const CandidateAnalysis = () => {
  const { candidateId } = useParams()
  const [candidate, setCandidate] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    candidateService.getCandidate(candidateId)
      .then((res) => {
        setCandidate(res.data);
        setLoading(false);
      })
      .catch(err => console.error(err))
  }, [candidateId]);

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

  return (
    <>
      <CCard className="mb-4">
        <CCardBody>
          <CRow>
            <CCol sm={5}>
              <h4 id="candidate-profile" className="card-title mb-0">
                {candidate.firstName} {candidate.lastName}
              </h4>
              <div className="small text-body-secondary">{candidate.job.title} - {candidate.job.location}</div>
            </CCol>
            <CCol sm={7} className="d-none d-md-block">
              <CButtonGroup className="float-end me-3">
                {['Profile', 'Interview'].map((value) => (
                  <CButton
                    color="outline-secondary"
                    key={value}
                    className="mx-0"
                    active={value === 'Profile'}
                  >
                    {value}
                  </CButton>
                ))}
              </CButtonGroup>
            </CCol>
          </CRow>
        </CCardBody>
        <CCardFooter>
          <CRow
            xs={{ cols: 1, gutter: 3 }}
            sm={{ cols: 2 }}
            lg={{ cols: 3 }}
            xl={{ cols: 3 }}
            className="mb-2 text-center"
          >
            {[
              { title: 'Application status', value: capitalize(candidate.status), color: 'warning' },
              { title: 'Years of experience', value: candidate.yearsOfExperience, color: 'success' },
              { title: 'Languages', value: candidate.languages.length, color: 'info' },
            ].map((item, index, items) => (
              <CCol
                className={classNames({
                  'd-none d-xl-block': index + 1 === items.length,
                })}
                key={index}
              >
                <div className="text-body-secondary">{item.title}</div>
                <div className="fw-semibold text-truncate">
                  {item.value}
                </div>
              </CCol>
            ))}
          </CRow>
        </CCardFooter>
      </CCard>
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
            <CCardHeader>Candidate profile</CCardHeader>
            <CCardBody>
              <CRow>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-info py-1 px-3">
                        <div className="text-body-secondary text-truncate small">Email</div>
                        <div className="fs-5 fw-semibold">{candidate.email}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Phone</div>
                        <div className="fs-5 fw-semibold">{candidate.phone}</div>
                      </div>
                    </CCol>
                  </CRow>
                  <hr className="mt-0" />

                  <h6 className="mb-3">Career</h6>
                  {Object.entries(candidate.experience).map(([position, period], index) => (
                    <div className="progress-group" key={index}>
                      <div className="progress-group-header">
                        <CIcon className="me-2" icon={cilBriefcase} size="lg" />
                        <span>{position}</span>
                        <span className="ms-auto fw-semibold">
                        {period}
                      </span>
                      </div>
                      <div className="progress-group-bars">
                        <CProgress thin color="info" value={100} />
                      </div>
                    </div>
                  ))}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Education</h6>
                  {Object.entries(candidate.education).map(([degree, details], index) => (
                    <div className="progress-group" key={index}>
                      <div className="progress-group-header">
                        <CIcon className="me-2" icon={cilEducation} size="lg" />
                        <span>{degree}</span>
                        <span className="ms-auto fw-semibold">
                        {details}
                      </span>
                      </div>
                      <div className="progress-group-bars">
                        <CProgress thin color="danger" value={100} />
                      </div>
                    </div>
                  ))}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Languages</h6>
                  {candidate.languages.map((lang, index) => (
                    <>
                      <CIcon
                        key={index}
                        size="xl"
                        icon={icons[`cif${capitalize(lang === 'en' ? 'GB' : lang)}`]}
                        title={lang}
                        className="me-1"
                      />
                    </>
                  )).reduce((prev, curr) => [prev, ' ', curr])}
                </CCol>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={12}>
                      <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Executive Summary</div>
                        <div className="small">{candidate.analysedSummary}</div>
                      </div>
                    </CCol>
                  </CRow>

                  <hr className="mt-0" />

                  <h6 className="mb-3">Skills</h6>
                  {Object.entries(candidate.skills)
                    .map(([skill, level], index) => (
                      <div className="progress-group" key={index}>
                        <div className="progress-group-header">
                          <CIcon className="me-2" icon={cilCode} size="lg" />
                          <span>{skill}</span>
                          <span className="ms-auto fw-semibold">
                          {level === 'good' ? 'Good' : 'Basic'}
                        </span>
                        </div>
                        <div className="progress-group-bars">
                          <CProgress thin color={level === 'good' ? "warning" : "success"} value={level === 'good' ? 66 : 33} />
                        </div>
                      </div>
                    ))}

                  <div className="mb-5"></div>

                  <h6 className="mb-3">Soft skills</h6>
                  {candidate.keySoftSkills.map((skill, index) => (
                    <CBadge key={index} color="success" className="me-1 mb-1">
                      {skill.charAt(0).toUpperCase() + skill.slice(1)}
                    </CBadge>
                  ))}
                </CCol>
              </CRow>

              <br />

              <CRow>
                <CCol xs={12}>
                  <CCard className="mb-4">
                    <CCardHeader>Job information</CCardHeader>
                    <CCardBody>
                      <CRow>
                        <CCol md={6}>
                          <h6>Good to have skills</h6>
                          {candidate.job.requirements.map((req, index) => {
                            const hasSkill = Object.keys(candidate.skills).some(
                              skill => skill.toLowerCase().includes(req.toLowerCase())
                            );
                            return (
                              <CBadge key={index} color={hasSkill ? "success" : "danger"} className="me-1 mb-1">
                                {req} {hasSkill ? '✓' : '✗'}
                              </CBadge>
                            );
                          })}
                        </CCol>
                        <CCol md={6}>
                          <h6>Benefits</h6>
                          {candidate.job.benefits.map((benefit, index) => (
                            <CBadge key={index} color="info" className="me-1 mb-1">
                              {benefit}
                            </CBadge>
                          ))}
                        </CCol>
                      </CRow>
                    </CCardBody>
                  </CCard>
                </CCol>
              </CRow>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </>
  )
}

export default CandidateAnalysis
