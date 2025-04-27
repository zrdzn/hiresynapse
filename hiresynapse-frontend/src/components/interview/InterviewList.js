import {CBadge, CListGroup, CListGroupItem} from "@coreui/react";
import {capitalize} from "../../hooks/wordCapitalizeUtil";

export const InterviewList = (props) => {
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

  const sortedInterviews = [...props.interviews].sort((a, b) => {
    return new Date(a.interviewAt).getTime() - new Date(b.interviewAt).getTime()
  })

  return (
    <CListGroup>
      {sortedInterviews.length > 0 ? (
        sortedInterviews.slice(0, 3).map((interview) => (
          <CListGroupItem key={interview.id} className="border-start-0 border-end-0">
            <div className="d-flex justify-content-between align-items-start">
              <div>
                <div className="fw-bold">{interview.candidate.firstName} {interview.candidate.lastName}</div>
                <div className="small text-muted">{interview.candidate.email} | {interview.candidate.phone}</div>
                <div className="small text-muted">{interview.candidate.job.title}</div>
                <div className="mt-1">
                  <CBadge color="primary" className="me-1">
                    {capitalize(interview.interviewType)}
                  </CBadge>
                  {getStatusBadge(interview.status)}
                </div>
              </div>
              <div className="text-end">
                <div>{new Date(interview.interviewAt).toLocaleDateString()}</div>
                <div className="small text-muted">{interview.time}</div>
              </div>
            </div>
          </CListGroupItem>
        ))
      ) : (
        <CListGroupItem>No interviews scheduled</CListGroupItem>
      )}
    </CListGroup>
  )
}
