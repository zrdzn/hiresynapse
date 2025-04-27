import React, {useEffect, useState} from 'react'
import {
  CButton,
  CCard,
  CCardBody,
  CCardHeader,
  CCol,
  CForm,
  CFormCheck,
  CFormRange,
  CFormSelect,
  CFormTextarea,
  CRow,
  CSpinner
} from "@coreui/react";
import {SelectCalendar} from "../../components/interview/SelectCalendar";
import {CandidateSearch} from "../../components/interview/CandidateSearch";
import {InterviewList} from "../../components/interview/InterviewList";
import {FullCalendar} from "../../components/interview/FullCalendar";
import {toast} from "react-toastify";
import {interviewService} from "../../services/interviewService";

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
  const [upcomingInterviews, setUpcomingInterviews] = useState([])
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    interviewService.getUpcomingInterviews()
      .then(response => {
        setUpcomingInterviews(response.data)
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
      notes: notes,
      enableQuestions: questions,
      questionsAmount: questionsAmount
    }

    interviewService.createInterview(request)
      .then(response => {
        setUpcomingInterviews([...interviews, response.data])
        toast.success("Interview scheduled successfully")
      })
      .catch(err => {
        console.error(err)
        if (Array.isArray(err.response.data)) {
          err.response.data.forEach(message => toast.error(message))
        }
      })
  }

  if (loading) {
    return (
      <div className="pt-3 text-center">
        <CSpinner />
      </div>
    );
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
                <InterviewList interviews={upcomingInterviews} />
              </CCardBody>
            </CCard>
            <CCard className={"mt-4"}>
              <CCardHeader>
                <span>Unconfirmed</span>
              </CCardHeader>
              <CCardBody>
                <InterviewList interviews={interviews} />
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
                <FullCalendar interviews={upcomingInterviews} />
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
      </div>
    </div>
  )
}

export default Interviews
