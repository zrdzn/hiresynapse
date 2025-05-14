import React, {useEffect, useState} from 'react'
import {
  CBadge,
  CButton,
  CCard,
  CCardBody,
  CCardFooter,
  CCardHeader,
  CCardSubtitle,
  CCardText,
  CCardTitle,
  CCol,
  CListGroup,
  CListGroupItem,
  CRow,
  CSpinner,
} from '@coreui/react'
import {jobService} from "../../../services/jobService";
import {CiClock1, CiDollar, CiLocationOn} from "react-icons/ci";
import {useDateFormatter} from "../../../hooks/useDateFormatter";
import {FiBriefcase} from "react-icons/fi";

const ApplyOffers = () => {
  const { formatDate } = useDateFormatter()
  const [jobs, setJobs] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    jobService.getPublishedJobs()
      .then((res) => {
        setJobs(res.data);
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
    <div className="p-4">
      <CRow>
        <CCol>
          <h1 className="mb-4">Job Openings</h1>
        </CCol>
        <CCol className="text-end">
          <CButton color="primary" href="#/dashboard">
            Sign in
          </CButton>
        </CCol>
      </CRow>
      <CRow>
        {jobs.map((job) => (
          <CCol sm={12} lg={4} className="mb-4" key={job.id}>
            <CCard className={`h-100 border-top-3 border-primary`}>
              <CCardHeader className="d-flex justify-content-between align-items-center">
                <div>
                  <CCardTitle>{job.title}</CCardTitle>
                  <CCardSubtitle className="mt-1 text-medium-emphasis">
                    <CiLocationOn/> {job.location}
                  </CCardSubtitle>
                </div>
                <CBadge color="primary">{job.type}</CBadge>
              </CCardHeader>

              <CCardBody>
                <div className="mb-3">
                  <div className="d-flex align-items-center mb-2">
                    <CiDollar className="me-2" size={20}/>
                    <strong>Salary Range:</strong>
                  </div>
                  <CCardText className="ms-4">{job.salary}</CCardText>
                </div>

                <div className="mb-3">
                  <div className="d-flex align-items-center mb-2">
                    <FiBriefcase className="me-2" size={20}/>
                    <strong>Description:</strong>
                  </div>
                  <CCardText className="ms-4">{job.description}</CCardText>
                </div>

                <div className="mb-3">
                  <strong>Requirements:</strong>
                  <CListGroup flush className="mt-2">
                    {job.requirements.map((req, index) => (
                      <CListGroupItem key={index}>{req}</CListGroupItem>
                    ))}
                  </CListGroup>
                </div>

                <div className="mb-3">
                  <strong>Benefits:</strong>
                  <CListGroup flush className="mt-2">
                    {job.benefits.map((benefit, index) => (
                      <CListGroupItem key={index}>{benefit}</CListGroupItem>
                    ))}
                  </CListGroup>
                </div>
              </CCardBody>

              <CCardFooter className="d-flex justify-content-between align-items-center">
                <div className="d-flex align-items-center">
                  <CiClock1 className="me-1"/>
                  <small className="text-medium-emphasis">Posted {formatDate(job.createdAt, true)}</small>
                </div>
                <div className="d-flex align-items-center gap-3">
                  <CButton href={`/#/public/apply/${job.id}`} color="primary">Apply now</CButton>
                </div>
              </CCardFooter>
            </CCard>
          </CCol>
        ))}
      </CRow>
    </div>
  )
}

export default ApplyOffers
