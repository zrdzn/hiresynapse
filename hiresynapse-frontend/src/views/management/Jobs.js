import React, {useEffect, useState} from 'react'

import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CDropdown,
  CDropdownItem,
  CDropdownMenu,
  CDropdownToggle,
  CForm,
  CFormInput,
  CFormSelect,
  CFormTextarea,
  CModal,
  CModalBody,
  CModalFooter,
  CModalHeader,
  CModalTitle,
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

import {jobService} from "../../services/jobService";
import {
  FiArchive,
  FiBriefcase,
  FiCalendar,
  FiClock,
  FiEdit,
  FiMoreHorizontal,
  FiSend,
  FiTrash2,
  FiUpload,
  FiX,
  FiZap
} from "react-icons/fi";
import {toast} from "react-toastify";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

const Jobs = () => {
  const [jobs, setJobs] = useState([]);
  const [createJobRequest, setCreateJobRequest] = useState({
    title: '',
    description: '',
    location: '',
    salary: '',
    requiredExperience: '',
    status: 'PUBLISHED',
    requirements: [],
    benefits: [],
  });
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(false);
  const [titleError, setTitleError] = useState(false);
  const [descriptionError, setDescriptionError] = useState(false);
  const [locationError, setLocationError] = useState(false);
  const [requirementsInput, setRequirementsInput] = useState('');
  const [benefitsInput, setBenefitsInput] = useState('');

  useEffect(() => {
    jobService.getJobs()
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

  const handleFieldsChange = (event) => {
    if (event.target.name === 'title') {
      if (event.target.value.length < 3 || event.target.value.length > 50) {
        setTitleError(true);
      } else {
        setTitleError(false);
      }
    }

    if (event.target.name === 'description') {
      if (event.target.value.length < 3 || event.target.value.length > 1000) {
        setDescriptionError(true);
      } else {
        setDescriptionError(false);
      }
    }

    if (event.target.name === 'location') {
      if (event.target.value.length < 1) {
        setLocationError(true);
      } else {
        setLocationError(false);
      }
    }

    if (event.target.name === 'requirements') {
      setRequirementsInput(event.target.value);
      return;
    }

    if (event.target.name === 'benefits') {
      setBenefitsInput(event.target.value);
      return;
    }

    setCreateJobRequest({
      ...createJobRequest,
      [event.target.name]: event.target.value
    })
  }

  const handleJobCreate = () => {
    const requirementsArray = requirementsInput
      .split(',')
      .map(item => item.trim())
      .filter(item => item !== '');

    const benefitsArray = benefitsInput
      .split(',')
      .map(item => item.trim())
      .filter(item => item !== '');

    const finalRequest = {
      ...createJobRequest,
      requirements: requirementsArray,
      benefits: benefitsArray
    };

    jobService.createJob(finalRequest)
      .then((res) => {
        setJobs(res.data);
        setModal(false);
      })
      .catch(err => {
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }

  const handleJobDelete = (id) => {
    jobService.deleteJob(id)
      .then((res) => {
        setJobs(res.data);
        toast.success('Job deleted successfully')
      })
      .catch(err => {
        console.error(err)
        toast.error('Could not delete job')
      })
  }

  return (
    <>
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
            <CCardHeader>
              <div className="d-flex w-100 justify-content-between align-items-center">
                <span>Manage jobs</span>
                <CButton color="primary"
                         className="d-flex align-items-center"
                         onClick={() => setModal(!modal)}>
                  <FiBriefcase className="me-1" size={16} />
                  Create new
                </CButton>
              </div>
              <CModal
                visible={modal}
                onClose={() => setModal(false)}
              >
                <CModalHeader>
                  <CModalTitle>Create new job</CModalTitle>
                </CModalHeader>
                <CModalBody>
                  <CForm onChange={handleFieldsChange}>
                    <CFormInput
                      name="title"
                      type="text"
                      label="Title"
                      placeholder="Software Engineer..."
                      text="Must be 3-50 characters long"
                      required
                      valid={createJobRequest.title !== '' && !titleError}
                      invalid={titleError}
                    />
                    <br/>
                    <CFormTextarea
                      name="description"
                      type="text"
                      label="Description"
                      placeholder="Job description"
                      rows={3}
                      text="Must be 3-1000 characters long"
                      required
                      valid={createJobRequest.description !== '' && !descriptionError}
                      invalid={descriptionError}
                    ></CFormTextarea>
                    <br/>
                    <CFormInput
                      name="location"
                      type="text"
                      label="Location"
                      placeholder="Amsterdam / Warsaw / Remote..."
                      text="Must not be empty"
                      required
                      valid={createJobRequest.location !== '' && !locationError}
                      invalid={locationError}
                    />
                    <br/>
                    <CFormInput
                      name="salary"
                      type="text"
                      label="Estimated salary"
                      placeholder="$10000"
                    />
                    <br/>
                    <CFormInput
                      name="requiredExperience"
                      type="text"
                      label="Required experience"
                      placeholder="2 years"
                    />
                    <br/>
                    <CFormSelect name="status"
                                 label="Status on create">
                      <option disabled>Choose...</option>
                      <option>PUBLISHED</option>
                      <option>SCHEDULED</option>
                      <option>UNPUBLISHED</option>
                    </CFormSelect>
                    <br/>
                    <CFormInput
                      name="requirements"
                      type="text"
                      label="Requirements"
                      placeholder="Python, Java, C++..."
                    />
                    <br/>
                    <CFormInput
                      name="benefits"
                      type="text"
                      label="Benefits"
                      placeholder="Free lunch, Gym membership..."
                    />
                  </CForm>
                </CModalBody>
                <CModalFooter>
                  <CButton color="secondary" onClick={() => setModal(false)}>
                    Close
                  </CButton>
                  <CButton color="primary" onClick={() => handleJobCreate()}>Save changes</CButton>
                </CModalFooter>
              </CModal>
            </CCardHeader>
            <CCardBody>
              <CRow>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-info py-1 px-3">
                        <div className="text-body-secondary text-truncate small">All</div>
                        <div className="fs-5 fw-semibold">{jobs.length}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Unpublished</div>
                        <div className="fs-5 fw-semibold">{jobs.filter(task => task.status === 'UNPUBLISHED').length}</div>
                      </div>
                    </CCol>
                  </CRow>
                </CCol>
                <CCol xs={12} md={6} xl={6}>
                  <CRow>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Scheduled</div>
                        <div className="fs-5 fw-semibold">{jobs.filter(task => task.status === 'SCHEDULED').length}</div>
                      </div>
                    </CCol>
                    <CCol xs={6}>
                      <div className="border-start border-start-4 border-start-success py-1 px-3 mb-3">
                        <div className="text-body-secondary text-truncate small">Published</div>
                        <div className="fs-5 fw-semibold">{jobs.filter(task => task.status === 'PUBLISHED').length}</div>
                      </div>
                    </CCol>
                  </CRow>
                </CCol>
              </CRow>
              <CTable align="middle" className="mb-0 border" hover responsive>
                <CTableHead className="text-nowrap">
                  <CTableRow>
                    <CTableHeaderCell className="bg-body-tertiary">ID</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">Title</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">Salary</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">Status</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-end">Actions</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {jobs.map((item, index) => (
                    <CTableRow v-for="item in tableItems" key={index}>
                      <CTableDataCell>
                        <div className="text-nowrap">{item.id}</div>
                      </CTableDataCell>
                      <CTableDataCell className="text-center">
                        <div className="fw-semibold text-nowrap">{item.title}</div>
                      </CTableDataCell>
                      <CTableDataCell className="text-center">
                        <div className="text-nowrap">{item.salary === '' ? 'Undisclosed' : item.salary}</div>
                      </CTableDataCell>
                      <CTableDataCell className="text-center">
                        <div className="fw-semibold text-center mb-1">{capitalize(item.status)}</div>
                        <CProgress thin
                                   color={item.status === 'UNPUBLISHED' ? 'danger' : item.status === 'SCHEDULED' ? 'warning' : 'success'}
                                   value="100" />
                      </CTableDataCell>
                      <CTableDataCell className="text-end">
                        {
                          item.status === 'UNPUBLISHED' &&
                          <CDropdown alignment="end">
                            <CDropdownToggle className="bg-success text-white me-1 rounded-1" caret={false} size="sm">
                              <FiUpload className="me-1" size={16} />
                              Publish
                            </CDropdownToggle>
                            <CDropdownMenu className="table-dropdown-fix">
                              <CDropdownItem href="#" className="d-flex align-items-center">
                                <FiSend className="me-2" size={16} />
                                Publish now
                              </CDropdownItem>
                              <CDropdownItem href="#" className="d-flex align-items-center">
                                <FiCalendar className="me-2" size={16} />
                                Schedule
                              </CDropdownItem>
                            </CDropdownMenu>
                          </CDropdown>
                        }

                        {
                          item.status === 'SCHEDULED' &&
                          <CDropdown alignment="end">
                            <CDropdownToggle className="bg-warning text-white me-1 rounded-1" caret={false} size="sm">
                              <FiCalendar className="me-1" size={16} />
                              Schedule
                            </CDropdownToggle>
                            <CDropdownMenu className="table-dropdown-fix">
                              <CDropdownItem href="#" className="d-flex align-items-center">
                                <FiClock className="me-2" size={16} />
                                Reschedule
                              </CDropdownItem>
                              <CDropdownItem href="#" className="d-flex align-items-center">
                                <FiZap className="me-2" size={16} />
                                Publish now
                              </CDropdownItem>
                              <CDropdownItem href="#" className="d-flex align-items-center">
                                <FiX className="me-2" size={16} />
                                Cancel
                              </CDropdownItem>
                            </CDropdownMenu>
                          </CDropdown>
                        }

                        {
                          item.status === 'PUBLISHED' &&
                          <CButton
                            className="text-white bg-danger me-1 align-items-center"
                            size="sm"
                          >
                            <FiArchive className="me-1" size={16} />
                            Unpublish
                          </CButton>
                        }

                        <CButton
                          className="text-white bg-info me-1 align-items-center"
                          size="sm"
                        >
                          <FiEdit className="me-1" size={16} />
                          Edit
                        </CButton>

                        <CDropdown alignment="end">
                          <CDropdownToggle color="light" className="rounded-1" caret={false} size="sm">
                            <FiMoreHorizontal size={16} />
                          </CDropdownToggle>
                          <CDropdownMenu className="table-dropdown-fix">
                            <CDropdownItem href="#" className="text-danger d-flex align-items-center">
                              <FiTrash2 className="me-2" size={16} />
                              Delete
                            </CDropdownItem>
                          </CDropdownMenu>
                        </CDropdown>
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

export default Jobs
