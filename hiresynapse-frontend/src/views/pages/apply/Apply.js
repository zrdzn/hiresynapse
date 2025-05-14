import React, {useEffect, useState} from 'react'
import {
  CButton,
  CCard,
  CCardBody,
  CCol,
  CContainer,
  CForm,
  CFormInput,
  CInputGroup,
  CInputGroupText,
  CRow,
  CSpinner,
} from '@coreui/react'
import CIcon from '@coreui/icons-react'
import {cilAt, cilFile} from '@coreui/icons'
import {useNavigate, useParams} from "react-router-dom";
import {candidateService} from "../../../services/candidateService";
import {toast} from "react-toastify";
import {jobService} from "../../../services/jobService";

const Apply = () => {
  const { jobId } = useParams()
  const [job, setJob] = useState(null)
  const [loading, setLoading] = useState(false)
  const [candidateCreateRequest, setCandidateCreateRequest] = useState({
    email: '',
    jobId: jobId
  });
  const navigate = useNavigate()
  const [resumeFile, setResumeFile] = useState(null)

  useEffect(() => {
    jobService.getJob(jobId)
      .then(response => {
        setJob(response.data);
        setLoading(false);
      })
      .catch(err => {
        console.error(err)
        navigate('/#/apply')
      })
  }, [jobId]);

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

  const handleFileInputChange = (event) => {
    const file = event.target.files?.item(0) ?? null
    if (file != null) {
      setResumeFile(file)
    }
  }

  const handleFieldsChange = (event) => {
    console.log(event.target.name)
    setCandidateCreateRequest({
      ...candidateCreateRequest,
      [event.target.name]: event.target.value
    })
  }

  const handleCandidateCreate = (event) => {
    event.preventDefault()

    if (candidateCreateRequest.email === '') {
      toast.error("You need to provide an email")
      return
    }

    if (resumeFile === null) {
      toast.error("You need to upload your resume")
      return
    }

    candidateService.createCandidate(candidateCreateRequest, resumeFile)
      .then(() => {
        toast.success("Your application has been submitted successfully")
        navigate('/#/apply')
      })
      .catch(err => {
        toast.error("There was an error submitting your application")
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }

  return (
    <div className="bg-body-tertiary min-vh-100 d-flex flex-row align-items-center">
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={9} lg={7} xl={6}>
            <CCard className="mx-4">
              <CCardBody className="p-4">
                <CForm>
                  <div className="text-center mb-4">
                    <h1 className="mb-2 fw-bold">Apply for {job?.title ?? "Unknown" }</h1>
                    <p className="text-body-secondary">Complete your application below</p>
                  </div>

                  <div className="mb-1">
                    <label className="form-label fw-semibold">
                      Email <span className="text-danger">*</span>
                    </label>
                  </div>
                  <CInputGroup className="mb-4">
                    <CInputGroupText className="bg-light">
                      <CIcon icon={cilAt} />
                    </CInputGroupText>
                    <CFormInput
                      name="email"
                      placeholder="your.email@example.com"
                      autoComplete="email"
                      className="py-2"
                      onChange={handleFieldsChange}
                      required
                    />
                  </CInputGroup>

                  <div className="mb-1">
                    <label className="form-label fw-semibold">
                      Resume <span className="text-danger">*</span>
                    </label>
                  </div>
                  <CInputGroup className="mb-4">
                    <CInputGroupText className="bg-light">
                      <CIcon icon={cilFile} />
                    </CInputGroupText>
                    <CFormInput
                      type="file"
                      id="inputGroupFile01"
                      className="py-2"
                      accept=".pdf,.doc,.docx"
                      required
                      onChange={handleFileInputChange}
                      aria-describedby="fileHelp"
                    />
                  </CInputGroup>
                  <div id="fileHelp" className="form-text mb-4">
                    Supported formats: .pdf, .doc, .docx
                  </div>

                  <div className="d-grid gap-2">
                    <CButton
                      color="success"
                      size="lg"
                      className="px-4 py-2 text-white fw-bold"
                      type="submit"
                      onClick={handleCandidateCreate}
                    >
                      Submit application
                    </CButton>
                  </div>

                  <div className="text-center mt-3">
                    <small className="text-muted">
                      By submitting, you agree to our <a href="#">Terms & Conditions</a>
                    </small>
                  </div>
                </CForm>
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Apply
