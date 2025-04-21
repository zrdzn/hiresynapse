import React, {useEffect, useState} from 'react'

import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CProgress,
  CRow,
  CSpinner,
  CTable,
  CTableBody,
  CTableDataCell,
  CTableHead,
  CTableHeaderCell,
  CTableRow,
  CTooltip,
} from '@coreui/react'
import CIcon from '@coreui/icons-react';
import * as icons from '@coreui/icons';

import {candidateService} from "../../services/candidateService";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

const Candidates = () => {
  const [candidates, setCandidates] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    candidateService.getCandidates()
      .then((res) => {
        setCandidates(res.data);
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
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
            <CCardHeader>
              <span>Candidates who applied for jobs</span>
            </CCardHeader>
            <CCardBody>
              <CRow>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-info py-1 px-3">
                        <div className="text-body-secondary text-truncate small">All</div>
                        <div className="fs-5 fw-semibold">{candidates.length}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Rejected</div>
                        <div className="fs-5 fw-semibold">{candidates.filter(task => task.status === 'REJECTED').length}</div>
                      </div>
                    </CCol>
                  </CRow>
                </CCol>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Pending</div>
                        <div className="fs-5 fw-semibold">{candidates.filter(task => task.status === 'PENDING').length}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-success py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Accepted</div>
                        <div className="fs-5 fw-semibold">{candidates.filter(task => task.status === 'ACCEPTED').length}</div>
                      </div>
                    </CCol>
                  </CRow>
                </CCol>
              </CRow>
              <CTable align="middle" className="mb-0 border" hover responsive>
                <CTableHead className="text-nowrap">
                  <CTableRow>
                    <CTableHeaderCell className="bg-body-tertiary">Candidate</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Languages</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Job</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Experience</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Status</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-end">Actions</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {candidates.map((item, index) => (
                    <CTableRow v-for="item in tableItems" key={index}>
                      <CTableDataCell>
                        <div>{item.firstName} {item.lastName}</div>
                        <div className="small text-body-secondary text-nowrap">
                          <span>{item.email}</span> | {item.phone}
                        </div>
                      </CTableDataCell>
                      <CTableDataCell>
                        {item.languages.map((lang, index) => (
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
                      </CTableDataCell>
                      <CTableDataCell>Software Engineer</CTableDataCell>
                      <CTableDataCell>
                        <CTooltip
                          style={{
                            minWidth: '200px',
                          }}
                          content={Object.entries(item.relatedExperience).map(([key, value]) => (
                            <div key={key}>{key}: {value}</div>
                          ))}
                        >
                          <span>{item.yearsOfRelatedExperience} years</span>
                        </CTooltip>
                      </CTableDataCell>
                      <CTableDataCell>
                        <div className="fw-semibold text-center mb-1">{capitalize(item.status)}</div>
                        <CProgress thin
                                   color={item.status === 'REJECTED' ? 'danger' : item.status === 'PENDING' ? 'warning' : 'success'}
                                   value="100" />
                      </CTableDataCell>
                      <CTableDataCell className="text-end">
                          <CButton className="text-white bg-success" size="sm">
                            Accept
                          </CButton>
                          <CButton href={`/#/recruitment/candidates/${item.id}`} className="text-white bg-primary mx-1" size="sm">
                            Analysis
                          </CButton>
                          <CButton className="text-white bg-danger" size="sm">
                            Reject
                          </CButton>
                      </CTableDataCell>
                    </CTableRow>
                  ))}
                </CTableBody>
              </CTable>
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
    </>
  )
}

export default Candidates
