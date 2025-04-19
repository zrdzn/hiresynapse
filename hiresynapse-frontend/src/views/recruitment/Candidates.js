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
} from '@coreui/react'
import CIcon from '@coreui/icons-react'

import {candidateService} from "../../services/candidateService";

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
                    <CTableHeaderCell className="bg-body-tertiary text-center">
                      Job
                    </CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Score</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">
                      Status
                    </CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary">Actions</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {candidates.map((item, index) => (
                    <CTableRow v-for="item in tableItems" key={index}>
                      <CTableDataCell>
                        <div>{item.firstName} {item.lastName}</div>
                        <div className="small text-body-secondary text-nowrap">
                          <span>zrdzn9@gmail.com</span> | 123-312-412
                        </div>
                      </CTableDataCell>
                      <CTableDataCell className="text-center">
                        {item.languages.map((lang, index) => (
                          <CIcon
                            key={index}
                            size="xl"
                            icon={`cif${lang.icon}`}
                            title={lang.name}
                            className="me-1"
                          />
                        )).reduce((prev, curr) => [prev, ', ', curr])}
                      </CTableDataCell>
                      <CTableDataCell className="text-center">Software Engineer</CTableDataCell>
                      <CTableDataCell>
                        <div className="fw-semibold text-center mb-1">{item.matchScore}%</div>
                        <CProgress thin color={"success"} value={item.matchScore} />
                      </CTableDataCell>
                      <CTableDataCell className="text-center">Pending</CTableDataCell>
                      <CTableDataCell>
                          <CButton className="text-white bg-success" size="sm">
                            Accept
                          </CButton>
                          <CButton className="text-white bg-primary mx-1" size="sm">
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
