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
  CModal,
  CModalBody,
  CModalHeader,
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
import {JobForm} from "../../components/JobForm";

const Jobs = () => {
  const [jobs, setJobs] = useState([]);
  const [jobStatus, setJobStatus] = useState('PUBLISHED');
  const [editJob, setEditJob] = useState(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [createJobRequest, setCreateJobRequest] = useState({
    title: '',
    description: '',
    location: '',
    salary: '',
    requiredExperience: '',
    status: jobStatus,
    requirements: '',
    benefits: '',
  });
  const [loading, setLoading] = useState(true);
  const [titleError, setTitleError] = useState(false);
  const [descriptionError, setDescriptionError] = useState(false);
  const [locationError, setLocationError] = useState(false);

  useEffect(() => {
    jobService.getJobs()
      .then(response => {
        setJobs(response.data);
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

    setCreateJobRequest({
      ...createJobRequest,
      [event.target.name]: event.target.value
    })
  }

  const handleJobCreate = () => {
    const finalRequest = {
      ...createJobRequest,
      status: jobStatus
    };

    jobService.createJob(finalRequest)
      .then(response => {
        setJobs([...jobs, response.data]);
        setCreateJobRequest({
          title: '',
          description: '',
          location: '',
          salary: '',
          requiredExperience: '',
          status: jobStatus,
          requirements: '',
          benefits: ''
        })
        toast.success(`Job ${response.data.title} created successfully`)
      })
      .catch(err => {
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }

  const handleJobPublish = (event, id) => {
    event.preventDefault()

    jobService.publishJob(id)
      .then(() => {
        setJobs([...jobs.map(job => job.id === id ? {...job, status: 'PUBLISHED'} : job)]);
        toast.success('Job published successfully')
      })
      .catch(err => {
        console.error(err)
        toast.error('Could not publish job')
      })
  }

  const handleJobUnpublish = (event, id) => {
    event.preventDefault()

    jobService.unpublishJob(id)
      .then(() => {
        setJobs([...jobs.map(job => job.id === id ? {...job, status: 'UNPUBLISHED'} : job)]);
        toast.success('Job unpublished successfully')
      })
      .catch(err => {
        console.error(err)
        toast.error('Could not unpublish job')
      })
  }

  const handleJobDelete = (event, id) => {
    event.preventDefault()

    jobService.deleteJob(id)
      .then(() => {
        setJobs([...jobs.filter(job => job.id !== id)]);
        toast.success('Job deleted successfully')
      })
      .catch(err => {
        console.error(err)
        toast.error('Could not delete job')
      })
  }

  const handleEditClick = (event, job) => {
    event.preventDefault()
    setEditJob({
      ...job,
      requirements: job.requirements.join(','),
      benefits: job.benefits.join(',')
    })
    setIsEditModalOpen(true)
  }

  const handleEditFieldsChange = (event) => {
    setEditJob({
      ...editJob,
      [event.target.name]: event.target.value
    })
  }

  const handleJobUpdate = () => {
    jobService.updateJob(editJob.id, editJob)
      .then(response => {
        setIsEditModalOpen(false)
        toast.success(`Job ${response.data.title} updated successfully`);
      })
      .catch(error => {
        console.error(error);
        toast.error('Could not update job');
      })
  }

  return (
    <>
      <CRow>
        <CCol>
          <CCard className="mb-4">
            <CCardHeader>
              <span>Create new job</span>
            </CCardHeader>
            <CCardBody>
              <JobForm
                jobData={createJobRequest}
                onChange={handleFieldsChange}
                onSubmit={handleJobCreate}
                jobStatus={jobStatus}
                setJobStatus={setJobStatus}
                titleError={titleError}
                descriptionError={descriptionError}
                locationError={locationError}
              />
            </CCardBody>
          </CCard>
        </CCol>
      </CRow>
      <CRow>
        <CCol xs>
          <CCard className="mb-4">
          <CCardHeader>
              <div className="d-flex w-100 justify-content-between align-items-center">
                <span>Manage jobs</span>
              </div>
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
                    <CTableHeaderCell className="bg-body-tertiary">Title</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">Location</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">Salary</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-center">Status</CTableHeaderCell>
                    <CTableHeaderCell className="bg-body-tertiary text-end">Actions</CTableHeaderCell>
                  </CTableRow>
                </CTableHead>
                <CTableBody>
                  {jobs.map((item, index) => (
                    <CTableRow v-for="item in tableItems" key={index}>
                      <CTableDataCell>
                        <div className="fw-semibold text-nowrap">{item.title}</div>
                      </CTableDataCell>
                      <CTableDataCell className="text-center">
                        <div className="text-nowrap">{item.location}</div>
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
                            <CDropdownMenu>
                              <CDropdownItem
                                className="d-flex align-items-center"
                                onClick={event => handleJobPublish(event, item.id)}
                              >
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
                            <CDropdownMenu>
                              <CDropdownItem href="#" className="d-flex align-items-center">
                                <FiClock className="me-2" size={16} />
                                Reschedule
                              </CDropdownItem>
                              <CDropdownItem
                                className="d-flex align-items-center"
                                onClick={event => handleJobPublish(event, item.id)}
                              >
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
                            onClick={event => handleJobUnpublish(event, item.id)}
                          >
                            <FiArchive className="me-1" size={16} />
                            Unpublish
                          </CButton>
                        }

                        <CButton
                          className="text-white bg-info me-1 align-items-center"
                          size="sm"
                          onClick={event => handleEditClick(event, item)}
                        >
                          <FiEdit className="me-1" size={16} />
                          Edit
                        </CButton>
                        <CModal visible={isEditModalOpen} onClose={() => setIsEditModalOpen(false)}>
                          <CModalHeader>Edit Job</CModalHeader>
                          <CModalBody>
                            <JobForm
                              jobData={editJob}
                              onChange={handleEditFieldsChange}
                              onSubmit={handleJobUpdate}
                              isEdit={true}
                              titleError={titleError}
                              descriptionError={descriptionError}
                              locationError={locationError}
                            />
                          </CModalBody>
                        </CModal>

                        <CDropdown alignment="end">
                          <CDropdownToggle color="light" className="rounded-1" caret={false} size="sm">
                            <FiMoreHorizontal size={16} />
                          </CDropdownToggle>
                          <CDropdownMenu>
                            <CDropdownItem
                              className="text-danger d-flex align-items-center"
                              onClick={event => handleJobDelete(event, item.id)}>
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
