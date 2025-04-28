import {CBadge, CButton, CCard, CCardBody, CListGroup, CListGroupItem} from "@coreui/react";
import {useEffect, useState} from "react";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

export const FullCalendar = (props) => {
  const [currentMonth, setCurrentMonth] = useState(new Date())
  const [selectedDay, setSelectedDay] = useState(null)
  const [dayInterviews, setDayInterviews] = useState([])

  const formatDate = (rawDate) => {
    return new Date(rawDate).toLocaleDateString("en-US")
  }

  const interviewsByDate = {}
  props.interviews.forEach(interview => {
    const formattedDate = formatDate(interview.interviewAt)

    if (!interviewsByDate[formattedDate]) {
      interviewsByDate[formattedDate] = []
    }
    interviewsByDate[formattedDate].push(interview)
  })

  useEffect(() => {
    if (selectedDay) {
      const formattedDate = formatDate(selectedDay)
      setDayInterviews(interviewsByDate[formattedDate] || [])
    } else {
      setDayInterviews([])
    }
  }, [selectedDay, props.interviews])

  const getDaysInMonth = (year, month) => {
    return new Date(year, month + 1, 0).getDate()
  }

  const getFirstDayOfMonth = (year, month) => {
    return new Date(year, month, 1).getDay()
  }

  const renderCalendar = () => {
    const year = currentMonth.getFullYear()
    const month = currentMonth.getMonth()

    const daysInMonth = getDaysInMonth(year, month)
    const firstDayOfMonth = getFirstDayOfMonth(year, month)

    const days = []

    for (let i = 0; i < firstDayOfMonth; i++) {
      days.push(
        <div
          key={`empty-${i}`}
          className="bg-body-secondary border-end border-bottom"
          style={{ width: "14.28%", height: "80px" }}
        ></div>,
      )
    }

    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(year, month, day)
      const formattedDate = formatDate(date)
      const hasInterviews = interviewsByDate[formattedDate] && interviewsByDate[formattedDate].length > 0
      const interviewCount = hasInterviews ? interviewsByDate[formattedDate].length : 0

      const isSelected =
        selectedDay &&
        date.getDate() === selectedDay.getDate() &&
        date.getMonth() === selectedDay.getMonth() &&
        date.getFullYear() === selectedDay.getFullYear()

      let dayClasses = "border-end border-bottom position-relative p-1"

      if (isSelected && hasInterviews) {
        dayClasses += " bg-primary bg-opacity-25"
      } else if (isSelected) {
        dayClasses += " bg-primary bg-opacity-10"
      } else if (hasInterviews) {
        dayClasses += " bg-primary bg-opacity-10"
      }

      days.push(
        <div
          key={`day-${day}`}
          className={dayClasses}
          style={{ width: "14.28%", height: "80px", cursor: "pointer" }}
          onClick={() => setSelectedDay(date)}
        >
          <div className="fw-bold">{day}</div>
          {hasInterviews && (
            <div className="mt-1">
              <CBadge color="primary" shape="rounded-pill" style={{ fontSize: "0.75rem" }}>
                {interviewCount}
              </CBadge>
            </div>
          )}
        </div>,
      )
    }

    return days
  }

  const prevMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1))
    setSelectedDay(null)
  }

  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 1))
    setSelectedDay(null)
  }

  const monthNames = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ]

  const getStatusBadge = (status) => {
    switch (status) {
      case "SCHEDULED":
        return <CBadge color="warning">Scheduled</CBadge>
      case "CONFIRMED":
        return <CBadge color="success">Confirmed</CBadge>
      case "COMPLETED":
        return <CBadge color="secondary">Completed</CBadge>
      case "CANCELLED":
        return <CBadge color="secondary">Cancelled</CBadge>
      default:
        return <CBadge color="danger">Unknown</CBadge>
    }
  }

  return (
    <div className="full-calendar">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <CButton className="bg-body-secondary" onClick={prevMonth}>
          &lt;
        </CButton>
        <h4 className="mb-0">
          {monthNames[currentMonth.getMonth()]} {currentMonth.getFullYear()}
        </h4>
        <CButton className="bg-body-secondary" onClick={nextMonth}>
          &gt;
        </CButton>
      </div>

      <div className="d-flex flex-column flex-md-row">
        <div className="flex-grow-1 mb-4 mb-md-0">
          <div className="border rounded overflow-hidden">
            <div className="d-flex bg-body-secondary border-bottom">
              {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map((day) => (
                <div
                  key={day}
                  className="fw-bold text-center py-2"
                  style={{width: '14.28%'}}
                >
                  {day}
                </div>
              ))}
            </div>

            <div className="d-flex flex-wrap" style={{minHeight: '500px'}}>
              {renderCalendar()}
            </div>
          </div>
        </div>

        {selectedDay && (
          <div className="ms-0 ms-md-4" style={{flexBasis: '300px'}}>
            <h5>
              Interviews for{" "}
              {selectedDay.toLocaleDateString("en-US", {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric",
              })}
            </h5>

            {dayInterviews.length > 0 ? (
              <CListGroup className="mt-3">
                {dayInterviews.map((interview) => (
                  <CListGroupItem key={interview.id} className="border-start-0 border-end-0">
                    <div className="d-flex justify-content-between align-items-start">
                      <div>
                        <div className="fw-bold">{interview.candidate.firstName} {interview.candidate.lastName}</div>
                        <div className="small text-muted">{interview.candidate.email}</div>
                        <div className="small text-muted">{interview.candidate.phone}</div>
                        <div className="small text-muted">{interview.candidate.job.title}</div>
                        <div className="mt-1">
                          <CBadge color="primary" className="me-1">
                            {capitalize(interview.interviewType)}
                          </CBadge>
                          {getStatusBadge(interview.status)}
                        </div>
                      </div>
                      <div className="text-end">
                        <div>{new Date(interview.interviewAt).toLocaleString("en-US")}</div>
                      </div>
                    </div>
                  </CListGroupItem>
                ))}
              </CListGroup>
            ) : (
              <CCard className="mt-3">
                <CCardBody className="text-center text-muted">No interviews scheduled for this day</CCardBody>
              </CCard>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
