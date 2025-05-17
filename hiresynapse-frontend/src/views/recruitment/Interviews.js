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
  CFormCheck,
  CFormRange,
  CFormSelect,
  CFormTextarea,
  CProgress,
  CRow,
  CSpinner,
  CTable,
  CTableBody,
  CTableDataCell,
  CTableHead,
  CTableHeaderCell,
  CTableRow
} from "@coreui/react";
import {SelectCalendar} from "../../components/interview/SelectCalendar";
import {CandidateSearch} from "../../components/interview/CandidateSearch";
import {InterviewList} from "../../components/interview/InterviewList";
import {FullCalendar} from "../../components/interview/FullCalendar";
import {toast} from "react-toastify";
import {interviewService} from "../../services/interviewService";
import {FiMoreHorizontal, FiTrash2} from "react-icons/fi";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

const Interviews = () => {
  const [selectedDate, setSelectedDate] = useState(new Date())
  const [selectedCandidate, setSelectedCandidate] = useState(null)
  const [interviewType, setInterviewType] = useState("TECHNICAL")
  const [timeSlot, setTimeSlot] = useState("")
  const [interviewStatus, setInterviewStatus] = useState("SCHEDULED")
  const [notes, setNotes] = useState("")
  const [questions, setQuestions] = useState(false)
  const [questionsAmount, setQuestionsAmount] = useState(5)
  const [interviews, setInterviews] = useState([])
  const [confirmedInterviews, setConfirmedInterviews] = useState([])
  const [unconfirmedInterviews, setUnconfirmedInterviews] = useState([])
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    interviewService.getConfirmedInterviews()
      .then(response => {
        setConfirmedInterviews(response.data)
        return interviewService.getUnconfirmedInterviews()
      })
      .then(response => {
        setUnconfirmedInterviews(response.data)
        return interviewService.getInterviews()
      })
      .then(response => {
        setInterviews(response.data)
        setLoading(false)
      })
      .catch(err => {
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }, []);

  const handleScheduleInterview = (event) => {
    event.preventDefault()
    if (!selectedCandidate || !interviewType || !selectedDate || !timeSlot) return

    const dateTime = new Date(selectedDate)
    const [hours, minutes] = timeSlot.split(":")
    dateTime.setHours(parseInt(hours), parseInt(minutes))

    const request = {
      candidateId: selectedCandidate.id,
      interviewAt: dateTime.toISOString(),
      interviewType: interviewType,
      interviewStatus: interviewStatus,
      notes: notes,
      enableQuestions: questions,
      questionsAmount: questionsAmount
    }

    interviewService.createInterview(request)
      .then(response => {
        if (interviewStatus === "CONFIRMED") {
          setConfirmedInterviews([...confirmedInterviews, response.data])
        }

        if (interviewStatus === "SCHEDULED") {
          setUnconfirmedInterviews([...unconfirmedInterviews, response.data])
        }

        setInterviews([...interviews, response.data])
        toast.success("Interview scheduled successfully")
      })
      .catch(err => {
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }

  const handleInterviewDelete = (event, id) => {
    event.preventDefault()

    interviewService.deleteInterview(id)
      .then(() => {
        setInterviews([...interviews.filter(interview => interview.id !== id)]);
        toast.success('Interview deleted successfully')
      })
      .catch(error => {
        console.error(error)
        toast.error('Could not delete interview')
      })
  }

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
  }

  const handleMarkAsCompleted = (event, id) => {
    event.preventDefault()

    interviewService.markAsCompleted(id)
      .then(() => {
        toast.success('Interview marked as completed successfully')
        setInterviews([...interviews.map(interview => interview.id === id ? {...interview, status: 'COMPLETED'} : interview)]);
      })
      .catch(error => {
        toast.error('Could not mark interview as completed')
        console.error(error)
      })
  }

  const handleCancel = (event, id) => {
    event.preventDefault()

    interviewService.cancel(id)
      .then(() => {
        toast.success('Interview cancelled successfully')
        setInterviews([...interviews.map(interview => interview.id === id ? {...interview, status: 'CANCELLED'} : interview)]);
      })
      .catch(error => {
        toast.error('Could not cancel interview')
        console.error(error)
      })
  }

  return (
    <div className="min-vh-100 d-flex flex-row">
      <div className="container-fluid">
        <CRow>
          <CCol md={8}>
            <CCard className="mb-4">
              <CCardHeader>
                <span>Schedule interview</span>
              </CCardHeader>
              <CCardBody>
                <CForm>
                  <CRow className="mb-3">
                    <CCol md={6}>
                      <CandidateSearch
                        selectedCandidate={selectedCandidate}
                        onSelectCandidate={setSelectedCandidate} />
                    </CCol>
                    <CCol md={6}>
                      <label className="form-label">Interview type</label>
                      <CFormSelect value={interviewType} onChange={event => setInterviewType(event.target.value)}>
                        <option value="TECHNICAL">Technical</option>
                        <option value="BEHAVIORAL">Behavioral</option>
                        <option value="CASE">Case</option>
                        <option value="HR">HR</option>
                        <option value="SCREENING">Screening</option>
                        <option value="PANEL">Panel</option>
                        <option value="TAKE_HOME_ASSIGNMENT">Take home assignment</option>
                        <option value="PORTFOLIO">Portfolio</option>
                        <option value="OTHER">Other</option>
                      </CFormSelect>
                    </CCol>
                  </CRow>

                  <CRow className="mb-3">
                    <CCol md={6}>
                      <label className="form-label">Select date</label>
                      <SelectCalendar selectedDate={selectedDate} onSelectDate={setSelectedDate}/>
                    </CCol>
                    <CCol md={6}>
                      <label className="form-label">Select hour</label>
                      <CFormSelect
                        value={timeSlot}
                        onChange={event => setTimeSlot(event.target.value)}
                        disabled={!selectedDate}
                      >
                        <option value="09:00">9:00 AM</option>
                        <option value="10:00">10:00 AM</option>
                        <option value="11:00">11:00 AM</option>
                        <option value="13:00">1:00 PM</option>
                        <option value="14:00">2:00 PM</option>
                        <option value="15:00">3:00 PM</option>
                        <option value="16:00">4:00 PM</option>
                      </CFormSelect>

                      <div className="mt-3">
                        <label className="form-label">Notes</label>
                        <CFormTextarea
                          rows={3}
                          onChange={event => setNotes(event.target.value)}
                          value={notes}
                          placeholder="Add topics to cover, questions to ask or other notes"
                        ></CFormTextarea>
                      </div>
                      <div className="mt-3">
                        <CFormCheck
                          id="check1"
                          label="Generate questions by AI"
                          defaultChecked={questions}
                          onChange={event => setQuestions(event.target.checked)}
                        />
                      </div>
                      <div className="mt-3">
                        <CFormRange
                          min={1}
                          max={10}
                          label={`Generate ${questionsAmount} questions`}
                          disabled={!questions}
                          defaultValue="5"
                          onChange={event => setQuestionsAmount(event.target.value)}
                        />
                      </div>
                      <div className="mt-3">
                        <CFormCheck
                          id="check2"
                          label="Auto confirm interview after creation"
                          defaultChecked={interviewStatus === "CONFIRMED"}
                          onChange={event =>
                            event.target.checked === true ?
                              setInterviewStatus("CONFIRMED") :
                              setInterviewStatus("SCHEDULED")}
                        />
                      </div>
                    </CCol>
                  </CRow>

                  <CButton
                    color="primary"
                    className="mt-3"
                    onClick={handleScheduleInterview}
                    disabled={!selectedCandidate || !selectedDate || !timeSlot || !interviewType}
                  >
                    Schedule
                  </CButton>
                </CForm>
              </CCardBody>
            </CCard>
          </CCol>

          <CCol md={4}>
            <CCard>
              <CCardHeader>
                <span>Upcoming</span>
              </CCardHeader>
              <CCardBody>
                <InterviewList interviews={confirmedInterviews} />
              </CCardBody>
            </CCard>
            <CCard className={"mt-4"}>
              <CCardHeader>
                <span>Unconfirmed</span>
              </CCardHeader>
              <CCardBody>
                <InterviewList interviews={unconfirmedInterviews} />
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>

        <CRow className="mt-4">
          <CCol>
            <CCard>
              <CCardHeader>
                <div className="d-flex justify-content-between align-items-center">
                  <span>Interviews</span>
                </div>
              </CCardHeader>
              <CCardBody>
                <FullCalendar interviews={interviews} />
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
        <CRow className="mt-4">
          <CCol xs>
            <CCard className="mb-4">
              <CCardHeader>
                <div className="d-flex w-100 justify-content-between align-items-center">
                  <span>Manage interviews</span>
                </div>
              </CCardHeader>
              <CCardBody>
                <CRow>
                  <CCol xs={12} md={6} xl={6}>
                    <CRow>
                      <CCol xs={6}>
                        <div className="border-start border-start-4 border-start-info py-1 px-3">
                          <div className="text-body-secondary text-truncate small">All</div>
                          <div className="fs-5 fw-semibold">{interviews.length}</div>
                        </div>
                      </CCol>
                      <CCol xs={6}>
                        <div className="border-start border-start-4 border-start-danger py-1 px-3 mb-3">
                          <div className="text-body-secondary text-truncate small">Cancelled</div>
                          <div className="fs-5 fw-semibold">{interviews.filter(interview => interview.status === 'CANCELLED').length}</div>
                        </div>
                      </CCol>
                    </CRow>
                  </CCol>
                  <CCol xs={12} md={6} xl={6}>
                    <CRow>
                      <CCol xs={6}>
                        <div className="border-start border-start-4 border-start-warning py-1 px-3 mb-3">
                          <div className="text-body-secondary text-truncate small">Scheduled</div>
                          <div className="fs-5 fw-semibold">{unconfirmedInterviews.length}</div>
                        </div>
                      </CCol>
                      <CCol xs={6}>
                        <div className="border-start border-start-4 border-start-success py-1 px-3 mb-3">
                          <div className="text-body-secondary text-truncate small">Confirmed & Completed</div>
                          <div className="fs-5 fw-semibold">{interviews.filter(interview =>
                            interview.status === 'CONFIRMED' ||
                            interview.status === 'COMPLETED'
                          ).length}</div>
                        </div>
                      </CCol>
                    </CRow>
                  </CCol>
                </CRow>
                <CTable align="middle" className="mb-0 border" hover responsive>
                  <CTableHead className="text-nowrap">
                    <CTableRow>
                      <CTableHeaderCell className="bg-body-tertiary">Candidate</CTableHeaderCell>
                      <CTableHeaderCell className="bg-body-tertiary text-center">Recruiter</CTableHeaderCell>
                      <CTableHeaderCell className="bg-body-tertiary text-center">Job</CTableHeaderCell>
                      <CTableHeaderCell className="bg-body-tertiary text-center">Type</CTableHeaderCell>
                      <CTableHeaderCell className="bg-body-tertiary text-center">Status</CTableHeaderCell>
                      <CTableHeaderCell className="bg-body-tertiary text-end">Actions</CTableHeaderCell>
                    </CTableRow>
                  </CTableHead>
                  <CTableBody>
                    {interviews.map((item, index) => (
                      <CTableRow v-for="item in tableItems" key={index}>
                        <CTableDataCell className="fw-semibold">
                          <div>{item.candidate.firstName} {item.candidate.lastName}</div>
                          <div className="small text-body-secondary text-nowrap">
                            <span>{item.candidate.email}</span> | {item.candidate.phone}
                          </div>
                        </CTableDataCell>
                        <CTableDataCell className="text-center">
                          <div>{item.recruiter.firstName} {item.recruiter.lastName}</div>
                          <div className="small text-body-secondary text-nowrap">
                            <span>{item.recruiter.email}</span>
                          </div>
                        </CTableDataCell>
                        <CTableDataCell className="text-center">{item.candidate.job.title}</CTableDataCell>
                        <CTableDataCell className="text-center fw-semibold">
                          {capitalize(item.interviewType)}
                        </CTableDataCell>
                        <CTableDataCell className="text-center">
                          <div className="fw-semibold text-center mb-1">{capitalize(item.status)}</div>
                          <CProgress thin
                                     color={item.status === 'CANCELLED' ? 'danger' : item.status === 'SCHEDULED' ? 'warning' : 'success'}
                                     value="100" />
                        </CTableDataCell>
                        <CTableDataCell className="text-end">
                          {
                            item.status === 'SCHEDULED' && (
                              <>
                                <CButton
                                  onClick={event => handleMarkAsCompleted(event, item.id)}
                                  className="text-white bg-success"
                                  size="sm">
                                  Mark as completed
                                </CButton>
                                <CButton
                                  onClick={event => handleCancel(event, item.id)}
                                  className="text-white bg-danger mx-1"
                                  size="sm">
                                  Cancel
                                </CButton>
                              </>
                            )
                          }
                          <CDropdown alignment="end">
                            <CDropdownToggle color="light" className="rounded-1" caret={false} size="sm">
                              <FiMoreHorizontal size={16} />
                            </CDropdownToggle>
                            <CDropdownMenu>
                              <CDropdownItem
                                className="text-danger d-flex align-items-center"
                                onClick={event => handleInterviewDelete(event, item.id)}>
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
      </div>
    </div>
  )
}

export default Interviews
